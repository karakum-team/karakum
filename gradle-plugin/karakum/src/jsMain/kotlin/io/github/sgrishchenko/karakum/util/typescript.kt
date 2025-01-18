package io.github.sgrishchenko.karakum.util

import typescript.Node
import typescript.SourceFile

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
fun Node.getParentOrNull() = parent as Node?

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
fun Node.getSourceFileOrNull() = getSourceFile() as SourceFile?

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
