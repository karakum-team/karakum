import {ConverterContext} from "./context.js";
import {Node} from "typescript"
import {Render} from "./render.js";
import {Lifecycle} from "./lifecycle.js";

export interface InjectionContext extends ConverterContext {
    static: boolean
}

export interface Injection<TNode extends Node = Node> extends Lifecycle {
    render(node: TNode, context: InjectionContext, next: Render): string | null
}

export type SimpleInjection<TNode extends Node = Node> =
    (node: TNode, context: InjectionContext, next: Render) => string | null

export function createSimpleInjection<TNode extends Node = Node>(
    render: SimpleInjection<TNode>
): Injection<TNode> {
    return {
        setup: () => undefined,

        traverse: () => undefined,

        render,

        generate: () => []
    }
}
