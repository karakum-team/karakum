import {PartialConfiguration} from "./configuration/configuration.js";
import {defaultizeConfiguration} from "./configuration/defaultizeConfiguration.js";
import glob from "glob";
import ts, {CompilerOptions} from "typescript";
import path from "path";
import nodeProcess from "process";
import fs from "fs";
import {createContext} from "./converter/context.js";
import {ConverterPlugin, createSimplePlugin, SimpleConverterPlugin} from "./converter/plugin.js";
import {createPlugins} from "./defaultPlugins.js";
import {CommentsPlugin} from "./converter/plugins/CommentsPlugin.js";
import {prepareStructure} from "./structure/prepareStructure.js";
import {traverse} from "./utils/traverse.js";
import minimatch from "minimatch";
import {createTargetFile} from "./structure/createTargetFile.js";
import {createRender} from "./converter/render.js";
import {NameResolver} from "./converter/nameResolver.js";
import {AnnotationsPlugin} from "./converter/plugins/AnnotationsPlugin.js";
import {InheritanceModifier} from "./converter/inheritanceModifier.js";
import {Annotation} from "./converter/annotation.js";
import {collectNamespaceInfo} from "./structure/namespace/collectNamespaceInfo.js";
import {collectSourceFileInfo} from "./structure/sourceFile/collectSourceFileInfo.js";
import {packageToOutputFileName} from "./structure/package/packageToFileName.js";

async function loadExtensions<T>(
    name: string,
    patterns: string[],
    loader: (extension: unknown) => T = extension => extension as T
): Promise<T[]> {
    const fileNames = patterns.flatMap(pattern => glob.sync(pattern, {
        absolute: true,
    }))

    const extensions: T[] = []

    for (const fileName of fileNames) {
        console.log(`${name} file: ${fileName}`)

        const extensionModule: { default: unknown } = await import(fileName)
        const extension = extensionModule.default

        extensions.push(loader(extension))
    }

    return extensions
}

export async function process(partialConfiguration: PartialConfiguration) {
    const configuration = defaultizeConfiguration(partialConfiguration)

    const {
        inputRoots,
        inputFileNames,
        input,
        output,
        ignoreInput,
        ignoreOutput,
        plugins,
        annotations,
        nameResolvers,
        inheritanceModifiers,
        compilerOptions,
    } = configuration

    const customPlugins = await loadExtensions<ConverterPlugin>(
        "Plugin",
        plugins,
        plugin => {
            if (typeof plugin === "function") {
                return createSimplePlugin(plugin as SimpleConverterPlugin)
            } else {
                return plugin as ConverterPlugin
            }
        }
    )

    const customAnnotations = await loadExtensions<Annotation>(
        "Annotation",
        annotations,
    )

    const customNameResolvers = await loadExtensions<NameResolver>(
        "Name Resolver",
        nameResolvers,
    )

    const customInheritanceModifiers = await loadExtensions<InheritanceModifier>(
        "Inheritance Modifier",
        inheritanceModifiers,
    )

    const preparedCompilerOptions: CompilerOptions = {
        lib: ["lib.esnext.d.ts"],
        types: [],
        strict: true,
        ...compilerOptions
    }

    const compilerHost = ts.createCompilerHost(preparedCompilerOptions, /* setParentNodes */ true);

    const program = ts.createProgram(inputFileNames, preparedCompilerOptions, compilerHost)

    for (const inputRoot of inputRoots) {
        console.log(`Source files root: ${inputRoot}`)
    }


    if (fs.existsSync(output)) {
        fs.rmSync(output, {recursive: true})
    }
    fs.mkdirSync(output, {recursive: true})

    const absoluteInput = input
        .map(pattern => path.isAbsolute(pattern) ? pattern : path.join(nodeProcess.cwd(), pattern))

    const absoluteIgnoreInput = ignoreInput
        .map(pattern => path.isAbsolute(pattern) ? pattern : path.join(nodeProcess.cwd(), pattern))

    const sourceFiles = program.getSourceFiles()
        .filter(sourceFile => {
            return absoluteInput.some(pattern => minimatch(sourceFile.fileName, pattern))
        })
        .filter(sourceFile => {
            return absoluteIgnoreInput.every(pattern => !minimatch(sourceFile.fileName, pattern))
        })

    console.log(`Source files count: ${sourceFiles.length}`)

    const namespaceInfo = collectNamespaceInfo(sourceFiles, configuration)
    const sourceFileInfo = collectSourceFileInfo(sourceFiles, configuration)

    const structure = prepareStructure(
        [
            ...namespaceInfo.filter(it => it.strategy === "package"),
            ...sourceFileInfo,
        ],
        configuration,
    )

    const defaultPlugins = createPlugins(
        configuration,
        customNameResolvers,
        customInheritanceModifiers,
        program,
        namespaceInfo,
    )

    const converterPlugins = [
        // it is important to handle comments and annotations at first
        new CommentsPlugin(),
        new AnnotationsPlugin(customAnnotations),

        ...customPlugins,
        ...defaultPlugins
    ]

    const context = createContext()

    for (const plugin of converterPlugins) {
        plugin.setup(context)
    }

    structure
        .flatMap(it => it.statements)
        .forEach(node => {
            traverse(node, node => {
                for (const plugin of converterPlugins) {
                    plugin.traverse(node, context)
                }
            })
        })

    const render = createRender(context, converterPlugins)

    structure
        .forEach(item => {
            console.log(`${item.meta.type}: ${item.meta.name}`)

            const outputFileName = packageToOutputFileName(item.package, item.fileName, configuration)

            const targetFileName = path.resolve(output, outputFileName)

            console.log(`Target file: ${targetFileName}`)

            const convertedBody = item.statements
                .map(node => render(node))
                .join("\n\n")

            const targetFile = createTargetFile(
                item,
                convertedBody,
                configuration,
            )

            if (ignoreOutput.every(pattern => !minimatch(targetFileName, pattern))) {
                fs.mkdirSync(path.dirname(targetFileName), {recursive: true})
                fs.writeFileSync(targetFileName, targetFile)
            }
        })

    for (const plugin of converterPlugins) {
        const generated = plugin.generate(context)

        for (const [fileName, content] of Object.entries(generated)) {
            if (ignoreOutput.every(pattern => !minimatch(fileName, pattern))) {
                if (fs.existsSync(fileName)) {
                    fs.appendFileSync(fileName, content)
                } else {
                    console.log(`Generated file: ${fileName}`)

                    fs.writeFileSync(fileName, content)
                }
            }
        }
    }
}
