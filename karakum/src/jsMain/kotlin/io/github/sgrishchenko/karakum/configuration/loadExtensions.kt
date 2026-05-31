package io.github.sgrishchenko.karakum.configuration

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.util.glob
import io.github.sgrishchenko.karakum.util.toModuleName
import js.array.ReadonlyArray
import js.import.import
import kotlinx.js.JsPlainObject

@JsPlainObject
external interface ExtensionConfiguration {
    val plugins: ReadonlyArray<String>
    val injections: ReadonlyArray<String>
    val annotations: ReadonlyArray<String>
    val nameResolvers: ReadonlyArray<String>
    val inheritanceModifiers: ReadonlyArray<String>
    val mutabilityModifiers: ReadonlyArray<String>
    val varianceModifiers: ReadonlyArray<String>
}

@JsPlainObject
external interface Extensions {
    val plugins: ReadonlyArray<JsPlugin>
    val injections: ReadonlyArray<JsInjection>
    val annotations: ReadonlyArray<JsAnnotation>
    val nameResolvers: ReadonlyArray<JsNameResolver>
    val inheritanceModifiers: ReadonlyArray<JsInheritanceModifier>
    val mutabilityModifiers: ReadonlyArray<JsMutabilityModifier>
    val varianceModifiers: ReadonlyArray<JsVarianceModifier>
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
    patterns: List<String>,
    cwd: String,
    loader: (extension: Any) -> T = { it.unsafeCast<T>() },
): List<T> {
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

    return extensions
}

suspend fun loadExtensions(
    configuration: ExtensionConfiguration,
    cwd: String,
): Extensions {

    val plugins = loadExtensions(
        "Plugin",
        configuration.plugins.toList(),
        cwd
    ) { plugin ->
        if (jsTypeOf(plugin) == "function") {
            createJsPlugin(plugin.unsafeCast<SimpleJsPlugin>())
        } else {
            plugin.unsafeCast<JsPlugin>()
        }
    }

    val injections = loadExtensions(
        "Injection",
        configuration.injections.toList(),
        cwd
    ) { injection ->
        if (jsTypeOf(injection) == "function") {
            createJsInjection(injection.unsafeCast<SimpleJsInjection>())
        } else {
            injection.unsafeCast<JsInjection>()
        }
    }

    val annotations = loadExtensions<JsAnnotation>(
        "Annotation",
        configuration.annotations.toList(),
        cwd,
    )

    val nameResolvers = loadExtensions<JsNameResolver>(
        "Name Resolver",
        configuration.nameResolvers.toList(),
        cwd,
    )

    val inheritanceModifiers = loadExtensions<JsInheritanceModifier>(
        "Inheritance Modifier",
        configuration.inheritanceModifiers.toList(),
        cwd,
    )

    val mutabilityModifiers = loadExtensions<JsMutabilityModifier>(
        "Mutability modifier",
        configuration.mutabilityModifiers.toList(),
        cwd,
    )

    val varianceModifiers = loadExtensions<JsVarianceModifier>(
        "Variance Modifier",
        configuration.varianceModifiers.toList(),
        cwd,
    )

    return Extensions(
        plugins = plugins.toTypedArray(),
        injections = injections.toTypedArray(),
        annotations = annotations.toTypedArray(),
        nameResolvers = nameResolvers.toTypedArray(),
        inheritanceModifiers = inheritanceModifiers.toTypedArray(),
        mutabilityModifiers = mutabilityModifiers.toTypedArray(),
        varianceModifiers = varianceModifiers.toTypedArray(),
    )
}
