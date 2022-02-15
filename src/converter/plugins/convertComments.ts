import ts, {CommentRange, SourceFile} from "typescript";
import {createSimplePlugin} from "../plugin";
import {ConverterContext} from "../context";

function renderComments(sourceFile: SourceFile, commentRanges: CommentRange[], context: ConverterContext) {
    const fullText = sourceFile.getFullText()

    return commentRanges
        .filter(it => !context.isCommentRangeCovered(it))
        .map(it => {
            context.coverCommentRange(it)

            const text = fullText.slice(it.pos, it.end)
            return it.hasTrailingNewLine ? `${text}\n` : text
        })
        .join("")
}

export const convertComments = createSimplePlugin((node, context, render) => {
    if (context.isCommentCovered(node)) return null
    context.coverComment(node)

    const sourceFile = node.getSourceFile()

    const leadingComments = ts.getLeadingCommentRanges(sourceFile.getFullText(), node.getFullStart()) ?? []
    const trailingComments = ts.getTrailingCommentRanges(sourceFile.getFullText(), node.getEnd()) ?? []

    const leadingCommentsOutput = renderComments(sourceFile, leadingComments, context)
    const trailingCommentsOutput = renderComments(sourceFile, trailingComments, context)

    return `${leadingCommentsOutput}${render(node)}${trailingCommentsOutput}`
})
