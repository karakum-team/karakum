package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import typescript.asArray
import typescript.isFunctionTypeNode

val convertFunctionType = createPlugin plugin@{ node, context, render ->
    if (!isFunctionTypeNode(node)) return@plugin null

    val checkCoverageService = context.lookupService(checkCoverageServiceKey)
    val typeScriptService = context.lookupService(typeScriptServiceKey)

    checkCoverageService?.cover(node)

    if (node.typeParameters != null) {
        return@plugin "Function<Any?> /* ${typeScriptService?.printNode(node)} */"
    }

    val returnType = render(node.type)

    if (node.parameters.asArray().any { it.dotDotDotToken != null }) {
        return@plugin "Function<${returnType}> /* ${typeScriptService?.printNode(node)} */"
    }

    convertParameterDeclarations(node, context, render, ParameterDeclarationsConfiguration(
        strategy = ParameterDeclarationStrategy.lambda,
        template = { parameters, _ ->
            "(${parameters}) -> $returnType"
        },
    ))
}
