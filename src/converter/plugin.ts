import {ConverterContext} from "./context";
import {Render} from "./render";
import {Node} from "typescript"

export type ConverterPlugin<TNode extends Node = Node> = (node: TNode, context: ConverterContext, render: Render) => string | null
