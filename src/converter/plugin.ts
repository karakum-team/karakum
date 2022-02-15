import {ConverterContext} from "./context";
import {Render} from "./render";
import {Node} from "typescript"

export interface ConverterPlugin<TNode extends Node = Node> {
    traverse(node: Node): void

    render(node: TNode, context: ConverterContext, next: Render): string | null

    generate(): Record<string, string>
}

export function createSimplePlugin<TNode extends Node = Node>(
    render: (node: TNode, context: ConverterContext, next: Render) => string | null
): ConverterPlugin<TNode> {
    return {
        traverse: () => {},

        render,

        generate: () => ({})
    }
}
