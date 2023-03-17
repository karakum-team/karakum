import {ConverterContext} from "./context";
import {Node} from "typescript"

export type InheritanceModifier<TNode extends Node = Node> =
    (node: TNode, context: ConverterContext) => string | null