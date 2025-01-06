import {Configuration} from "./configuration/configuration.js";
import ts, {CompilerOptions} from "typescript";
import path, {matchesGlob} from "node:path";
import fs from "node:fs/promises";
import {createContext} from "./converter/context.js";
import {createPlugins} from "./defaultPlugins.js";
import {CommentPlugin} from "./converter/plugins/CommentPlugin.js";
import {prepareStructure} from "./structure/prepareStructure.js";
import {traverse} from "./utils/traverse.js";
import {createRender} from "./converter/render.js";
import {AnnotationPlugin} from "./converter/plugins/AnnotationPlugin.js";
import {collectNamespaceInfo} from "./structure/namespace/collectNamespaceInfo.js";
import {collectSourceFileInfo} from "./structure/sourceFile/collectSourceFileInfo.js";
import {packageToOutputFileName} from "./structure/package/packageToFileName.js";
import {DerivedFile, GeneratedFile, isDerivedFile} from "./converter/generated.js";
import {resolveConflicts, TargetFile} from "./structure/resolveConflicts.js";
import {collectImportInfo} from "./structure/import/collectImportInfo.js";

function checkCasing(fileNames: string[]) {
    let isConflict = false

    for (let i = 0; i < fileNames.length - 1; i++) {
        for (let j = i + 1; j < fileNames.length; j++) {
            const fileName = fileNames[i]
            const otherFileName = fileNames[j]

            if (
                fileName !== otherFileName
                && fileName.toLowerCase() === otherFileName.toLowerCase()
            ) {
                isConflict = true

                console.error(`Files have the same name but different casing:\n${fileName}\n${otherFileName}`)
            }
        }
    }

    if (isConflict) {
        throw new Error("There are conflicts in file names")
    }
}

export async function generate(configuration: Configuration) {
    const {
        inputRoots,
        inputFileNames,
        input,
        output,
        outputFileName,
        ignoreInput,
        ignoreOutput,
        plugins,
        injections,
        annotations,
        nameResolvers,
        inheritanceModifiers,
        varianceModifiers,
        compilerOptions,
        cwd,
    } = configuration

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
        await fs.rm(outputFileName, {recursive: true, force: true})
    } else {
        await fs.rm(output, {recursive: true, force: true})
    }
    await fs.mkdir(output, {recursive: true})

    const sourceFiles = program.getSourceFiles()
        .filter(sourceFile => {
            const relativeFileName = path.relative(cwd, sourceFile.fileName)
            return input.some(pattern => {
                if (path.isAbsolute(pattern)) {
                    return matchesGlob(sourceFile.fileName, pattern);
                } else {
                    return matchesGlob(relativeFileName, pattern);
                }
            })
        })
        .filter(sourceFile => {
            const relativeFileName = path.relative(cwd, sourceFile.fileName)
            return ignoreInput.every(pattern => {
                if (path.isAbsolute(pattern)) {
                    return !matchesGlob(sourceFile.fileName, pattern);
                } else {
                    return !matchesGlob(relativeFileName, pattern);
                }
            })
        })

    console.log(`Source files count: ${sourceFiles.length}`)

    const importInfo = collectImportInfo(sourceFiles, configuration)

    const namespaceInfo = collectNamespaceInfo(sourceFiles, importInfo, configuration)
    const sourceFileInfo = collectSourceFileInfo(sourceFiles, importInfo, configuration)

    const namespaceStructure = prepareStructure(
        namespaceInfo.filter(it => it.strategy === "package"),
        configuration,
    )
    const sourceFileStructure = prepareStructure(
        sourceFileInfo,
        configuration,
    )
    const structure = [
        ...namespaceStructure,
        ...sourceFileStructure,
    ]

    const defaultPlugins = createPlugins(
        configuration,
        injections,
        nameResolvers,
        inheritanceModifiers,
        varianceModifiers,
        program,
        namespaceInfo,
        importInfo,
    )

    const converterPlugins = [
        // it is important to handle comments and annotations at first
        new CommentPlugin(),
        new AnnotationPlugin(annotations),

        ...injections,
        ...plugins,
        ...defaultPlugins
    ]

    const context = createContext()

    for (const plugin of converterPlugins) {
        plugin.setup(context)
    }

    sourceFiles
        .flatMap(it => it.statements)
        .forEach(statement => {
            traverse(statement, node => {
                for (const plugin of converterPlugins) {
                    plugin.traverse(node, context)
                }
            })
        })

    const render = createRender(context, converterPlugins)

    const targetFiles: TargetFile[] = []

    const structureMeta = new Set<string>()

    for (const item of structure) {
        structureMeta.add(`${item.meta.type}: ${item.meta.name}`)

        const outputFileName = packageToOutputFileName(
            item.package,
            item.fileName,
            configuration
        )

        const targetFileName = path.resolve(output, outputFileName)

        const body = item.nodes
            .map(node => render(node))
            .join("\n\n")

        if (ignoreOutput.every(pattern => !matchesGlob(targetFileName, pattern))) {
            targetFiles.push({
                ...item,
                body,
            })
        }
    }

    for (const meta of structureMeta) {
        console.log(meta)
    }

    const derivedFiles: DerivedFile[] = []
    const generatedFiles: GeneratedFile[] = []

    for (const plugin of converterPlugins) {
        const generated = plugin.generate(context, render)

        for (const generatedFile of generated) {
            let generatedFileName = generatedFile.fileName

            if (isDerivedFile(generatedFile)) {
                const outputFileName = packageToOutputFileName(
                    generatedFile.package,
                    generatedFile.fileName,
                    configuration,
                )

                generatedFileName = path.resolve(output, outputFileName)
            }

            if (ignoreOutput.every(pattern => !matchesGlob(generatedFileName, pattern))) {
                if (isDerivedFile(generatedFile)) {
                    derivedFiles.push(generatedFile)
                } else {
                    generatedFiles.push(generatedFile)
                }
            }
        }
    }

    const resultFiles = resolveConflicts(
        targetFiles,
        derivedFiles,
        generatedFiles,
        configuration,
    )

    checkCasing(resultFiles.map(it => it.fileName))

    for (const resultFile of resultFiles) {
        await fs.mkdir(path.dirname(resultFile.fileName), {recursive: true})
        await fs.writeFile(resultFile.fileName, resultFile.body)
    }
}
