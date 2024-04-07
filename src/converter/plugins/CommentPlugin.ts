import {ConverterPlugin} from "../plugin.js";
import ts, {CommentRange, Node, SourceFile} from "typescript";
import {ConverterContext} from "../context.js";
import {Render} from "../render.js";
import {GeneratedFile} from "../generated.js";

export const commentServiceKey = Symbol()

export class CommentService {
    private nestedCommentPattern = /(?<!^)\/\*/g

    renderLeadingComments(node: Node) {
        const sourceFile = node.getSourceFile()
        if (!sourceFile) return null

        const leadingComments = ts.getLeadingCommentRanges(sourceFile.getFullText(), node.getFullStart()) ?? []
        return this.renderComments(sourceFile, leadingComments)
    }

    renderTrailingComments(node: Node) {
        const sourceFile = node.getSourceFile()
        if (!sourceFile) return null

        const trailingComments = ts.getTrailingCommentRanges(sourceFile.getFullText(), node.getEnd()) ?? []
        return this.renderComments(sourceFile, trailingComments)
    }

    renderComments(sourceFile: SourceFile, commentRanges: CommentRange[]) {
        const fullText = sourceFile.getFullText()

        return commentRanges
            .map(it => {
                const text = fullText.slice(it.pos, it.end)
                const escapedTest = text
                    .replace(this.nestedCommentPattern, "&#47;*")
                    .replaceAll("\r\n", "\n")

                return it.hasTrailingNewLine ? `${escapedTest}\n` : text
            })
            .join("")
    }
}

export class CommentPlugin implements ConverterPlugin {
    private coveredCommentRanges = new Map<SourceFile, CommentRange[]>()
    private readonly commentsService = new CommentService()

    setup(context: ConverterContext): void {
        context.registerService(commentServiceKey, this.commentsService)
    }

    traverse(node: Node): void {
    }

    render(node: Node, context: ConverterContext, next: Render): string | null {
        const sourceFile = node.getSourceFile()
        if (!sourceFile) return null

        const leadingComments = (ts.getLeadingCommentRanges(sourceFile.getFullText(), node.getFullStart()) ?? [])
            .filter(it => !this.isCommentRangeCovered(sourceFile, it))

        const trailingComments = (ts.getTrailingCommentRanges(sourceFile.getFullText(), node.getEnd()) ?? [])
            .filter(it => !this.isCommentRangeCovered(sourceFile, it))

        const allComments = leadingComments.concat(trailingComments)

        for (const commentRange of allComments) {
            this.coverCommentRange(sourceFile, commentRange)
        }

        const leadingCommentOutput = this.commentsService.renderComments(sourceFile, leadingComments)
        const trailingCommentOutput = this.commentsService.renderComments(sourceFile, trailingComments)

        return `${leadingCommentOutput}${next(node)}${trailingCommentOutput}`
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

    generate(): GeneratedFile[] {
        return [];
    }
}
