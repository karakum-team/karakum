import {Configuration} from "./configuration/configuration";
import glob from "glob";
import ts from "typescript";
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

export const defaultPluginPatterns = [
    "karakum/**/*.js"
]

export async function process(configuration: Configuration) {
    const {input, output, ignore, plugins: pluginPatterns, compilerOptions} = configuration

    const normalizedInput = typeof input === "string" ? [input] : input
    const normalizedIgnore = ignore !== undefined
        ? typeof ignore === "string" ? [ignore] : ignore
        : []
    const normalizedPluginPatterns = pluginPatterns !== undefined
        ? typeof pluginPatterns === "string" ? [pluginPatterns] : pluginPatterns
        : defaultPluginPatterns

    const rootNames = normalizedInput.flatMap(pattern => glob.sync(pattern, {
        absolute: true,
        ignore,
    }))

    const pluginFileNames = normalizedPluginPatterns.flatMap(pattern => glob.sync(pattern, {
        absolute: true,
    }))

    const preparedCompilerOptions = {
        lib: [],
        types: [],
        ...compilerOptions
    }

    const compilerHost = ts.createCompilerHost(preparedCompilerOptions, /* setParentNodes */ true);

    const program = ts.createProgram(rootNames, preparedCompilerOptions, compilerHost)

    console.log(`Source files count: ${program.getSourceFiles().length}`)

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

    const defaultPlugins = createPlugins(sourceFileRoot, configuration)

    const plugins = [
        // it is important to handle comments at first
        new CommentsPlugin(),

        ...customPlugins,
        ...defaultPlugins
    ]

    for (const plugin of plugins) {
        plugin.setup(context)
    }

    const fileStructure = prepareFileStructure(sourceFileRoot, program, configuration)

    fileStructure
        .flatMap(it => it.nodes)
        .forEach(node => {
            traverse(node, node => {
                for (const plugin of plugins) {
                    plugin.traverse(node, context)
                }
            })
        })

    const render = createRender(context, plugins)

    fileStructure
        .filter(item => {
            return normalizedIgnore.every(pattern => !minimatch(item.sourceFileName, pattern))
        })
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
                convertedBody,
                configuration,
            )

            fs.mkdirSync(path.dirname(targetFileName), {recursive: true})
            fs.writeFileSync(targetFileName, targetFile)
        })

    for (const plugin of plugins) {
        const generated = plugin.generate(context)

        for (const [fileName, content] of Object.entries(generated)) {
            if (fs.existsSync(fileName)) {
                fs.appendFileSync(fileName, content)
            } else {
                console.log(`Generated file: ${fileName}`)

                fs.writeFileSync(fileName, content)
            }
        }
    }
}
