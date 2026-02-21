package io.github.sgrishchenko.karakum.extension

import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.getSourceFileOrNull
import typescript.Node

private val builtinSources = listOf(
    "^.*/typescript/lib/lib\\.decorators.+\\.d\\.ts$".toRegex(),
    "^.*/typescript/lib/lib\\.dom.+\\.d\\.ts$".toRegex(),
    "^.*/typescript/lib/lib\\.es.+\\.d\\.ts$".toRegex(),
    "^.*/typescript/lib/lib\\.scripthost.+\\.d\\.ts$".toRegex(),
    "^.*/typescript/lib/lib\\.webworker.+\\.d\\.ts$".toRegex(),
)

@JsExport
fun isBuiltin(node: Node, context: Context): Boolean {
    val typeScriptService = context.lookupService(typeScriptServiceKey) ?: return false
    val typeChecker = typeScriptService.program.getTypeChecker()

    val symbol = typeChecker.getSymbolAtLocation(node) ?: return false
    val declarations = symbol.declarations ?: emptyArray()
    val valueDeclaration = symbol.valueDeclaration

    val allDeclarations = if (valueDeclaration != null) {
        declarations + valueDeclaration
    } else {
        declarations
    }

    return allDeclarations.any { declaration ->
        val sourceFileName = declaration.getSourceFileOrNull()?.fileName

        sourceFileName != null && builtinSources.any { it.matches(sourceFileName) }
    }
}
