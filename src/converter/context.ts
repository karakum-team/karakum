import {Node} from "typescript";

export interface ConverterContext {
    cover(node: Node): void
    deepCover(node: Node): void
}
