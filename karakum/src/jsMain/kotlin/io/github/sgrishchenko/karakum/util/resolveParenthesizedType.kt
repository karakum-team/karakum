package io.github.sgrishchenko.karakum.util

import typescript.TypeNode
import typescript.isParenthesizedTypeNode

fun resolveParenthesizedType(node: TypeNode): TypeNode {
    return if (isParenthesizedTypeNode(node)) {
        resolveParenthesizedType(node.type)
    } else {
        node
    }
}
