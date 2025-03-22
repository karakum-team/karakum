package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import io.github.sgrishchenko.karakum.util.camelize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import io.github.sgrishchenko.karakum.util.isKebab
import io.github.sgrishchenko.karakum.util.isValidIdentifier
import typescript.*

fun createKebabAnnotation(node: Node): String {
    if (isStringLiteral(node) && node.text == "") {
        return "@JsName(\"\")"
    }

    if (
        isStringLiteral(node)
        && !isValidIdentifier(node.text)
        && isKebab(node.text)
        && isValidIdentifier(camelize(node.text))
    ) {
        return "@JsName(\"${node.text}\")"
    }

    return ""
}

fun convertMemberNameLiteral(node: StringLiteral): String {
    return if (node.text === "") {
        "`_`"
    } else if (isValidIdentifier(node.text)) {
        node.text
    } else if (
        isKebab(node.text)
        && isValidIdentifier(camelize(node.text))
    ) {
        camelize(node.text)
    } else {
        "`${node.text}`"
    }
}

val convertMemberName = createPlugin plugin@{ node, context, _ ->
    if (isStringLiteral(node) || isNumericLiteral(node)) {
        val parent = node.getParentOrNull() ?: return@plugin null

        if (
            (isPropertyDeclaration(parent) && parent.name === node)
            || (isPropertySignature(parent) && parent.name === node)

            || (isMethodDeclaration(parent) && parent.name === node)
            || (isMethodSignature(parent) && parent.name === node)

            || (isGetAccessor(parent) && parent.name === node)
            || (isSetAccessor(parent) && parent.name === node)
        ) {
            val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
            checkCoverageService?.cover(node)

            if (isNumericLiteral(node)) return@plugin "`${node.text}`"
            if (isStringLiteral(node)) return@plugin convertMemberNameLiteral(node)
        }
    }

    null
}
