package io.github.sgrishchenko.karakum.util

import typescript.Node

@OptIn(ExperimentalJsExport::class)
@JsExport
fun traverse(rootNode: Node, handler: (node: Node) -> Unit) {
    handler(rootNode)

    rootNode.forEachChild({ node ->
        traverse(node, handler)
        undefined
    })
}
