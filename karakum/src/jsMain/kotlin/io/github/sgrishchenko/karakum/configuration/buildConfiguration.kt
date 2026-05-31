package io.github.sgrishchenko.karakum.configuration

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.util.Rule
import io.github.sgrishchenko.karakum.util.toList
import js.array.component1
import js.array.component2
import js.objects.Object
import js.objects.ReadonlyRecord
import typescript.CompilerOptions

private class MutableConfigurationImpl(
    override var inputRoots: List<String>? = null,
    override var inputResolutionStrategy: InputResolutionStrategy? = null,

    override var input: List<String>? = null,
    override var output: String? = null,

    override var ignoreInput: List<String>? = null,
    override var ignoreOutput: List<String>? = null,

    override var libraryName: String? = null,
    override var libraryNameOutputPrefix: Boolean? = null,
    override var isolatedOutputPackage: Boolean? = null,

    override var plugins: List<Plugin>? = null,

    override var injections: List<Injection>? = null,

    override var annotations: List<Annotation>? = null,

    override var nameResolvers: List<NameResolver>? = null,

    override var inheritanceModifiers: List<InheritanceModifier>? = null,

    override var mutabilityModifiers: List<MutabilityModifier>? = null,

    override var varianceModifiers: List<VarianceModifier>? = null,

    override var moduleNameMapper: Map<String, String>? = null,

    override var packageNameMapper: Map<String, String>? = null,

    override var importInjector: Map<String, List<String>>? = null,

    override var importMapper: Map<String, Rule>? = null,

    override var namespaceStrategy: Map<String, NamespaceStrategy>? = null,

    override var conflictResolutionStrategy: Map<String, ConflictResolutionStrategy>? = null,

    override var compilerOptions: CompilerOptions? = null,

    override var disclaimer: String? = null,
    override var verbose: Boolean? = null,
    override var cwd: String? = null,
) : MutableConfiguration

private fun <V> ReadonlyRecord<String, V>.toMap(): Map<String, V> =
    Object.entries(this).associate { (key, value) -> key to value }

internal fun buildConfiguration(
    partialConfiguration: PartialConfiguration,
    block: MutableConfiguration.() -> Unit,
): MutableConfiguration {
    return MutableConfigurationImpl()
        .apply(block)
        .apply {
            inputRoots = inputRoots
                ?: partialConfiguration.inputRoots?.toList()
            inputResolutionStrategy = inputResolutionStrategy
                ?: partialConfiguration.inputResolutionStrategy

            input = input
                ?: partialConfiguration.input?.toList()
            output = output
                ?: partialConfiguration.output

            ignoreInput = ignoreInput
                ?: partialConfiguration.ignoreInput?.toList()
            ignoreOutput = ignoreOutput
                ?: partialConfiguration.ignoreOutput?.toList()

            libraryName = libraryName
                ?: partialConfiguration.libraryName
            libraryNameOutputPrefix = libraryNameOutputPrefix
                ?: partialConfiguration.libraryNameOutputPrefix
            isolatedOutputPackage = isolatedOutputPackage
                ?: partialConfiguration.isolatedOutputPackage

            plugins = plugins
                ?: partialConfiguration.plugins?.toList()?.map { it.toPlugin() }

            injections = injections
                ?: partialConfiguration.injections?.toList()?.map { it.toInjection() }

            annotations = annotations
                ?: partialConfiguration.annotations?.toList()?.map { it.toExtension() }

            nameResolvers = nameResolvers
                ?: partialConfiguration.nameResolvers?.toList()?.map { it.toExtension() }

            inheritanceModifiers = inheritanceModifiers
                ?: partialConfiguration.inheritanceModifiers?.toList()?.map { it.toExtension() }

            mutabilityModifiers = mutabilityModifiers
                ?: partialConfiguration.mutabilityModifiers?.toList()?.map { it.toExtension() }

            varianceModifiers = varianceModifiers
                ?: partialConfiguration.varianceModifiers?.toList()?.map { it.toExtension() }

            moduleNameMapper = moduleNameMapper
                ?: partialConfiguration.moduleNameMapper?.toMap()

            packageNameMapper = packageNameMapper
                ?: partialConfiguration.packageNameMapper?.toMap()

            importInjector = importInjector
                ?: partialConfiguration.importInjector
                    ?.toMap()
                    ?.mapValues { (_, value) -> value.toList() }

            importMapper = importMapper
                ?: partialConfiguration.importMapper?.toMap()

            namespaceStrategy = namespaceStrategy
                ?: partialConfiguration.namespaceStrategy?.toMap()

            conflictResolutionStrategy = conflictResolutionStrategy
                ?: partialConfiguration.conflictResolutionStrategy?.toMap()

            compilerOptions = compilerOptions
                ?: partialConfiguration.compilerOptions

            disclaimer = disclaimer
                ?: partialConfiguration.disclaimer
            verbose = verbose
                ?: partialConfiguration.verbose
            cwd = cwd
                ?: partialConfiguration.cwd
        }
}
