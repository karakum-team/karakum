package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.configuration.Configuration
import io.github.sgrishchenko.karakum.extension.*
import typescript.Node

@JsExport
val configurationServiceKey = ContextKey<ConfigurationService>()

@JsExport
class ConfigurationService @JsExport.Ignore constructor(val configuration: Configuration)

class ConfigurationPlugin(configuration: Configuration) : Plugin {
    private val configurationService = ConfigurationService(configuration)

    override suspend fun traverse(node: Node, context: Context) = Unit

    override suspend fun render(node: Node, context: Context, next: Render<Node>) = null

    override suspend fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()

    override suspend fun setup(context: Context) {
        context.registerService(configurationServiceKey, configurationService)
    }
}
