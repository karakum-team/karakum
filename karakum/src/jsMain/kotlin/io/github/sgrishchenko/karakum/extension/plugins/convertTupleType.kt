package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import io.github.sgrishchenko.karakum.extension.ifPresent
import typescript.asArray
import typescript.isRestTypeNode
import typescript.isTupleTypeNode

val convertTupleType = createPlugin plugin@{ node, context, render ->
    if (!isTupleTypeNode(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    checkCoverageService?.cover(node)

    if (node.elements.asArray().any { isRestTypeNode(it) }) {
        checkCoverageService?.deepCover(node)

        return@plugin "js.array.ReadonlyArray<Any?> /* ${typeScriptService?.printNode(node)} */"
    }

    val elementArray = node.elements.asArray()
        .map { render(it) }
        .filter { it.isNotEmpty() }

    val tupleSize = elementArray.size

    val elements = elementArray.joinToString(separator = ", ")

    "js.array.Tuple${ifPresent(elements) { "${tupleSize}<${elements}>" }}"
}
