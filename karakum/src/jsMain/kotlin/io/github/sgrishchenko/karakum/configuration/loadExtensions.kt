package io.github.sgrishchenko.karakum.configuration

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.util.glob
import io.github.sgrishchenko.karakum.util.toModuleName
import js.array.ReadonlyArray
import js.import.import
import js.objects.JsPlainObject

@JsPlainObject
external interface ExtensionConfiguration {
    val plugins: ReadonlyArray<String>
    val injections: ReadonlyArray<String>
    val annotations: ReadonlyArray<String>
    val nameResolvers: ReadonlyArray<String>
    val inheritanceModifiers: ReadonlyArray<String>
    val varianceModifiers: ReadonlyArray<String>
}

@JsPlainObject
external interface Extensions {
    val plugins: ReadonlyArray<Plugin>
    val injections: ReadonlyArray<Injection>
    val annotations: ReadonlyArray<Annotation>
    val nameResolvers: ReadonlyArray<NameResolver>
    val inheritanceModifiers: ReadonlyArray<InheritanceModifier>
    val varianceModifiers: ReadonlyArray<VarianceModifier>
}

external interface ExtensionModule {
    val default: Any?
}

/**
 * Loads extension modules based on specified patterns.
 * Exported as a utility function for programmatic API.
 */
suspend fun <T> loadExtensions(
    name: String,
    patterns: ReadonlyArray<String>,
    cwd: String,
    loader: (extension: Any) -> T = { it.unsafeCast<T>() },
): ReadonlyArray<T> {
    val fileNames = glob(patterns, cwd)

    val extensions = mutableListOf<T>()

    for (fileName in fileNames) {
        val extensionModule = import<ExtensionModule>(toModuleName(fileName))
        val extensionExport = requireNotNull(extensionModule.default)

        if (extensionExport is ReadonlyArray<*>) {
            console.log("$name file: $fileName [x${extensionExport.size}]")

            extensions += extensionExport.map { loader(requireNotNull(it)) }
        } else {
            console.log("$name file: $fileName")

            extensions += loader(extensionExport)
        }
    }

    return extensions.toTypedArray()
}

suspend fun loadExtensions(
    configuration: ExtensionConfiguration,
    cwd: String,
): Extensions {

    val plugins = loadExtensions(
        "Plugin",
        configuration.plugins,
        cwd
    ) { plugin ->
        if (jsTypeOf(plugin) == "function") {
            createSimplePlugin(plugin.unsafeCast<SimplePlugin>())
        } else {
            plugin.unsafeCast<Plugin>()
        }
    }

    val injections = loadExtensions(
        "Injection",
        configuration.injections,
        cwd
    ) { injection ->
        if (jsTypeOf(injection) == "function") {
            createSimpleInjection(injection.unsafeCast<SimpleInjection>())
        } else {
            injection.unsafeCast<Injection>()
        }
    }

    val annotations = loadExtensions<Annotation>(
        "Annotation",
        configuration.annotations,
        cwd,
    )

    val nameResolvers = loadExtensions<NameResolver>(
        "Name Resolver",
        configuration.nameResolvers,
        cwd,
    )

    val inheritanceModifiers = loadExtensions<InheritanceModifier>(
        "Inheritance Modifier",
        configuration.inheritanceModifiers,
        cwd,
    )

    val varianceModifiers = loadExtensions<VarianceModifier>(
        "Variance Modifier",
        configuration.varianceModifiers,
        cwd,
    )

    return Extensions(
        plugins = plugins,
        injections = injections,
        annotations = annotations,
        nameResolvers = nameResolvers,
        inheritanceModifiers = inheritanceModifiers,
        varianceModifiers = varianceModifiers,
    )
}
