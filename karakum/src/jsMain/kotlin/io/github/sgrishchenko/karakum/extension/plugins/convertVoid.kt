package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.*

private fun extractReturnType(node: Node): Node? {
    // signatures
    if (isCallSignatureDeclaration(node)) return node.type
    if (isConstructSignatureDeclaration(node)) return node.type
    if (isMethodSignature(node)) return node.type

    // function like
    if (isFunctionDeclaration(node)) return node.type
    if (isMethodDeclaration(node)) return node.type

    // types
    if (isFunctionTypeNode(node)) return node.type
    if (isConstructorTypeNode(node)) return node.type

    return null
}

val convertVoid = createSimplePlugin plugin@{ node, _, _ ->
    if (node.kind !== SyntaxKind.VoidKeyword) return@plugin null

    val returnType = extractReturnType(node.parent)

    if (returnType != null) {
        return@plugin "Unit"
    }

    "js.core.Void"
}
