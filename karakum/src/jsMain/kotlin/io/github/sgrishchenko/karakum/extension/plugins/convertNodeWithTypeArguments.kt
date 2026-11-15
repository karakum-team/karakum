package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.Render
import io.github.sgrishchenko.karakum.extension.ifPresent
import typescript.ast.Node
import typescript.ast.NodeWithTypeArguments

suspend fun convertNodeWithTypeArguments(node: NodeWithTypeArguments, render: Render<Node>): String {
    val typeArguments = node.typeArguments
        ?.map { render(it) }
        ?.filter { it.isNotEmpty() }
        ?.joinToString(separator = ", ")

    return ifPresent(typeArguments) { "<${it}>" }
}
