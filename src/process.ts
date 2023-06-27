import {Configuration} from "./configuration/configuration";
import glob from "glob";
import ts, {CompilerOptions} from "typescript";
import path from "path";
import nodeProcess from "process";
import fs from "fs";
import {commonPrefix} from "./utils/fileName";
import {createContext} from "./converter/context";
import {ConverterPlugin, createSimplePlugin, SimpleConverterPlugin} from "./converter/plugin";
import {createPlugins} from "./defaultPlugins";
import {CommentsPlugin} from "./converter/plugins/CommentsPlugin";
import {prepareStructure} from "./structure/prepareStructure";
import {traverse} from "./utils/traverse";
import minimatch from "minimatch";
import {createTargetFile} from "./structure/createTargetFile";
import {createRender} from "./converter/render";
import {NameResolver} from "./converter/nameResolver";
import {AnnotationsPlugin} from "./converter/plugins/AnnotationsPlugin";
import {InheritanceModifier} from "./converter/inheritanceModifier";
import {Annotation} from "./converter/annotation";
import {collectNamespaceInfo} from "./structure/namespace/collectNamespaceInfo";
import {collectSourceFileInfo} from "./structure/sourceFile/collectSourceFileInfo";
import {packageToOutputFileName} from "./structure/package/packageToFileName";

export const defaultPluginPatterns = [
    "karakum/plugins/*.js"
]

export const defaultAnnotationPatterns = [
    "karakum/annotations/*.js"
]

export const defaultNameResolverPatterns = [
    "karakum/nameResolvers/*.js"
]

export const defaultInheritanceModifierPatterns = [
    "karakum/inheritanceModifiers/*.js"
]

function normalizeOption(
    patterns: string | string[] | undefined,
    defaultValue: string[] = [],
) {
    return patterns !== undefined
        ? typeof patterns === "string" ? [patterns] : patterns
        : defaultValue
}

async function loadExtensions<T>(
    name: string,
    patterns: string | string[] | undefined,
    defaultValue: string[],
    loader: (extension: unknown) => T = extension => extension as T
): Promise<T[]> {
    const normalizedPatterns = normalizeOption(patterns, defaultValue)

    const fileNames = normalizedPatterns.flatMap(pattern => glob.sync(pattern, {
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

export async function process(configuration: Configuration) {
    const {
        input,
        inputRoots,
        output,
        ignoreInput,
        ignoreOutput,
        plugins,
        annotations,
        nameResolvers,
        inheritanceModifiers,
        compilerOptions,
    } = configuration

    const normalizedInput = normalizeOption(input)
    const normalizedIgnoreInput = normalizeOption(ignoreInput)
    const normalizedIgnoreOutput = normalizeOption(ignoreOutput)

    const customPlugins = await loadExtensions<ConverterPlugin>(
        "Plugin",
        plugins,
        defaultPluginPatterns,
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
        defaultAnnotationPatterns,
    )

    const customNameResolvers = await loadExtensions<NameResolver>(
        "Name Resolver",
        nameResolvers,
        defaultNameResolverPatterns,
    )

    const customInheritanceModifiers = await loadExtensions<InheritanceModifier>(
        "Inheritance Modifier",
        inheritanceModifiers,
        defaultInheritanceModifierPatterns,
    )

    const rootNames = normalizedInput.flatMap(pattern => glob.sync(pattern, {
        absolute: true,
        ignore: ignoreInput,
    }))

    const preparedCompilerOptions: CompilerOptions = {
        lib: ["lib.esnext.d.ts"],
        types: [],
        strict: true,
        ...compilerOptions
    }

    const compilerHost = ts.createCompilerHost(preparedCompilerOptions, /* setParentNodes */ true);

    const program = ts.createProgram(rootNames, preparedCompilerOptions, compilerHost)

    const sources = rootNames.map(fileName => fileName.split("/"))

    const defaultInputRoot = rootNames.length === 1
        ? path.dirname(rootNames[0]) + "/"
        : commonPrefix(...sources).join("/") + "/"

    const normalizedInputRoots = normalizeOption(inputRoots, [defaultInputRoot])

    const fixedConfiguration = {
        ...configuration,
        inputRoots: normalizedInputRoots,
    }

    for (const inputRoot of normalizedInputRoots) {
        console.log(`Source files root: ${inputRoot}`)
    }


    if (fs.existsSync(output)) {
        fs.rmSync(output, {recursive: true})
    }
    fs.mkdirSync(output, {recursive: true})

    const absoluteInput = normalizedInput
        .map(pattern => path.isAbsolute(pattern) ? pattern : path.join(nodeProcess.cwd(), pattern))

    const absoluteIgnoreInput = normalizedIgnoreInput
        .map(pattern => path.isAbsolute(pattern) ? pattern : path.join(nodeProcess.cwd(), pattern))

    const sourceFiles = program.getSourceFiles()
        .filter(sourceFile => {
            return absoluteInput.every(pattern => minimatch(sourceFile.fileName, pattern))
        })
        .filter(sourceFile => {
            return absoluteIgnoreInput.every(pattern => !minimatch(sourceFile.fileName, pattern))
        })

    console.log(`Source files count: ${sourceFiles.length}`)

    const namespaceInfo = collectNamespaceInfo(normalizedInputRoots, sourceFiles, fixedConfiguration)
    const sourceFileInfo = collectSourceFileInfo(normalizedInputRoots, sourceFiles, fixedConfiguration)

    const structure = prepareStructure(
        [
            ...namespaceInfo.filter(it => it.strategy === "package"),
            ...sourceFileInfo,
        ],
        fixedConfiguration,
    )

    const defaultPlugins = createPlugins(
        fixedConfiguration,
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

            const outputFileName = packageToOutputFileName(item.package, item.fileName, fixedConfiguration)

            const targetFileName = path.resolve(output, outputFileName)

            console.log(`Target file: ${targetFileName}`)

            const convertedBody = item.statements
                .map(node => render(node))
                .join("\n\n")

            const targetFile = createTargetFile(
                {
                    ...item,
                    body: convertedBody,
                },
                fixedConfiguration,
            )

            if (normalizedIgnoreOutput.every(pattern => !minimatch(targetFileName, pattern))) {
                fs.mkdirSync(path.dirname(targetFileName), {recursive: true})
                fs.writeFileSync(targetFileName, targetFile)
            }
        })

    for (const plugin of converterPlugins) {
        const generated = plugin.generate(context)

        for (const [fileName, content] of Object.entries(generated)) {
            if (normalizedIgnoreOutput.every(pattern => !minimatch(fileName, pattern))) {
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
