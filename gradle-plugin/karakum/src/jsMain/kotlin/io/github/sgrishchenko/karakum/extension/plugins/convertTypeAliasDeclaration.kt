package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import io.github.sgrishchenko.karakum.extension.ifPresent
import typescript.*

val convertTypeAliasDeclaration = createSimplePlugin plugin@{ node: Node, context, render ->
    if (!isTypeAliasDeclaration(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    val exportModifier = node.modifiers?.asArray()?.find { it.kind == SyntaxKind.ExportKeyword }
    exportModifier?.let { checkCoverageService?.cover(exportModifier) }

    val declareModifier = node.modifiers?.asArray()?.find { it.kind == SyntaxKind.DeclareKeyword }
    declareModifier?.let { checkCoverageService?.cover(declareModifier) }

    val name = render(node.name)

    val typeParameters = node.typeParameters?.asArray()
        ?.map { render(it) }
        ?.filter { it.isNotEmpty() }
        ?.joinToString(separator = ", ")

    val typeNode = node.type

    if (isTypeLiteralNode(typeNode)) {
        return@plugin convertTypeLiteral(typeNode, name, typeParameters, true, context, render)
    }

    if (isMappedTypeNode(typeNode)) {
        return@plugin convertMappedType(typeNode, name, typeParameters, true, context, render)
    }

    if (isLiteralUnionType(typeNode, context)) {
        return@plugin convertLiteralUnionType(typeNode, name, true, context, render).declaration
    }

    if (isInheritedTypeLiteral(typeNode)) {
        return@plugin convertInheritedTypeLiteral(typeNode, name, typeParameters, true, context, render)
    }

    if (isFunctionTypeNode(typeNode)) {
        val functionTypeParameters = typeNode.typeParameters

        if (functionTypeParameters != null) {

            checkCoverageService?.cover(typeNode)

            val mergedTypeParameters = (node.typeParameters?.asArray() ?: emptyArray())
                .map { render(it) }
                .filter { it.isNotEmpty() }
                .joinToString(separator = ", ")

            val returnType = render(typeNode.type)

            val type = convertParameterDeclarations(
                typeNode, context, render, ParameterDeclarationsConfiguration(
                strategy = ParameterDeclarationStrategy.lambda,
                template = { parameters, _ ->
                    "(${parameters}) -> $returnType"
                }
            ))

            return@plugin "typealias ${name}${ifPresent(mergedTypeParameters) { "<${it}>"}} = $type"
        }
    }

    val type = render(typeNode)

    "typealias ${name}${ifPresent(typeParameters) { "<${it}>" }} = $type"
}
