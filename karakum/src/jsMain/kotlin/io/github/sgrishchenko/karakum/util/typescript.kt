package io.github.sgrishchenko.karakum.util

import typescript.Node

private external interface MutableNode {
    var parent: Node
}

fun <TNode : Node> setParentNodes(rootNode: TNode): TNode {
    rootNode.forEachChild({ node ->
        node.unsafeCast<MutableNode>().parent = rootNode
        setParentNodes(node)
        undefined
    })

    return rootNode
}
