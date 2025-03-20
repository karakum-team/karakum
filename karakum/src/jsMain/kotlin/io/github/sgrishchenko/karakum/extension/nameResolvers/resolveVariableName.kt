package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.TypeScriptService
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.Node
import typescript.isIdentifier
import typescript.isVariableDeclaration

val resolveVariableName: NameResolver = nameResolver@{ node, context ->
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriptService?.getParent(it) ?: it.getParentOrNull()
    }

    val variable = getParent(node)
        ?: return@nameResolver null
    if (!isVariableDeclaration(variable)) return@nameResolver null

    val variableNameNode = variable.name
    if (!isIdentifier(variableNameNode)) return@nameResolver null

    val variableName = variableNameNode.text

    capitalize(variableName)
}
