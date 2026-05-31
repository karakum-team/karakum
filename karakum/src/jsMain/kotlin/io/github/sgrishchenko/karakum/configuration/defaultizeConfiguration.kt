package io.github.sgrishchenko.karakum.configuration

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.structure.prepareLibraryName
import io.github.sgrishchenko.karakum.util.*
import js.import.import
import node.module.findPackageJSON
import node.path.path
import node.process.process
import typescript.CompilerOptions

const val defaultDisclaimer = "// Automatically generated - do not modify!"

private fun resolveDefaultInputRoot(inputFileNames: List<String>): String {
    if (inputFileNames.size == 1) {
        return path.dirname(inputFileNames[0]) + path.posix.sep
    }

    val inputPathChunks = inputFileNames
        .map { it.split(path.posix.sep).toTypedArray() }
        .toTypedArray()

    // TODO: handle non-default relative root (UNC prefix)
    return commonPrefix(sources = inputPathChunks).joinToString(path.posix.sep, postfix = path.posix.sep)
}

private class ConfigurationImpl(
    override val inputRoots: List<String>,
    override val inputResolutionStrategy: InputResolutionStrategy,

    override val input: List<String>,
    override val inputFileNames: List<String>,
    override val output: String,
    override val outputFileName: String?,

    override val ignoreInput: List<String>,
    override val ignoreOutput: List<String>,

    override val libraryName: String,
    override val libraryNameOutputPrefix: Boolean,
    override val isolatedOutputPackage: Boolean,

    override val plugins: List<Plugin>,

    override val injections: List<Injection>,

    override val annotations: List<Annotation>,

    override val nameResolvers: List<NameResolver>,

    override val inheritanceModifiers: List<InheritanceModifier>,

    override val mutabilityModifiers: List<MutabilityModifier>,

    override val varianceModifiers: List<VarianceModifier>,

    override val moduleNameMapper: Map<String, String>,
    override val packageNameMapper: Map<String, String>,

    override val importInjector: Map<String, List<String>>,
    override val importMapper: Map<String, Rule>,

    override val namespaceStrategy: Map<String, NamespaceStrategy>,

    override val conflictResolutionStrategy: Map<String, ConflictResolutionStrategy>,

    override val compilerOptions: CompilerOptions,

    override val disclaimer: String,
    override val verbose: Boolean,
    override val cwd: String,
    override val inputCwd: String,
) : Configuration

internal suspend fun defaultizeConfiguration(configuration: MutableConfiguration): Configuration {
    val cwd = toPosix(configuration.cwd ?: process.cwd())

    val input = configuration.input ?: emptyList()

    val ignoreInput = configuration.ignoreInput ?: emptyList()
    val ignoreOutput = configuration.ignoreOutput ?: emptyList()

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

    val plugins = configuration.plugins ?: emptyList()
    val injections = configuration.injections ?: emptyList()
    val annotations = configuration.annotations ?: emptyList()
    val nameResolvers = configuration.nameResolvers ?: emptyList()
    val inheritanceModifiers = configuration.inheritanceModifiers ?: emptyList()
    val mutabilityModifiers = configuration.mutabilityModifiers ?: emptyList()
    val varianceModifiers = configuration.varianceModifiers ?: emptyList()

    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
    val compilerOptions = configuration.compilerOptions ?: Any() as CompilerOptions

    return ConfigurationImpl(
        inputRoots = configuration.inputRoots
            ?: if (inputResolutionStrategy == InputResolutionStrategy.node) {
                listOf(requireNotNull(libraryLocation))
            } else {
                listOf(resolveDefaultInputRoot(inputFileNames))
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
        isolatedOutputPackage = configuration.isolatedOutputPackage ?: false,

        plugins = plugins,

        injections = injections,

        annotations = annotations,

        nameResolvers = nameResolvers,

        inheritanceModifiers = inheritanceModifiers,

        mutabilityModifiers = mutabilityModifiers,

        varianceModifiers = varianceModifiers,

        moduleNameMapper = configuration.moduleNameMapper ?: mapOf("^.*$" to prepareLibraryName(libraryName)),
        packageNameMapper = configuration.packageNameMapper ?: emptyMap(),

        importInjector = configuration.importInjector ?: emptyMap(),
        importMapper = configuration.importMapper ?: emptyMap(),

        namespaceStrategy = configuration.namespaceStrategy ?: emptyMap(),

        conflictResolutionStrategy = configuration.conflictResolutionStrategy ?: emptyMap(),

        compilerOptions = compilerOptions,

        verbose = configuration.verbose ?: false,
        disclaimer = configuration.disclaimer ?: defaultDisclaimer,
        cwd = cwd,
        inputCwd = inputCwd,
    )
}
