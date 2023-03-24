import {ConverterContext} from "./context";
import {Node} from "typescript"

export type Annotation<TNode extends Node = Node> =
    (node: TNode, context: ConverterContext) => string | null
