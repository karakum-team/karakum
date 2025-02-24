package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.ConverterPlugin
import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.Node
import typescript.isPrefixUnaryExpression

fun convertLiteral(
    predicate: (node: Node) -> Boolean,
    render: (node: Node) -> String?
): ConverterPlugin<Node> {
    val primitivePlugin = convertPrimitive(predicate, render)

    return createSimplePlugin plugin@{ node: Node, context, pluginRender ->
        val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

        if (isPrefixUnaryExpression(node) && predicate(node.operand)) {
            val result = primitivePlugin.render(node.operand, context, pluginRender)
            if (result === null) return@plugin null

            "$result /* ${typeScriptService?.printNode(node)} */"
        } else {
            val result = primitivePlugin.render(node, context, pluginRender)
            if (result === null) return@plugin null

            "$result /* ${typeScriptService?.printNode(node)} */"
        }
    }
}
