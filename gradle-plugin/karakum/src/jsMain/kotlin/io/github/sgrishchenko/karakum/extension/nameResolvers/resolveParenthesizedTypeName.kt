package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.ConverterContext
import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.TypeScriptService
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.Node
import typescript.isParenthesizedTypeNode

fun resolveParenthesizedTypeName(resolver: NameResolver<Node>): NameResolver<Node> {
    fun parenthesizedResolver(node: Node, context: ConverterContext): String? {
        val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
        val getParent = { it: Node ->
            typeScriptService?.getParent(it) ?: it.getParentOrNull()
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

    return NameResolver { node, context -> parenthesizedResolver(node, context) }
}
