package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.Node
import typescript.isCallSignatureDeclaration
import typescript.isInterfaceDeclaration

val resolveInterfaceCallSignatureReturnTypeName: NameResolver = nameResolver@{ node, context ->
    val typeScriptService = context.lookupService(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriptService?.getParent(it) ?: it.getParentOrNull()
    }

    val callSignature = getParent(node)
        ?: return@nameResolver null
    if (!isCallSignatureDeclaration(callSignature)) return@nameResolver null
    if (callSignature.type != node) return@nameResolver null

    val interfaceNode = getParent(callSignature)
        ?: return@nameResolver null
    if (!isInterfaceDeclaration(interfaceNode)) return@nameResolver null

    val parentName = interfaceNode.name.text

    "${capitalize(parentName)}Result"
}
