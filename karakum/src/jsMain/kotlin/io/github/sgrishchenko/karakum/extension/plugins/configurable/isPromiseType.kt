package io.github.sgrishchenko.karakum.extension.plugins.configurable

import io.github.sgrishchenko.karakum.extension.Context
import io.github.sgrishchenko.karakum.extension.isBuiltin
import js.reflect.invoke
import typescript.ast.Node
import typescript.ast.`is`.isIdentifier
import typescript.ast.`is`.isTypeReferenceNode

@JsExport
fun isPromiseType(node: Node, context: Context): Boolean {
    if (!isTypeReferenceNode(node)) return false

    val typeName = node.typeName

    return isIdentifier(typeName)
            && typeName.text == "Promise"
            && isBuiltin(typeName, context)
}
