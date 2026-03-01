package io.github.sgrishchenko.karakum.configuration

import io.github.sgrishchenko.karakum.extension.Annotation
import io.github.sgrishchenko.karakum.extension.InheritanceModifier
import io.github.sgrishchenko.karakum.extension.Injection
import io.github.sgrishchenko.karakum.extension.MutabilityModifier
import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.Plugin
import io.github.sgrishchenko.karakum.extension.VarianceModifier
import io.github.sgrishchenko.karakum.util.Rule
import io.github.sgrishchenko.karakum.util.manyOf
import js.objects.recordOf
import typescript.CompilerOptions
import kotlin.String
import kotlin.collections.List

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

private fun <T> List<T>.toMany() = manyOf(values = toTypedArray())

private fun <K : Any, V> Map<K, V>.toRecord() = recordOf(pairs = toList().toTypedArray())

internal fun buildConfiguration(
    partialConfiguration: PartialConfiguration,
    block: MutableConfiguration.() -> Unit,
): PartialConfiguration {
    val scope = MutableConfigurationImpl().apply(block)

    return PartialConfiguration(
        inputRoots = scope.inputRoots?.toMany()
            ?: partialConfiguration.inputRoots,
        inputResolutionStrategy = scope.inputResolutionStrategy
            ?: partialConfiguration.inputResolutionStrategy,

        input = scope.input?.toMany()
            ?: partialConfiguration.input,
        output = scope.output
            ?: partialConfiguration.output,

        ignoreInput = scope.ignoreInput?.toMany()
            ?: partialConfiguration.ignoreInput,
        ignoreOutput = scope.ignoreOutput?.toMany()
            ?: partialConfiguration.ignoreOutput,

        libraryName = scope.libraryName
            ?: partialConfiguration.libraryName,
        libraryNameOutputPrefix = scope.libraryNameOutputPrefix
            ?: partialConfiguration.libraryNameOutputPrefix,
        isolatedOutputPackage = scope.isolatedOutputPackage
            ?: partialConfiguration.isolatedOutputPackage,

        plugins = scope.plugins?.toMany()
            ?: partialConfiguration.plugins,

        injections = scope.injections?.toMany()
            ?: partialConfiguration.injections,

        annotations = scope.annotations?.toMany()
            ?: partialConfiguration.annotations,

        nameResolvers = scope.nameResolvers?.toMany()
            ?: partialConfiguration.nameResolvers,

        inheritanceModifiers = scope.inheritanceModifiers?.toMany()
            ?: partialConfiguration.inheritanceModifiers,

        mutabilityModifiers = scope.mutabilityModifiers?.toMany()
            ?: partialConfiguration.mutabilityModifiers,

        varianceModifiers = scope.varianceModifiers?.toMany()
            ?: partialConfiguration.varianceModifiers,

        moduleNameMapper = scope.moduleNameMapper?.toRecord()
            ?: partialConfiguration.moduleNameMapper,

        packageNameMapper = scope.packageNameMapper?.toRecord()
            ?: partialConfiguration.packageNameMapper,

        importInjector = scope.importInjector
            ?.mapValues { (_, value) -> value.toTypedArray() }
            ?.toRecord()
            ?: partialConfiguration.importInjector,

        importMapper = scope.importMapper?.toRecord()
            ?: partialConfiguration.importMapper,

        namespaceStrategy = scope.namespaceStrategy?.toRecord()
            ?: partialConfiguration.namespaceStrategy,

        conflictResolutionStrategy = scope.conflictResolutionStrategy?.toRecord()
            ?: partialConfiguration.conflictResolutionStrategy,

        compilerOptions = scope.compilerOptions
            ?: partialConfiguration.compilerOptions,

        disclaimer = scope.disclaimer
            ?: partialConfiguration.disclaimer,
        verbose = scope.verbose
            ?: partialConfiguration.verbose,
        cwd = scope.cwd
            ?: partialConfiguration.cwd,
    )
}
