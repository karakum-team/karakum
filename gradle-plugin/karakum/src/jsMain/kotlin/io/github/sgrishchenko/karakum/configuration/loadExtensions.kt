package io.github.sgrishchenko.karakum.configuration

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.util.glob
import io.github.sgrishchenko.karakum.util.toAbsolute
import io.github.sgrishchenko.karakum.util.toModuleName
import js.array.ReadonlyArray
import js.import.import
import js.objects.JsPlainObject
import typescript.Node

@JsPlainObject
external interface ExtensionConfiguration {
    val plugins: ReadonlyArray<String>
    val injections: ReadonlyArray<String>
    val annotations: ReadonlyArray<String>
    val nameResolvers: ReadonlyArray<String>
    val inheritanceModifiers: ReadonlyArray<String>
    val varianceModifiers: ReadonlyArray<String>
}

external interface PartialExtensions {
    val plugins: ReadonlyArray<Any>? /* SimpleConverterPlugin | ConverterPlugin */
    val injections: ReadonlyArray<Any>? /* SimpleInjection | Injection */
    val annotations: ReadonlyArray<Annotation<Node>>?
    val nameResolvers: ReadonlyArray<NameResolver<Node>>?
    val inheritanceModifiers: ReadonlyArray<InheritanceModifier<Node>>?
    val varianceModifiers: ReadonlyArray<VarianceModifier<Node>>?
}

@JsPlainObject
external interface Extensions {
    val plugins: ReadonlyArray<ConverterPlugin<Node>>
    val injections: ReadonlyArray<Injection<Node, Node>>
    val annotations: ReadonlyArray<Annotation<Node>>
    val nameResolvers: ReadonlyArray<NameResolver<Node>>
    val inheritanceModifiers: ReadonlyArray<InheritanceModifier<Node>>
    val varianceModifiers: ReadonlyArray<VarianceModifier<Node>>
}

external interface ExtensionModule {
    val default: Any?
}

private suspend fun <T> loadExtensions(
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

@Suppress("UNCHECKED_CAST", "UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
suspend fun loadExtensionsFromGlobs(
    configuration: ExtensionConfiguration,
    cwd: String,
): Extensions {

    val plugins = loadExtensions<ConverterPlugin<Node>>(
        "Plugin",
        configuration.plugins,
        cwd
    ) { plugin ->
        if (jsTypeOf(plugin) == "function") {
            createSimplePlugin(plugin as SimpleConverterPlugin<Node>)
        } else {
            plugin as ConverterPlugin<Node>
        }
    }

    val injections = loadExtensions<Injection<Node, Node>>(
        "Injection",
        configuration.injections,
        cwd
    ) { injection ->
        if (jsTypeOf(injection) == "function") {
            createSimpleInjection(injection as SimpleInjection<Node>)
        } else {
            injection as Injection<Node, Node>
        }
    }

    val annotations = loadExtensions<Annotation<Node>>(
        "io.github.sgrishchenko.karakum.extension.Annotation",
        configuration.annotations,
        cwd,
    )

    val nameResolvers = loadExtensions<NameResolver<Node>>(
        "Name Resolver",
        configuration.nameResolvers,
        cwd,
    )

    val inheritanceModifiers = loadExtensions<InheritanceModifier<Node>>(
        "Inheritance Modifier",
        configuration.inheritanceModifiers,
        cwd,
    )

    val varianceModifiers = loadExtensions<VarianceModifier<Node>>(
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

@Suppress("UNCHECKED_CAST", "UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
suspend fun loadExtensionsFromFile(
    extensions: String,
    cwd: String,
): Extensions {
    val absoluteExtensions = toAbsolute(extensions, cwd)
    val extensionModule = import<ExtensionModule>(toModuleName(absoluteExtensions))

    val partialExtensions = requireNotNull(extensionModule.default) as PartialExtensions

    console.log("Extension file: $absoluteExtensions")

    val plugins = (partialExtensions.plugins ?: emptyArray())
        .map { plugin ->
            if (jsTypeOf(plugin) == "function") {
                createSimplePlugin(plugin as SimpleConverterPlugin<Node>)
            } else {
                plugin as ConverterPlugin<Node>
            }
        }

    val injections = (partialExtensions.injections ?: emptyArray())
        .map { injection ->
            if (jsTypeOf(injection) == "function") {
                createSimpleInjection(injection as SimpleInjection<Node>)
            } else {
                injection as Injection<Node, Node>
            }
        }

    return Extensions(
        plugins = plugins.toTypedArray(),
        injections = injections.toTypedArray(),
        annotations = partialExtensions.annotations ?: emptyArray(),
        nameResolvers = partialExtensions.nameResolvers ?: emptyArray(),
        inheritanceModifiers = partialExtensions.inheritanceModifiers ?: emptyArray(),
        varianceModifiers = partialExtensions.varianceModifiers ?: emptyArray(),
    )
}
