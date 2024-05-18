import {Node} from "typescript"
import {ConverterContext} from "./context.js";

export type VarianceModifier<TNode extends Node = Node> =
    (node: TNode, context: ConverterContext) => string | null
