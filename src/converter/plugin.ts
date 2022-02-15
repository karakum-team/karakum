import {ConverterContext} from "./context";
import {Render} from "./render";
import {Node} from "typescript"

export interface ConverterPlugin<TNode extends Node = Node> {
    setup(context: ConverterContext): void

    traverse(node: Node, context: ConverterContext): void

    render(node: TNode, context: ConverterContext, next: Render): string | null

    generate(context: ConverterContext): Record<string, string>
}

export function createSimplePlugin<TNode extends Node = Node>(
    render: (node: TNode, context: ConverterContext, next: Render) => string | null
): ConverterPlugin<TNode> {
    return {
        setup: () => undefined,

        traverse: () => undefined,

        render,

        generate: () => ({})
    }
}
