package io.github.sgrishchenko.karakum.extension.plugins

import typescript.Node
import typescript.getLeadingCommentRanges

fun isDeprecated(
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