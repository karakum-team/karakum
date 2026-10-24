package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.Plugin
import io.github.sgrishchenko.karakum.extension.createPlugin
import js.reflect.TypeGuard
import js.reflect.invoke
import typescript.Node
import typescript.isPrefixUnaryExpression

fun <T : Node> convertLiteral(
    predicate: TypeGuard<Node, T>,
    render: (node: Node) -> String?
): Plugin {
    val primitivePlugin = convertPrimitive(predicate, render)

    return createPlugin plugin@{ node, context, pluginRender ->
        val typeScriptService = context.lookupService(typeScriptServiceKey)

        if (isPrefixUnaryExpression(node) && predicate(node.operand)) {
            val result = primitivePlugin.render(node.operand, context, pluginRender)
            if (result == null) return@plugin null

            "$result /* ${typeScriptService?.printNode(node)} */"
        } else {
            val result = primitivePlugin.render(node, context, pluginRender)
            if (result == null) return@plugin null

            "$result /* ${typeScriptService?.printNode(node)} */"
        }
    }
}
