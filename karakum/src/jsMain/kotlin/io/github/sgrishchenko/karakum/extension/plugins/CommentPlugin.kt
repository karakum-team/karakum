package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.Context
import io.github.sgrishchenko.karakum.extension.GeneratedFile
import io.github.sgrishchenko.karakum.extension.Plugin
import io.github.sgrishchenko.karakum.extension.Render
import io.github.sgrishchenko.karakum.util.getSourceFileOrNull
import js.array.ReadonlyArray
import js.symbol.Symbol
import typescript.*

@OptIn(ExperimentalJsExport::class)
@JsExport
val commentServiceKey = Symbol()

@OptIn(ExperimentalJsExport::class)
@JsExport
class CommentService @JsExport.Ignore constructor() {
    private val nestedCommentPattern = "(?<!^)/\\*".toRegex()

    fun renderLeadingComments(node: Node): String? {
        val sourceFile = node.getSourceFileOrNull() ?: return null

        val leadingComments = getLeadingCommentRanges(sourceFile.getFullText(), node.getFullStart()) ?: emptyArray()
        return renderComments(sourceFile, leadingComments)
    }

    fun renderTrailingComments(node: Node): String? {
        val sourceFile = node.getSourceFileOrNull() ?: return null

        val trailingComments = getTrailingCommentRanges(sourceFile.getFullText(), node.getEnd()) ?: emptyArray()
        return renderComments(sourceFile, trailingComments)
    }

    fun renderComments(sourceFile: SourceFile, commentRanges: ReadonlyArray<CommentRange>): String {
        val fullText = sourceFile.getFullText()

        return commentRanges.joinToString(separator = "") {
            val text = fullText.slice(it.pos.toInt()..<it.end.toInt())
            val escapedTest = text
                .replace(nestedCommentPattern, "&#47;*")
                .replace("\r\n", "\n")

            if (it.hasTrailingNewLine == true) "${escapedTest}\n" else text
        }
    }
}

class CommentPlugin : Plugin {
    private val coveredCommentRanges = mutableMapOf<SourceFile, MutableList<CommentRange>>()
    private val commentsService = CommentService()

    override fun setup(context: Context) {
        context.registerService(commentServiceKey, this.commentsService)
    }

    override fun traverse(node: Node, context: Context) = Unit

    override fun render(node: Node, context: Context, next: Render<Node>): String? {
        val sourceFile = node.getSourceFileOrNull() ?: return null

        val leadingComments = (getLeadingCommentRanges(sourceFile.getFullText(), node.getFullStart()) ?: emptyArray())
            .filter { !isCommentRangeCovered(sourceFile, it) }
            .toTypedArray()

        val trailingComments = (getTrailingCommentRanges(sourceFile.getFullText(), node.getEnd()) ?: emptyArray())
            .filter { !isCommentRangeCovered(sourceFile, it) }
            .toTypedArray()

        val allComments = leadingComments + trailingComments

        for (commentRange in allComments) {
            coverCommentRange(sourceFile, commentRange)
        }

        val leadingCommentOutput = commentsService.renderComments(sourceFile, leadingComments)
        val trailingCommentOutput = commentsService.renderComments(sourceFile, trailingComments)

        return "${leadingCommentOutput}${next(node)}${trailingCommentOutput}"
    }

    private fun isCommentRangeCovered(sourceFile: SourceFile, commentRange: CommentRange): Boolean {
        val commentRanges = coveredCommentRanges[sourceFile]
        return commentRanges?.any { it.pos == commentRange.pos && it.end == commentRange.end } ?: false
    }

    private fun coverCommentRange(sourceFile: SourceFile, commentRange: CommentRange) {
        if (sourceFile !in coveredCommentRanges) {
            coveredCommentRanges[sourceFile] = mutableListOf()
        }

        val commentRanges = coveredCommentRanges[sourceFile]
        commentRanges?.add(commentRange)
    }

    override fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
}
