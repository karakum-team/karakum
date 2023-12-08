import {ConverterContext} from "./context.js";
import {Render} from "./render.js";
import {Node} from "typescript"
import {Lifecycle} from "./lifecycle.js";

export interface ConverterPlugin<TNode extends Node = Node> extends Lifecycle {
    render(node: TNode, context: ConverterContext, next: Render): string | null
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

        generate: () => []
    }
}
