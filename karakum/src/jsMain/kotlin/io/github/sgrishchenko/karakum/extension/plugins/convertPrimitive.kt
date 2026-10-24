package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.Plugin
import io.github.sgrishchenko.karakum.extension.createPlugin
import js.reflect.TypeGuard
import js.reflect.invoke
import typescript.Node

fun convertPrimitive(
    predicate: (node: Node) -> Boolean,
    render: (render: Node) -> String?,
): Plugin =
    createPlugin plugin@{ node, context, _ ->
        if (!predicate(node))
            return@plugin null

        val checkCoverageService = context.lookupService(checkCoverageServiceKey)
        checkCoverageService?.cover(node)

        render(node)
    }

fun <T : Node> convertPrimitive(
    predicate: TypeGuard<Node, T>,
    render: (render: T) -> String?,
): Plugin =
    createPlugin plugin@{ node, context, _ ->
        if (!predicate(node))
            return@plugin null

        val checkCoverageService = context.lookupService(checkCoverageServiceKey)
        checkCoverageService?.cover(node)

        render(node)
    }
