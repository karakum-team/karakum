package io.github.sgrishchenko.karakum.extension

import io.github.sgrishchenko.karakum.extension.plugins.TypeScriptService
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.traverse
import js.array.JsTuple2
import js.array.ReadonlyArray
import js.array.tupleOf
import typescript.*

typealias TypeParameterExtractionResult = ReadonlyArray<JsTuple2<Node, Declaration>>

@OptIn(ExperimentalJsExport::class)
@JsExport
fun extractTypeParameters(
    node: Node,
    context: ConverterContext
): TypeParameterExtractionResult {
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    val result = mutableListOf<JsTuple2<Node, Declaration>>()

    val typeChecker = typeScriptService?.program?.getTypeChecker()

    traverse(node) { currentNode ->
        if (isIdentifier(currentNode)) {
            val symbol = typeChecker?.getSymbolAtLocation(currentNode)
            val typeParameterDeclarations = (symbol?.declarations ?: emptyArray())
                .filter { declaration -> isTypeParameterDeclaration(declaration) }

            for (declaration in typeParameterDeclarations) {
                var typeParameterContainer = typeScriptService?.getParent(declaration)

                if (typeParameterContainer != null && isInferTypeNode(typeParameterContainer)) {
                    val conditionalType = typeScriptService?.findClosest(typeParameterContainer) {
                        isConditionalTypeNode(it)
                    }

                    if (conditionalType != null && isConditionalTypeNode(conditionalType)) {
                        typeParameterContainer = conditionalType.trueType
                    }
                }

                val foundParent = typeScriptService?.findClosest(node) { it == typeParameterContainer }

                if (foundParent != null && foundParent != node) {
                    result += tupleOf(currentNode, declaration)
                }
            }
        }
    }

    return result.toTypedArray()
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun renderDeclaration(result: TypeParameterExtractionResult, render: Render<Node>): String {
    return result
        .map { (_, declaration) -> render(declaration) }
        .filter { it.isNotEmpty() }
        .joinToString(separator = ", ")
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun renderReference(result: TypeParameterExtractionResult, render: Render<Node>): String {
    return result
        .map { (node) -> render(node) }
        .filter { it.isNotEmpty() }
        .joinToString(separator = ", ")
}
