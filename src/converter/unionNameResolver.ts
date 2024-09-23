import {ConverterContext} from "./context.js";
import {Node} from "typescript"

export type UnionNameResolver<TNode extends Node = Node> =
    (node: TNode, context: ConverterContext) => string | null
