package io.github.sgrishchenko.karakum.extension

import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.traverseSync
import js.array.*
import js.coroutines.promise
import js.promise.Promise
import typescript.*
import web.abort.Abortable
import web.abort.asCoroutineScope

typealias TypeParameterExtractionResult = ReadonlyArray<Tuple2<Node, Declaration>>

@JsExport
fun extractTypeParameters(
    node: Node,
    context: Context
): TypeParameterExtractionResult {
    val typeScriptService = context.lookupService(typeScriptServiceKey)

    val result = mutableListOf<Tuple2<Node, Declaration>>()

    val typeChecker = typeScriptService?.program?.getTypeChecker()

    traverseSync(node) { currentNode ->
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


suspend fun renderDeclaration(result: TypeParameterExtractionResult, render: Render<Node>): String {
    return result
        .map { (_, declaration) -> render(declaration) }
        .filter { it.isNotEmpty() }
        .joinToString(separator = ", ")
}

@JsExport
@JsName("renderDeclaration")
fun renderDeclarationAsync(
    result: TypeParameterExtractionResult,
    render: Render<Node>,
    options: Abortable = Abortable(),
): Promise<String> =
    options.asCoroutineScope().promise {
        renderDeclaration(result, render)
    }

suspend fun renderReference(result: TypeParameterExtractionResult, render: Render<Node>): String {
    return result
        .map { (node) -> render(node) }
        .filter { it.isNotEmpty() }
        .joinToString(separator = ", ")
}

@JsExport
@JsName("renderReference")
fun renderReferenceAsync(
    result: TypeParameterExtractionResult,
    render: Render<Node>,
    options: Abortable = Abortable(),
): Promise<String> =
    options.asCoroutineScope().promise {
        renderReference(result, render)
    }
