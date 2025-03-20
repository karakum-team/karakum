package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.isExpressionWithTypeArguments
import typescript.isIdentifier
import typescript.isTypeReferenceNode

val convertErrorTypeReferenceNode = createSimplePlugin plugin@{ node, _, _ ->
    if (isTypeReferenceNode(node)) {
        val typeName = node.typeName

        if (
            isIdentifier(typeName)
            && typeName.text === "Error"
        ) {
            return@plugin "js.errors.JsError"
        }
    }

    if (isExpressionWithTypeArguments(node)) {
        val expression = node.expression

        if (
            isIdentifier(expression)
            && expression.text === "Error"
        ) {
            return@plugin "js.errors.JsError"
        }
    }

    null
}
