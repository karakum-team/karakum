package io.github.sgrishchenko.karakum.configuration

import io.github.sgrishchenko.karakum.util.Many
import io.github.sgrishchenko.karakum.util.manyOf
import io.github.sgrishchenko.karakum.util.toArray
import io.github.sgrishchenko.karakum.util.toPosix
import js.array.ReadonlyArray
import node.process.process

private fun normalizeOption(
    patterns: Many<Any>?,
    defaultValue: ReadonlyArray<String> = emptyArray(),
): ReadonlyArray<String> {
    return patterns?.toArray()
        ?.map { it as String }
        ?.toTypedArray()
        ?: defaultValue
}

suspend fun reifyConfiguration(configuration: SchemaConfiguration): PartialConfiguration {
    val cwd = toPosix(configuration.cwd ?: process.cwd())

    val extensions = loadExtensions(ExtensionConfiguration(
        plugins = normalizeOption(configuration.plugins, arrayOf("karakum/plugins/*.js")),
        injections = normalizeOption(configuration.injections, arrayOf("karakum/injections/*.js")),
        annotations = normalizeOption(configuration.annotations, arrayOf("karakum/annotations/*.js")),
        nameResolvers = normalizeOption(configuration.nameResolvers, arrayOf("karakum/nameResolvers/*.js")),
        inheritanceModifiers = normalizeOption(configuration.inheritanceModifiers, arrayOf("karakum/inheritanceModifiers/*.js")),
        varianceModifiers = normalizeOption(configuration.varianceModifiers, arrayOf("karakum/varianceModifiers/*.js")),
    ), cwd)

    return PartialConfiguration(
        inputRoots = configuration.inputRoots,
        input = configuration.input,
        output = configuration.output,
        ignoreInput = configuration.ignoreInput,
        ignoreOutput = configuration.ignoreOutput,
        libraryName = configuration.libraryName,
        libraryNameOutputPrefix = configuration.libraryNameOutputPrefix,
        granularity = configuration.granularity,
        moduleNameMapper = configuration.moduleNameMapper,
        packageNameMapper = configuration.packageNameMapper,
        importInjector = configuration.importInjector,
        importMapper = configuration.importMapper,
        namespaceStrategy = configuration.namespaceStrategy,
        conflictResolutionStrategy = configuration.conflictResolutionStrategy,
        compilerOptions = configuration.compilerOptions,
        disclaimer = configuration.disclaimer,
        verbose = configuration.verbose,
        cwd = configuration.cwd,

        plugins = manyOf(values = extensions.plugins),
        injections = manyOf(values = extensions.injections),
        annotations = manyOf(values = extensions.annotations),
        nameResolvers = manyOf(values = extensions.nameResolvers),
        inheritanceModifiers = manyOf(values = extensions.inheritanceModifiers),
        varianceModifiers = manyOf(values = extensions.varianceModifiers),
    )
}
