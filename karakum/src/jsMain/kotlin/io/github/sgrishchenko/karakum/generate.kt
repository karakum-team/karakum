package io.github.sgrishchenko.karakum

import io.github.sgrishchenko.karakum.configuration.*
import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.extension.plugins.AnnotationPlugin
import io.github.sgrishchenko.karakum.extension.plugins.CommentPlugin
import io.github.sgrishchenko.karakum.structure.TargetFile
import io.github.sgrishchenko.karakum.structure.createTopLevelMatcher
import io.github.sgrishchenko.karakum.structure.import.collectImportInfo
import io.github.sgrishchenko.karakum.structure.namespace.collectNamespaceInfo
import io.github.sgrishchenko.karakum.structure.`package`.packageToOutputFileName
import io.github.sgrishchenko.karakum.structure.prepareStructure
import io.github.sgrishchenko.karakum.structure.resolveConflicts
import io.github.sgrishchenko.karakum.structure.sourceFile.collectSourceFileInfo
import io.github.sgrishchenko.karakum.util.traverse
import js.array.ReadonlyArray
import js.coroutines.promise
import js.objects.Object
import js.promise.Promise
import kotlinx.coroutines.CoroutineScope
import kotlinx.js.JsPlainObject
import node.fs.*
import node.path.path
import typescript.CompilerOptions
import typescript.asArray
import typescript.createCompilerHost
import typescript.createProgram
import web.abort.Abortable
import web.abort.asCoroutineScope
import kotlin.coroutines.EmptyCoroutineContext

private fun checkCasing(fileNames: ReadonlyArray<String>) {
    var isConflict = false

    for (i in 0..<(fileNames.size - 1)) {
        for (j in (i + 1)..<fileNames.size) {
            val fileName = fileNames[i]
            val otherFileName = fileNames[j]

            if (
                fileName != otherFileName
                && fileName.equals(otherFileName, ignoreCase = true)
            ) {
                isConflict = true

                console.error("Files have the same name but different casing:\n${fileName}\n${otherFileName}")
            }
        }
    }

    if (isConflict) {
        error("There are conflicts in file names")
    }
}

private suspend fun generate(mutableConfiguration: MutableConfiguration) {
    val configuration = defaultizeConfiguration(mutableConfiguration)

    val inputRoots = configuration.inputRoots
    val inputFileNames = configuration.inputFileNames
    val input = configuration.input
    val output = configuration.output
    val outputFileName = configuration.outputFileName
    val ignoreInput = configuration.ignoreInput
    val ignoreOutput = configuration.ignoreOutput
    val plugins = configuration.plugins
    val injections = configuration.injections
    val annotations = configuration.annotations
    val nameResolvers = configuration.nameResolvers
    val inheritanceModifiers = configuration.inheritanceModifiers
    val mutabilityModifiers = configuration.mutabilityModifiers
    val varianceModifiers = configuration.varianceModifiers
    val compilerOptions = configuration.compilerOptions
    val inputCwd = configuration.inputCwd

    val preparedCompilerOptions = Object.assign(
        CompilerOptions(
            lib = arrayOf(
                "lib.esnext.d.ts",
                "lib.dom.d.ts",
            )
        ),
        compilerOptions
    )

    val compilerHost = createCompilerHost(preparedCompilerOptions, setParentNodes = true)

    val program = createProgram(inputFileNames.toTypedArray(), preparedCompilerOptions, compilerHost)

    for (inputRoot in inputRoots) {
        console.log("Source files root: $inputRoot")
    }

    if (outputFileName != null) {
        rm(outputFileName, RmOptions(
            recursive = true,
            force = true,
        ))
    } else {
        rm(output, RmOptions(
            recursive = true,
            force = true,
        ))
    }

    mkdir(output, MakeDirectoryOptions(recursive = true))

    val sourceFiles = program.getSourceFiles()
        .filter { sourceFile ->
            val relativeFileName = path.relative(inputCwd, sourceFile.fileName)
            input.any { pattern ->
                if (path.isAbsolute(pattern)) {
                    path.matchesGlob(sourceFile.fileName, pattern)
                } else {
                    path.matchesGlob(relativeFileName, pattern)
                }
            }
        }
        .filter { sourceFile ->
            val relativeFileName = path.relative(inputCwd, sourceFile.fileName)
            ignoreInput.all { pattern ->
                if (path.isAbsolute(pattern)) {
                    !path.matchesGlob(sourceFile.fileName, pattern)
                } else {
                    !path.matchesGlob(relativeFileName, pattern)
                }
            }
        }
        .toTypedArray()

    console.log("Source files count: ${sourceFiles.size}")

    val importInfo = collectImportInfo(sourceFiles, configuration)

    val namespaceInfo = collectNamespaceInfo(sourceFiles, importInfo, configuration)
    val sourceFileInfo = collectSourceFileInfo(sourceFiles, importInfo, configuration)

    val topLevelMatcher = createTopLevelMatcher(importInfo, namespaceInfo)

    val namespaceStructure = prepareStructure(
        namespaceInfo
            .filter { it.strategy == NamespaceStrategy.`package` }
            .toTypedArray(),
        topLevelMatcher,
        configuration,
    )
    val sourceFileStructure = prepareStructure(
        sourceFileInfo,
        topLevelMatcher,
        configuration,
    )
    val structure = namespaceStructure + sourceFileStructure

    val defaultPlugins = createPlugins(
        configuration,
        injections,
        nameResolvers,
        inheritanceModifiers,
        mutabilityModifiers,
        varianceModifiers,
        program,
        namespaceInfo,
        importInfo,
    )

    val converterPlugins = listOf(
        // it is important to handle comments and annotations at first
        CommentPlugin(),
        AnnotationPlugin(annotations),
    )
        .plus(injections)
        .plus(plugins)
        .plus(defaultPlugins)

    val context = Context()

    for (plugin in converterPlugins) {
        plugin.setup(context)
    }

    sourceFiles
        .flatMap { it.statements.asArray().asIterable() }
        .forEach { statement ->
            traverse(statement) { node ->
                for (plugin in converterPlugins) {
                    plugin.traverse(node, context)
                }
            }
        }

    val render = createRender(context, converterPlugins.toTypedArray())

    val targetFiles = mutableListOf<TargetFile>()

    val structureMeta = mutableSetOf<String>()

    for (item in structure) {
        structureMeta.add("${item.meta.type}: ${item.meta.name}")

        val currentOutputFileName = packageToOutputFileName(
            item.`package`,
            item.fileName,
            configuration
        )

        val targetFileName = path.resolve(output, currentOutputFileName)

        val body = item.nodes
            .map { render(it) }
            .joinToString(separator = "\n\n")

        if (ignoreOutput.all { pattern -> !path.matchesGlob(targetFileName, pattern) }) {
            targetFiles += TargetFile(
                fileName = item.fileName,
                `package` = item.`package`,
                moduleName = item.moduleName,
                qualifier = item.qualifier,
                hasRuntime = item.hasRuntime,
                imports = item.imports,
                body = body,
            )
        }
    }

    for (meta in structureMeta) {
        console.log(meta)
    }

    val derivedFiles = mutableListOf<DerivedFile>()
    val generatedFiles = mutableListOf<GeneratedFile>()

    for (plugin in converterPlugins) {
        val generated = plugin.generate(context, render)

        for (generatedFile in generated) {
            var generatedFileName = generatedFile.fileName

            if (isDerivedFile(generatedFile)) {
                val fileName = packageToOutputFileName(
                    generatedFile.`package`,
                    generatedFile.fileName,
                    configuration,
                )

                generatedFileName = path.resolve(output, fileName)
            }

            if (ignoreOutput.all { pattern -> !path.matchesGlob(generatedFileName, pattern) }) {
                if (isDerivedFile(generatedFile)) {
                    derivedFiles += generatedFile
                } else {
                    generatedFiles += generatedFile
                }
            }
        }
    }

    val resultFiles = resolveConflicts(
        targetFiles.toTypedArray(),
        derivedFiles.toTypedArray(),
        generatedFiles.toTypedArray(),
        configuration,
    )

    checkCasing(resultFiles.map { it.fileName }.toTypedArray())

    for (resultFile in resultFiles) {
        mkdir(path.dirname(resultFile.fileName), MkdirOptions(recursive = true))
        writeFile(resultFile.fileName, resultFile.body)
    }
}

suspend fun generate(
    args: ReadonlyArray<String>,
    block: MutableConfiguration.() -> Unit,
) = generate(parseArgs(args), block)

suspend fun generate(
    partialConfiguration: PartialConfiguration = PartialConfiguration(),
    block: MutableConfiguration.() -> Unit,
) {
    generate(buildConfiguration(partialConfiguration, block))
}

@JsExport
@JsPlainObject
external interface GenerateOptions: PartialConfiguration, Abortable

@JsExport
@JsName("generate")
fun generateAsync(options: GenerateOptions): Promise<Unit> =
    options.asCoroutineScope()
        .promise { generate(options) {} }
