import {ConverterPlugin} from "../plugin.js";
import ts, {CommentRange, Node, SourceFile} from "typescript";
import {ConverterContext} from "../context.js";
import {Render} from "../render.js";
import {GeneratedFile} from "../generated.js";

export class CommentsPlugin implements ConverterPlugin {
    private nestedCommentPattern = /(?<!^)\/\*/g

    private coveredCommentRanges = new Map<SourceFile, CommentRange[]>()

    generate(): GeneratedFile[] {
        return [];
    }

    render(node: Node, context: ConverterContext, next: Render): string | null {
        const sourceFile = node.getSourceFile()
        if (!sourceFile) return null

        const leadingComments = ts.getLeadingCommentRanges(sourceFile.getFullText(), node.getFullStart()) ?? []
        const trailingComments = ts.getTrailingCommentRanges(sourceFile.getFullText(), node.getEnd()) ?? []

        const leadingCommentsOutput = this.renderComments(sourceFile, leadingComments)
        const trailingCommentsOutput = this.renderComments(sourceFile, trailingComments)

        return `${leadingCommentsOutput}${next(node)}${trailingCommentsOutput}`
    }

    traverse(node: Node): void {
    }

    setup(context: ConverterContext): void {
    }

    private renderComments(sourceFile: SourceFile, commentRanges: CommentRange[]) {
        const fullText = sourceFile.getFullText()

        return commentRanges
            .filter(it => !this.isCommentRangeCovered(sourceFile, it))
            .map(it => {
                this.coverCommentRange(sourceFile, it)

                const text = fullText.slice(it.pos, it.end)
                const escapedTest = text.replace(this.nestedCommentPattern, "&#47;*")

                return it.hasTrailingNewLine ? `${escapedTest}\n` : text
            })
            .join("")
    }

    private isCommentRangeCovered(sourceFile: SourceFile, commentRange: CommentRange): boolean {
        const commentRanges = this.coveredCommentRanges.get(sourceFile)
        return commentRanges?.some(it => it.pos === commentRange.pos && it.end === commentRange.end) ?? false
    }

    private coverCommentRange(sourceFile: SourceFile, commentRange: CommentRange) {
        if (!this.coveredCommentRanges.has(sourceFile)) {
            this.coveredCommentRanges.set(sourceFile, [])
        }

        const commentRanges = this.coveredCommentRanges.get(sourceFile)
        commentRanges?.push(commentRange)
    }
}
