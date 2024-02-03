import {PartialConfiguration} from "./configuration/configuration.js";
import {defaultizeConfiguration} from "./configuration/defaultizeConfiguration.js";
import {glob} from "glob";
import ts, {CompilerOptions} from "typescript";
import path from "node:path";
import fs from "node:fs/promises";
import {createContext} from "./converter/context.js";
import {ConverterPlugin, createSimplePlugin, SimpleConverterPlugin} from "./converter/plugin.js";
import {createPlugins} from "./defaultPlugins.js";
import {CommentsPlugin} from "./converter/plugins/CommentsPlugin.js";
import {prepareStructure} from "./structure/prepareStructure.js";
import {traverse} from "./utils/traverse.js";
import {minimatch} from "minimatch";
import {createRender} from "./converter/render.js";
import {NameResolver} from "./converter/nameResolver.js";
import {AnnotationPlugin} from "./converter/plugins/AnnotationPlugin.js";
import {InheritanceModifier} from "./converter/inheritanceModifier.js";
import {Annotation} from "./converter/annotation.js";
import {collectNamespaceInfo} from "./structure/namespace/collectNamespaceInfo.js";
import {collectSourceFileInfo} from "./structure/sourceFile/collectSourceFileInfo.js";
import {packageToOutputFileName} from "./structure/package/packageToFileName.js";
import {toModuleName} from "./utils/path.js";
import {DerivedFile, GeneratedFile, isDerivedFile} from "./converter/generated.js";
import {resolveConflicts, TargetFile} from "./structure/resolveConflicts.js";
import {createSimpleInjection, Injection, SimpleInjection} from "./converter/injection.js";
import {collectImportInfo} from "./structure/import/collectImportInfo.js";

async function loadExtensions<T>(
    name: string,
    patterns: string[],
    cwd: string,
    loader: (extension: unknown) => T = extension => extension as T
): Promise<T[]> {
    const fileNames = (await Promise.all(patterns.map(pattern => glob(pattern, {
        cwd,
        absolute: true,
        posix: true,
    })))).flat()

    const extensions: T[] = []

    for (const fileName of fileNames) {
        console.log(`${name} file: ${fileName}`)

        const extensionModule: { default: unknown } = await import(toModuleName(fileName))
        const extension = extensionModule.default

        extensions.push(loader(extension))
    }

    return extensions
}

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

                console.log(`Files have the same name but different casing:\n${fileName}\n${otherFileName}`)
            }
        }
    }

    if (isConflict) {
        throw new Error("There are conflicts in file names")
    }
}

export async function generate(partialConfiguration: PartialConfiguration) {
    const configuration = await defaultizeConfiguration(partialConfiguration)

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

    const customInjections = await loadExtensions<Injection>(
        "Injection",
        injections,
        cwd,
        injection => {
            if (typeof injection === "function") {
                return createSimpleInjection(injection as SimpleInjection)
            } else {
                return injection as Injection
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

    const importInfo = collectImportInfo(sourceFiles, configuration)

    const namespaceInfo = collectNamespaceInfo(sourceFiles, importInfo, configuration)
    const sourceFileInfo = collectSourceFileInfo(sourceFiles, importInfo, configuration)

    const structure = prepareStructure(
        [
            ...namespaceInfo.filter(it => it.strategy === "package"),
            ...sourceFileInfo,
        ],
        configuration,
    )

    const defaultPlugins = createPlugins(
        configuration,
        customInjections,
        customNameResolvers,
        customInheritanceModifiers,
        program,
        namespaceInfo,
        importInfo,
    )

    const converterPlugins = [
        // it is important to handle comments and annotations at first
        new CommentsPlugin(),
        new AnnotationPlugin(customAnnotations),

        ...customInjections,
        ...customPlugins,
        ...defaultPlugins
    ]

    const context = createContext()

    for (const plugin of converterPlugins) {
        plugin.setup(context)
    }

    structure
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

        const body = item.statements
            .map(node => render(node))
            .join("\n\n")

        if (ignoreOutput.every(pattern => !minimatch(targetFileName, pattern))) {
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
        const generated = plugin.generate(context)

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

            if (ignoreOutput.every(pattern => !minimatch(generatedFileName, pattern))) {
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
