import {CommentRange, Node} from "typescript";

export interface ConverterContext {
    cover(node: Node): void
    deepCover(node: Node): void

    isCommentCovered(node: Node): boolean
    coverComment(node: Node): void

    isCommentRangeCovered(node: CommentRange): boolean
    coverCommentRange(commentRange: CommentRange): void
}
