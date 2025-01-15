package io.github.sgrishchenko.karakum.configuration

import io.github.sgrishchenko.karakum.extension.Annotation
import js.array.ReadonlyArray
import js.import.import
import js.objects.JsPlainObject
import js.objects.jso
import node.fs.GlobOptionsWithoutFileTypes
import node.fs.glob
import node.util.isArray
import typescript.Node

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
    val nameResolvers: ReadonlyArray<NameResolver>?
    val inheritanceModifiers: ReadonlyArray<InheritanceModifier>?
    val varianceModifiers: ReadonlyArray<VarianceModifier>?
}

@JsPlainObject
external interface Extensions {
    val plugins: ReadonlyArray<ConverterPlugin>
    val injections: ReadonlyArray<Injection>
    val annotations: ReadonlyArray<Annotation<Node>>
    val nameResolvers: ReadonlyArray<NameResolver>
    val inheritanceModifiers: ReadonlyArray<InheritanceModifier>
    val varianceModifiers: ReadonlyArray<VarianceModifier>
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
    val fileNames = glob(patterns, jso<GlobOptionsWithoutFileTypes> { this.cwd = cwd })

    val extensions = mutableListOf<T>()

    for (fileName in fileNames) {
        val extensionModule = import<ExtensionModule>(toModuleName(fileName))
        val extensionExport = requireNotNull(extensionModule.default)

        if (isArray(extensionExport)) {
            console.log("$name file: $fileName [x${extensionExport.size}]")

            extensions += extensionExport.map { loader(requireNotNull(it)) }
        } else {
            console.log("$name file: $fileName")

            extensions += loader(extensionExport)
        }
    }

    return extensions.toTypedArray()
}

suspend fun loadExtensionsFromGlobs(
    configuration: ExtensionConfiguration,
    cwd: String,
): Extensions {

    val plugins = loadExtensions<ConverterPlugin>(
        "Plugin",
        configuration.plugins,
        cwd
    ) { plugin ->
        if (jsTypeOf(plugin) == "function") {
            createSimplePlugin(plugin as SimpleConverterPlugin)
        } else {
            plugin as ConverterPlugin
        }
    }

    val injections = loadExtensions<Injection>(
        "Injection",
        configuration.injections,
        cwd
    ) { injection ->
        if (jsTypeOf(injection) == "function") {
            createSimpleInjection(injection as SimpleInjection)
        } else {
            injection as Injection
        }
    }

    val annotations = loadExtensions<Annotation<Node>>(
        "io.github.sgrishchenko.karakum.extension.Annotation",
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

suspend fun loadExtensionsFromFile(
    extensions: String,
    cwd: String,
): Extensions {
    val absoluteExtensions = toAbsolute(extensions, cwd)
    val extensionModule = import<ExtensionModule>(toModuleName(absoluteExtensions))

    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
    val partialExtensions = requireNotNull(extensionModule.default) as PartialExtensions

    console.log("Extension file: $absoluteExtensions")

    val plugins = (partialExtensions.plugins ?: emptyArray())
        .map { plugin ->
            if (jsTypeOf(plugin) == "function") {
                createSimplePlugin(plugin as SimpleConverterPlugin)
            } else {
                plugin as ConverterPlugin
            }
        }

    val injections = (partialExtensions.injections ?: emptyArray())
        .map { injection ->
            if (jsTypeOf(injection) == "function") {
                createSimpleInjection(injection as SimpleInjection)
            } else {
                injection as Injection
            }
        }

    return Extensions(
        plugins = plugins,
        injections = injections,
        annotations = partialExtensions.annotations ?: emptyArray(),
        nameResolvers = partialExtensions.nameResolvers ?: emptyArray(),
        inheritanceModifiers = partialExtensions.inheritanceModifiers ?: emptyArray(),
        varianceModifiers = partialExtensions.varianceModifiers ?: emptyArray(),
    }
}
