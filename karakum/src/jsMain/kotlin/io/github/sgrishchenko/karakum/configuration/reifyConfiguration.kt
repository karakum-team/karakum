package io.github.sgrishchenko.karakum.configuration

import io.github.sgrishchenko.karakum.util.Many
import io.github.sgrishchenko.karakum.util.Rule
import io.github.sgrishchenko.karakum.util.manyOf
import io.github.sgrishchenko.karakum.util.toArray
import io.github.sgrishchenko.karakum.util.toPosix
import js.array.ReadonlyArray
import js.objects.ReadonlyRecord
import node.process.process
import typescript.CompilerOptions

private fun normalizeOption(
    patterns: Many<Any>?,
    defaultValue: ReadonlyArray<String> = emptyArray(),
): ReadonlyArray<String> {
    return patterns?.toArray()
        ?.map { it as String }
        ?.toTypedArray()
        ?: defaultValue
}

private fun createDefaultExtensionPatterns(name: String) = arrayOf(
    "karakum/$name/*.js",
    "karakum/$name/*.ts",
)

suspend fun reifyConfiguration(configuration: SchemaConfiguration): PartialConfiguration {
    val cwd = toPosix(configuration.cwd ?: process.cwd())

    val extensions = loadExtensions(ExtensionConfiguration(
        plugins = normalizeOption(configuration.plugins, createDefaultExtensionPatterns("plugins")),
        injections = normalizeOption(configuration.injections, createDefaultExtensionPatterns("injections")),
        annotations = normalizeOption(configuration.annotations, createDefaultExtensionPatterns("annotations")),
        nameResolvers = normalizeOption(configuration.nameResolvers, createDefaultExtensionPatterns("nameResolvers")),
        inheritanceModifiers = normalizeOption(configuration.inheritanceModifiers, createDefaultExtensionPatterns("inheritanceModifiers")),
        mutabilityModifiers = normalizeOption(configuration.mutabilityModifiers, createDefaultExtensionPatterns("mutabilityModifiers")),
        varianceModifiers = normalizeOption(configuration.varianceModifiers, createDefaultExtensionPatterns("varianceModifiers")),
    ), cwd)

    return PartialConfiguration(
        inputRoots = configuration.inputRoots,
        input = configuration.input,
        output = configuration.output,
        ignoreInput = configuration.ignoreInput,
        ignoreOutput = configuration.ignoreOutput,
        libraryName = configuration.libraryName,
        libraryNameOutputPrefix = configuration.libraryNameOutputPrefix,
        isolatedOutputPackage = configuration.isolatedOutputPackage,
        moduleNameMapper = configuration.moduleNameMapper,
        packageNameMapper = configuration.packageNameMapper,
        importInjector = configuration.importInjector,
        importMapper = configuration.importMapper.unsafeCast<ReadonlyRecord<String, Rule>?>(),
        namespaceStrategy = configuration.namespaceStrategy,
        conflictResolutionStrategy = configuration.conflictResolutionStrategy,
        compilerOptions = configuration.compilerOptions.unsafeCast<CompilerOptions?>(),
        disclaimer = configuration.disclaimer,
        verbose = configuration.verbose,
        cwd = configuration.cwd,

        plugins = manyOf(values = extensions.plugins),
        injections = manyOf(values = extensions.injections),
        annotations = manyOf(values = extensions.annotations),
        nameResolvers = manyOf(values = extensions.nameResolvers),
        inheritanceModifiers = manyOf(values = extensions.inheritanceModifiers),
        mutabilityModifiers = manyOf(values = extensions.mutabilityModifiers),
        varianceModifiers = manyOf(values = extensions.varianceModifiers),
    )
}
