package io.github.sgrishchenko.karakum.util

import typescript.Node

@JsExport
fun traverseSync(rootNode: Node, handler: (node: Node) -> Unit) {
    handler(rootNode)

    rootNode.forEachChild({ node ->
        traverseSync(node, handler)
        undefined
    })
}

suspend fun traverse(rootNode: Node, handler: suspend (node: Node) -> Unit) {
    handler(rootNode)

    val children = mutableListOf<Node>()

    rootNode.forEachChild({ node ->
        children += node
        undefined
    })

    for (node in children) {
        traverse(node, handler)
    }
}
