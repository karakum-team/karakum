package io.github.sgrishchenko.karakum.util

import typescript.Node
import typescript.SourceFile

fun Node.getParentOrNull() = parent.unsafeCast<Node?>()

fun Node.getSourceFileOrNull() = getSourceFile().unsafeCast<SourceFile?>()

private external interface MutableNode {
    var parent: Node
}

fun <TNode : Node> setParentNodes(rootNode: TNode): TNode {
    rootNode.forEachChild( { node ->
        node.unsafeCast<MutableNode>().parent = rootNode
        setParentNodes(node)
    })

    return rootNode
}
