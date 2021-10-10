import {ConverterPlugin} from "../plugin";
import {Node} from "typescript";
import {Render} from "../render";

export function convertPrimitive<TNode extends Node = Node>(
    predicate: (node: Node) => node is TNode,
    render: Render<TNode>
): ConverterPlugin

export function convertPrimitive(
    predicate: (node: Node) => boolean,
    render: Render
): ConverterPlugin

export function convertPrimitive(
    predicate: (node: Node) => boolean,
    render: Render
): ConverterPlugin {
    return (node, context) => {
        if (!predicate(node)) return null
        context.cover(node)

        return render(node)
    }
}
