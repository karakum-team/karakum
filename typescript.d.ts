function setTokenSourceMapRange<T extends Node>(node: T, token: SyntaxKind, range: SourceMapRange | undefined): T;
/**
 * Gets a custom text range to use when emitting comments.
 */
function getCommentRange(node: Node): TextRange;
