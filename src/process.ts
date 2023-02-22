import {Configuration} from "./configuration/configuration";
import glob from "glob";
import ts, {CompilerOptions} from "typescript";
import path from "path";
import {commonPrefix} from "./utils/fileName";
import fs from "fs";
import {createContext} from "./converter/context";
import {ConverterPlugin, createSimplePlugin, SimpleConverterPlugin} from "./converter/plugin";
import {createPlugins} from "./defaultPlugins";
import {CommentsPlugin} from "./converter/plugins/CommentsPlugin";
import {prepareFileStructure} from "./structure/prepareFileStructure";
import {traverse} from "./utils/traverse";
import minimatch from "minimatch";
import {createTargetFile} from "./structure/createTargetFile";
import {createRender} from "./converter/render";
import {NameResolver} from "./converter/nameResolver";

export const defaultPluginPatterns = [
    "karakum/plugins/*.js"
]

export const defaultNameResolverPatterns = [
    "karakum/nameResolvers/*.js"
]

export const ignoreLibPatterns = [
    "**/typescript/lib/**"
]

function normalizeGlob(patterns: string | string[] | undefined, defaultValue: string[] = []) {
    return patterns !== undefined
        ? typeof patterns === "string" ? [patterns] : patterns
        : defaultValue
}

export async function process(configuration: Configuration) {
    const {
        input,
        output,
        ignoreInput,
        ignoreOutput,
        plugins,
        nameResolvers,
        compilerOptions,
    } = configuration

    const normalizedInput = normalizeGlob(input)
    const normalizedIgnoreInput = normalizeGlob(ignoreInput)
    const normalizedIgnoreOutput = normalizeGlob(ignoreOutput)
    const normalizedPlugins = normalizeGlob(plugins, defaultPluginPatterns)
    const normalizedNameResolvers = normalizeGlob(nameResolvers, defaultNameResolverPatterns)

    const rootNames = normalizedInput.flatMap(pattern => glob.sync(pattern, {
        absolute: true,
        ignore: ignoreInput,
    }))

    const pluginFileNames = normalizedPlugins.flatMap(pattern => glob.sync(pattern, {
        absolute: true,
    }))

    const nameResolverFileNames = normalizedNameResolvers.flatMap(pattern => glob.sync(pattern, {
        absolute: true,
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

    const sourceFileRoot = rootNames.length === 1
        ? path.dirname(rootNames[0]) + "/"
        : commonPrefix(...sources).join("/") + "/"

    console.log(`Source files root: ${sourceFileRoot}`)

    if (fs.existsSync(output)) {
        fs.rmSync(output, {recursive: true})
    }
    fs.mkdirSync(output, {recursive: true})

    const context = createContext()

    const customPlugins: ConverterPlugin[] = []

    for (const pluginFileName of pluginFileNames) {
        console.log(`Plugin file: ${pluginFileName}`)

        const pluginModule: { default: unknown } = await import(pluginFileName)
        const plugin = pluginModule.default

        if (typeof plugin === "function") {
            customPlugins.push(createSimplePlugin(plugin as SimpleConverterPlugin))
        } else {
            customPlugins.push(plugin as ConverterPlugin)
        }
    }

    const customNameResolvers: NameResolver[] = []

    for (const nameResolverFileName of nameResolverFileNames) {
        console.log(`Name Resolver file: ${nameResolverFileName}`)

        const nameResolverModule: { default: unknown } = await import(nameResolverFileName)
        const nameResolver = nameResolverModule.default

        customNameResolvers.push(nameResolver as NameResolver)
    }

    const defaultPlugins = createPlugins(sourceFileRoot, configuration, customNameResolvers, program)

    const converterPlugins = [
        // it is important to handle comments at first
        new CommentsPlugin(),

        ...customPlugins,
        ...defaultPlugins
    ]

    for (const plugin of converterPlugins) {
        plugin.setup(context)
    }

    const preparedIgnoreInput = [
        ...normalizedIgnoreInput,
        ...ignoreLibPatterns,
    ]

    const sourceFiles = program.getSourceFiles()
        .filter(sourceFile => {
            return preparedIgnoreInput.every(pattern => !minimatch(sourceFile.fileName, pattern))
        })

    console.log(`Source files count: ${sourceFiles.length}`)

    const fileStructure = prepareFileStructure(sourceFileRoot, sourceFiles, configuration)

    fileStructure
        .flatMap(it => it.nodes)
        .forEach(node => {
            traverse(node, node => {
                for (const plugin of converterPlugins) {
                    plugin.traverse(node, context)
                }
            })
        })

    const render = createRender(context, converterPlugins)

    fileStructure
        .forEach(item => {
            const targetFileName = path.resolve(output, item.outputFileName)

            console.log(`Source file: ${item.sourceFileName}`)
            console.log(`Target file: ${targetFileName}`)

            const convertedBody = item.nodes
                .map(node => render(node))
                .join("\n\n")

            const targetFile = createTargetFile(
                sourceFileRoot,
                item.sourceFileName,
                item.outputFileName,
                item.packageName,
                item.hasRuntime,
                convertedBody,
                configuration,
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
