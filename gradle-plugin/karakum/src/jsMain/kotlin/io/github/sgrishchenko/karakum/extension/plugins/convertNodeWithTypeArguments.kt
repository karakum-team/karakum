package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import io.github.sgrishchenko.karakum.extension.ifPresent
import typescript.NodeWithTypeArguments
import typescript.asArray

val convertNodeWithTypeArguments = createSimplePlugin plugin@{ node: NodeWithTypeArguments, _, render ->
    val typeArguments = node.typeArguments?.asArray()
        ?.map { render(it) }
        ?.filter { it.isNotEmpty() }
        ?.joinToString(separator = ", ")

    ifPresent(typeArguments) { "<${it}>" }
}
