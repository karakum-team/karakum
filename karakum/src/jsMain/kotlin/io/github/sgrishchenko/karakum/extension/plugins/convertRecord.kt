package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.Render
import io.github.sgrishchenko.karakum.extension.createPlugin
import io.github.sgrishchenko.karakum.extension.ifPresent
import typescript.*

private fun renderRecord(node: IndexSignatureDeclaration, render: Render<Node>): String {
    val firstParameterType = node.parameters.asArray().firstOrNull()?.type

    val keyType = if (firstParameterType != null) {
        render(firstParameterType)
    } else {
        "Any? /* type isn't declared */"
    }

    val type = render(node.type)

    return "js.objects.ReadonlyRecord<$keyType, $type>"
}

val convertRecord = createPlugin plugin@{ node, context, render ->
    if (
        isTypeLiteralNode(node)
        && node.members.asArray().singleOrNull()?.let { isIndexSignatureDeclaration(it) } == true
    ) {
        val indexSignature = node.members.asArray().singleOrNull() ?: return@plugin null
        if (!isIndexSignatureDeclaration(indexSignature)) return@plugin null

        val checkCoverageService = context.lookupService(checkCoverageServiceKey)
        checkCoverageService?.cover(node)
        checkCoverageService?.cover(indexSignature)

        renderRecord(indexSignature, render)
    } else if (
        isTypeAliasDeclaration(node)
        && node.type
            .let { if (isTypeLiteralNode(it)) it.members else null }
            ?.asArray()?.singleOrNull()?.let { isIndexSignatureDeclaration(it) } == true
    ) {
        val type = node.type
        if (!isTypeLiteralNode(type)) return@plugin null

        val indexSignature = type.members.asArray().singleOrNull() ?: return@plugin null
        if (!isIndexSignatureDeclaration(indexSignature)) return@plugin null

        val checkCoverageService = context.lookupService(checkCoverageServiceKey)
        checkCoverageService?.cover(node)
        checkCoverageService?.cover(type)
        checkCoverageService?.cover(indexSignature)

        val name = render(node.name)
        val indexType = renderRecord(indexSignature, render)

        val typeParameters = node.typeParameters?.asArray()
            ?.map { render(it) }
            ?.filter { it.isNotEmpty() }
            ?.joinToString(separator = ", ")

        "typealias $name${ifPresent(typeParameters) { "<${it}>" }} = $indexType"
    } else if (
        isInterfaceDeclaration(node)
        && node.members.asArray().singleOrNull()?.let { isIndexSignatureDeclaration(it) } == true
    ) {
        val indexSignature = node.members.asArray().singleOrNull() ?: return@plugin null
        if (!isIndexSignatureDeclaration(indexSignature)) return@plugin null

        val typeScriptService = context.lookupService(typeScriptServiceKey)

        val typeChecker = typeScriptService?.program?.getTypeChecker() ?: return@plugin null
        val symbol = typeChecker.getSymbolAtLocation(node.name) ?: return@plugin null

        if (symbol.declarations?.size != 1) return@plugin null
        if (symbol.valueDeclaration != null) return@plugin null

        if (node.heritageClauses != null) return@plugin null

        val checkCoverageService = context.lookupService(checkCoverageServiceKey)
        checkCoverageService?.cover(node)
        checkCoverageService?.cover(indexSignature)

        val name = render(node.name)
        val type = renderRecord(indexSignature, render)

        val typeParameters = node.typeParameters?.asArray()
            ?.map { render(it) }
            ?.filter { it.isNotEmpty() }
            ?.joinToString(separator = ", ")

        "typealias $name${ifPresent(typeParameters) { "<${it}>" }} = $type"
    } else {
        null
    }
}
