package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.TypeScriptService
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.Node
import typescript.isIdentifier
import typescript.isVariableDeclaration

val resolveVariableName = NameResolver<Node> { node, context ->
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriptService?.getParent(it) ?: it.getParentOrNull()
    }

    val variable = getParent(node)
        ?: return@NameResolver null
    if (!isVariableDeclaration(variable)) return@NameResolver null

    val variableNameNode = variable.name
    if (!isIdentifier(variableNameNode)) return@NameResolver null

    val variableName = variableNameNode.text

    capitalize(variableName)
}
