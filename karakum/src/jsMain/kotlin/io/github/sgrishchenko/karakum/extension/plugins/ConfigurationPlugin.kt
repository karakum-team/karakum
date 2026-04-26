package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.configuration.Configuration
import io.github.sgrishchenko.karakum.configuration.JsConfiguration
import io.github.sgrishchenko.karakum.configuration.toJsConfiguration
import io.github.sgrishchenko.karakum.extension.*
import typescript.Node

val configurationServiceKey = ContextKey<ConfigurationService>()

@JsExport
@JsName("configurationServiceKey")
val jsConfigurationServiceKey = ContextKey<JsConfigurationService>()

class ConfigurationService(val configuration: Configuration)

@JsExport
@JsName("ConfigurationService")
class JsConfigurationService @JsExport.Ignore constructor(val configuration: JsConfiguration)

class ConfigurationPlugin(configuration: Configuration) : Plugin {
    private val configurationService = ConfigurationService(configuration)
    private val jsConfigurationService = JsConfigurationService(configuration.toJsConfiguration())

    override suspend fun traverse(node: Node, context: Context) = Unit

    override suspend fun render(node: Node, context: Context, next: Render<Node>) = null

    override suspend fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()

    override suspend fun setup(context: Context) {
        context.registerService(configurationServiceKey, configurationService)
        context.registerService(jsConfigurationServiceKey, jsConfigurationService)
    }
}
