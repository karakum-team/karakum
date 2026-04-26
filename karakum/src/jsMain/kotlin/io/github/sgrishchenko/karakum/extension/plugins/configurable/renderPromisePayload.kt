package io.github.sgrishchenko.karakum.extension.plugins.configurable

import io.github.sgrishchenko.karakum.extension.Context
import io.github.sgrishchenko.karakum.extension.Render
import typescript.Node
import typescript.TypeReferenceNode
import typescript.asArray

suspend fun renderPromisePayload(
    node: TypeReferenceNode,
    context: Context,
    render: Render<Node>,
): String {
    val typeArguments = requireNotNull(node.typeArguments)
    return render(typeArguments.asArray().first())
}
