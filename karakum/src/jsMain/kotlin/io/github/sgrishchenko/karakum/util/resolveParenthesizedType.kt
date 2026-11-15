package io.github.sgrishchenko.karakum.util

import js.reflect.invoke
import typescript.ast.TypeNode
import typescript.ast.`is`.isParenthesizedTypeNode

fun resolveParenthesizedType(node: TypeNode): TypeNode {
    return if (isParenthesizedTypeNode(node)) {
        resolveParenthesizedType(node.type)
    } else {
        node
    }
}
