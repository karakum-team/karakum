package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.Context
import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import js.reflect.invoke
import typescript.Node
import typescript.isParenthesizedTypeNode

fun resolveParenthesizedTypeName(resolver: NameResolver): NameResolver {
    suspend fun parenthesizedResolver(node: Node, context: Context): String? {
        val typeScriptService = context.lookupService(typeScriptServiceKey)
        val getParent = { it: Node ->
            typeScriptService?.getParent(it) ?: it.parent
        }

        val parent = getParent(node)
        if (
            parent == null
            || !isParenthesizedTypeNode(parent)
        ) {
            return resolver(node, context)
        }

        return parenthesizedResolver(parent, context)
    }

    return { node, context -> parenthesizedResolver(node, context) }
}
