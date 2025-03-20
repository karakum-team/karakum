package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.configuration.Configuration
import io.github.sgrishchenko.karakum.extension.Context
import io.github.sgrishchenko.karakum.extension.ConverterPlugin
import io.github.sgrishchenko.karakum.extension.GeneratedFile
import io.github.sgrishchenko.karakum.extension.Render
import js.symbol.Symbol
import typescript.Node

@OptIn(ExperimentalJsExport::class)
@JsExport
val configurationServiceKey = Symbol()

@OptIn(ExperimentalJsExport::class)
@JsExport
class ConfigurationService @JsExport.Ignore constructor(val configuration: Configuration)

class ConfigurationPlugin(configuration: Configuration) : ConverterPlugin<Node> {
    private val configurationService = ConfigurationService(configuration)

    override fun traverse(node: Node, context: Context) = Unit

    override fun render(node: Node, context: Context, next: Render<Node>) = null

    override fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()

    override fun setup(context: Context) {
        context.registerService(configurationServiceKey, configurationService)
    }
}
