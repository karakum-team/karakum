package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.Render
import io.github.sgrishchenko.karakum.extension.ifPresent
import typescript.Node
import typescript.NodeWithTypeArguments
import typescript.asArray

fun convertNodeWithTypeArguments(node: NodeWithTypeArguments, render: Render<Node>): String {
    val typeArguments = node.typeArguments?.asArray()
        ?.map { render(it) }
        ?.filter { it.isNotEmpty() }
        ?.joinToString(separator = ", ")

    return ifPresent(typeArguments) { "<${it}>" }
}
