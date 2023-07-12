import {ConverterContext} from "./context.js";
import {Render} from "./render.js";
import {Node} from "typescript"

export interface ConverterPlugin<TNode extends Node = Node> {
    setup(context: ConverterContext): void

    traverse(node: Node, context: ConverterContext): void

    render(node: TNode, context: ConverterContext, next: Render): string | null

    generate(context: ConverterContext): Record<string, string>
}

export type SimpleConverterPlugin<TNode extends Node = Node> =
    (node: TNode, context: ConverterContext, next: Render) => string | null

export function createSimplePlugin<TNode extends Node = Node>(
    render: SimpleConverterPlugin<TNode>
): ConverterPlugin<TNode> {
    return {
        setup: () => undefined,

        traverse: () => undefined,

        render,

        generate: () => ({})
    }
}
