package io.github.sgrishchenko.karakum.configuration

import io.github.sgrishchenko.karakum.util.Many
import io.github.sgrishchenko.karakum.util.toArray
import io.github.sgrishchenko.karakum.util.toPosix
import js.array.ReadonlyArray
import js.objects.recordOf
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

    return recordOf(
        "inputRoots" to configuration.inputRoots,
        "input" to configuration.input,
        "output" to configuration.output,
        "ignoreInput" to configuration.ignoreInput,
        "ignoreOutput" to configuration.ignoreOutput,
        "libraryName" to configuration.libraryName,
        "libraryNameOutputPrefix" to configuration.libraryNameOutputPrefix,
        "granularity" to configuration.granularity,
        "moduleNameMapper" to configuration.moduleNameMapper,
        "packageNameMapper" to configuration.packageNameMapper,
        "importInjector" to configuration.importInjector,
        "importMapper" to configuration.importMapper,
        "namespaceStrategy" to configuration.namespaceStrategy,
        "conflictResolutionStrategy" to configuration.conflictResolutionStrategy,
        "compilerOptions" to configuration.compilerOptions,
        "disclaimer" to configuration.disclaimer,
        "verbose" to configuration.verbose,
        "cwd" to configuration.cwd,

        "plugins" to extensions.plugins,
        "injections" to extensions.injections,
        "annotations" to extensions.annotations,
        "nameResolvers" to extensions.nameResolvers,
        "inheritanceModifiers" to extensions.inheritanceModifiers,
        "varianceModifiers" to extensions.varianceModifiers,
    ).unsafeCast<PartialConfiguration>()
}
