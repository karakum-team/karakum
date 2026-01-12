package io.github.sgrishchenko.karakum.configuration

import io.github.sgrishchenko.karakum.structure.prepareLibraryName
import io.github.sgrishchenko.karakum.util.*
import js.array.ReadonlyArray
import js.import.import
import js.objects.recordOf
import node.module.findPackageJSON
import node.path.path
import node.process.process
import typescript.CompilerOptions

const val defaultDisclaimer = "// Automatically generated - do not modify!"

private fun <T> normalizeOption(
    patterns: Many<T>?,
    defaultValue: ReadonlyArray<T> = emptyArray(),
): ReadonlyArray<T> {
    return normalizeOption(patterns) { defaultValue }
}

private fun <T> normalizeOption(
    patterns: Many<T>?,
    defaultValue: () -> ReadonlyArray<T>,
): ReadonlyArray<T> {
    return patterns?.toArray() ?: defaultValue()
}

private fun resolveDefaultInputRoot(inputFileNames: ReadonlyArray<String>): String {
    if (inputFileNames.size == 1) {
        return path.dirname(inputFileNames[0]) + path.posix.sep
    }

    val inputPathChunks = inputFileNames
        .map { it.split(path.posix.sep).toTypedArray() }
        .toTypedArray()

    // TODO: handle non-default relative root (UNC prefix)
    return commonPrefix(sources = inputPathChunks).joinToString(path.posix.sep, postfix = path.posix.sep)
}

suspend fun defaultizeConfiguration(configuration: PartialConfiguration): Configuration {
    val cwd = toPosix(configuration.cwd ?: process.cwd())

    val input = normalizeOption(configuration.input)

    val ignoreInput = normalizeOption(configuration.ignoreInput)
    val ignoreOutput = normalizeOption(configuration.ignoreOutput)

    val inputResolutionStrategy = configuration.inputResolutionStrategy ?: InputResolutionStrategy.node

    val libraryName = configuration.libraryName ?: ""
    val libraryLocation = if (inputResolutionStrategy == InputResolutionStrategy.node) {
        val packageJSON = requireNotNull(findPackageJSON(libraryName, import.meta.url))
        toPosix(path.dirname(packageJSON))
    } else null

    val inputCwd = when (inputResolutionStrategy) {
        InputResolutionStrategy.node -> requireNotNull(libraryLocation)
        InputResolutionStrategy.plain -> cwd
    }
    val inputFileNames = glob(input, inputCwd, ignoreInput)

    val absoluteOutput = toAbsolute(configuration.output ?: process.cwd(), cwd)

    var output = absoluteOutput
    var outputFileName: String? = null

    if (output.endsWith(".kt")) {
        output = path.dirname(absoluteOutput)
        outputFileName = absoluteOutput
    }

    val plugins = normalizeOption(configuration.plugins)
    val injections = normalizeOption(configuration.injections)
    val annotations = normalizeOption(configuration.annotations)
    val nameResolvers = normalizeOption(configuration.nameResolvers)
    val inheritanceModifiers = normalizeOption(configuration.inheritanceModifiers)
    val varianceModifiers = normalizeOption(configuration.varianceModifiers)

    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
    val compilerOptions = configuration.compilerOptions ?: Any() as CompilerOptions

    return Configuration(
        inputRoots = normalizeOption(configuration.inputRoots) {
            if (inputResolutionStrategy == InputResolutionStrategy.node) {
                arrayOf(requireNotNull(libraryLocation))
            } else {
                arrayOf(resolveDefaultInputRoot(inputFileNames))
            }
        },
        inputResolutionStrategy = inputResolutionStrategy,
        inputFileNames = inputFileNames,

        input = input,
        output = output,

        outputFileName = outputFileName,

        ignoreInput = ignoreInput,
        ignoreOutput = ignoreOutput,

        libraryName = libraryName,
        libraryNameOutputPrefix = configuration.libraryNameOutputPrefix ?: true,

        plugins = plugins,

        injections = injections,

        annotations = annotations,

        nameResolvers = nameResolvers,

        inheritanceModifiers = inheritanceModifiers,

        varianceModifiers = varianceModifiers,

        moduleNameMapper = configuration.moduleNameMapper ?: recordOf("^.*$" to prepareLibraryName(libraryName)),
        packageNameMapper = configuration.packageNameMapper ?: recordOf(),

        importInjector = configuration.importInjector ?: recordOf(),
        importMapper = configuration.importMapper ?: recordOf(),

        namespaceStrategy = configuration.namespaceStrategy ?: recordOf(),

        conflictResolutionStrategy = configuration.conflictResolutionStrategy ?: recordOf(),

        compilerOptions = compilerOptions,

        verbose = configuration.verbose ?: false,
        disclaimer = configuration.disclaimer ?: defaultDisclaimer,
        cwd = cwd,
        inputCwd = inputCwd,
    )
}
