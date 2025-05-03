package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import io.github.sgrishchenko.karakum.extension.ifPresent
import io.github.sgrishchenko.karakum.extension.renderNullable
import typescript.asArray
import typescript.isConstructorTypeNode

val convertConstructorType = createPlugin plugin@{ node, context, render ->
    if (!isConstructorTypeNode(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    checkCoverageService?.cover(node)

    if (node.typeParameters != null) {
        return@plugin "js.function.ConstructorFunction<Nothing, Any?> /* ${typeScriptService?.printNode(node)} */"
    }

    val returnType = render(node.type)

    if (node.parameters.asArray().any { it.dotDotDotToken != null }) {
        return@plugin "js.function.ConstructorFunction<Nothing, ${returnType}> /* ${typeScriptService?.printNode(node)} */"
    }

    val parameterArray = node.parameters.asArray()
        .map {
            val isOptional = it.questionToken != null

            "/* ${typeScriptService?.printNode(it.name)} */ ${renderNullable(it.type, isOptional, context, render)}${if (isOptional) " /* use undefined for default */" else ""}"
        }
        .filter { it.isNotEmpty() }

    val tupleSize = parameterArray.size

    val elements = parameterArray.joinToString(separator = ", ")

    "js.function.ConstructorFunction<js.array.Tuple${ifPresent(elements) { "${tupleSize}<${elements}>" }}, ${returnType}>"
}
