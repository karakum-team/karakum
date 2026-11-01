package io.github.sgrishchenko.karakum.extension.plugins

import js.reflect.invoke
import typescript.Node
import typescript.getLeadingCommentRanges
import typescript.isVariableDeclaration
import typescript.isVariableDeclarationList

fun isDeprecated(
    node: Node,
): Boolean {
    val sourceFileText = node.getSourceFile()
        ?.getFullText()
        ?: return false

    val commentOwner = node
        .takeIf { isVariableDeclaration(it) }
        ?.parent
        ?.takeIf { isVariableDeclarationList(it) }
        ?: node

    val commentRanges = getLeadingCommentRanges(sourceFileText, commentOwner.getFullStart())
        ?: return false

    return commentRanges.asSequence()
        .map { sourceFileText.substring(it.pos, it.end) }
        .any { "@deprecated" in it }
}