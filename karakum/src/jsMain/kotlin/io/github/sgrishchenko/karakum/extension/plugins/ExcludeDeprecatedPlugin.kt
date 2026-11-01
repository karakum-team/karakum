package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import typescript.Node
import typescript.getLeadingCommentRanges

val ExcludeDeprecatedPlugin = createPlugin { node, _, _ ->
    if (isDeprecated(node)) "" else null
}

private fun isDeprecated(
    node: Node,
): Boolean {
    val sourceFileText = node.getSourceFile()
        ?.getFullText()
        ?: return false

    val commentRanges = getLeadingCommentRanges(sourceFileText, node.getFullStart())
        ?: return false

    return commentRanges.asSequence()
        .map { sourceFileText.substring(it.pos, it.end) }
        .any { "@deprecated" in it }
}
