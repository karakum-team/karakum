import {PartialConfiguration} from "./configuration/configuration.js";
import {defaultizeConfiguration} from "./configuration/defaultizeConfiguration.js";
import {glob} from "glob";
import ts, {CompilerOptions} from "typescript";
import path from "node:path";
import fs from "node:fs";
import {createContext} from "./converter/context.js";
import {ConverterPlugin, createSimplePlugin, SimpleConverterPlugin} from "./converter/plugin.js";
import {createPlugins} from "./defaultPlugins.js";
import {CommentsPlugin} from "./converter/plugins/CommentsPlugin.js";
import {prepareStructure} from "./structure/prepareStructure.js";
import {traverse} from "./utils/traverse.js";
import {minimatch} from "minimatch";
import {createTargetFile} from "./structure/createTargetFile.js";
import {createRender} from "./converter/render.js";
import {NameResolver} from "./converter/nameResolver.js";
import {AnnotationsPlugin} from "./converter/plugins/AnnotationsPlugin.js";
import {InheritanceModifier} from "./converter/inheritanceModifier.js";
import {Annotation} from "./converter/annotation.js";
import {collectNamespaceInfo} from "./structure/namespace/collectNamespaceInfo.js";
import {collectSourceFileInfo} from "./structure/sourceFile/collectSourceFileInfo.js";
import {packageToOutputFileName} from "./structure/package/packageToFileName.js";
import {toPosix, toModuleName} from "./utils/path.js";

async function loadExtensions<T>(
    name: string,
    patterns: string[],
    cwd: string,
    loader: (extension: unknown) => T = extension => extension as T
): Promise<T[]> {
    const fileNames = patterns.flatMap(pattern => glob.sync(pattern, {
        cwd,
        absolute: true,
        posix: true,
    }))

    const extensions: T[] = []

    for (const fileName of fileNames) {
        console.log(`${name} file: ${fileName}`)

        const extensionModule: { default: unknown } = await import(toModuleName(fileName))
        const extension = extensionModule.default

        extensions.push(loader(extension))
    }

    return extensions
}

export async function generate(partialConfiguration: PartialConfiguration) {
    const configuration = defaultizeConfiguration(partialConfiguration)

    const {
        inputRoots,
        inputFileNames,
        input,
        output,
        outputFileName,
        ignoreInput,
        ignoreOutput,
        plugins,
        annotations,
        nameResolvers,
        inheritanceModifiers,
        compilerOptions,
        cwd,
    } = configuration

    const customPlugins = await loadExtensions<ConverterPlugin>(
        "Plugin",
        plugins,
        cwd,
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
        cwd,
    )

    const customNameResolvers = await loadExtensions<NameResolver>(
        "Name Resolver",
        nameResolvers,
        cwd,
    )

    const customInheritanceModifiers = await loadExtensions<InheritanceModifier>(
        "Inheritance Modifier",
        inheritanceModifiers,
        cwd,
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


    if (outputFileName) {
        if (fs.existsSync(outputFileName)) {
            fs.rmSync(outputFileName, {recursive: true})
        }
    } else {
        if (fs.existsSync(output)) {
            fs.rmSync(output, {recursive: true})
        }
    }
    fs.mkdirSync(output, {recursive: true})

    const sourceFiles = program.getSourceFiles()
        .filter(sourceFile => {
            const relativeFileName = path.relative(cwd, sourceFile.fileName)
            return input.some(pattern => {
                if (path.isAbsolute(pattern)) {
                    return minimatch(sourceFile.fileName, pattern);
                } else {
                    return minimatch(relativeFileName, pattern);
                }
            })
        })
        .filter(sourceFile => {
            const relativeFileName = path.relative(cwd, sourceFile.fileName)
            return ignoreInput.every(pattern => {
                if (path.isAbsolute(pattern)) {
                    return !minimatch(sourceFile.fileName, pattern);
                } else {
                    return !minimatch(relativeFileName, pattern);
                }
            })
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

            console.log(`Target file: ${toPosix(targetFileName)}`)

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
                    console.log(`Generated file: ${toPosix(fileName)}`)

                    fs.writeFileSync(fileName, content)
                }
            }
        }
    }
}
