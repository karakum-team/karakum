package io.github.sgrishchenko.karakum.extension.plugins.configurable

import io.github.sgrishchenko.karakum.extension.Context
import io.github.sgrishchenko.karakum.extension.Render
import typescript.ast.Node
import typescript.ast.TypeReferenceNode

suspend fun renderPromisePayload(
    node: TypeReferenceNode,
    context: Context,
    render: Render<Node>,
): String {
    val typeArguments = requireNotNull(node.typeArguments)
    return render(typeArguments.first())
}
