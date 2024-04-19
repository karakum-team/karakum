import {ConverterContext} from "./context.js";
import {Node} from "typescript"
import {Render} from "./render.js";
import {ConverterPlugin} from "./plugin.js";

export enum InjectionType {
    MEMBER = "Member",
    STATIC_MEMBER = "StaticMember",

    PARAMETER = "Parameter",
    TYPE_PARAMETER = "TypeParameter",

    HERITAGE_CLAUSE = "HeritageClause",
}

export interface InjectionContext extends ConverterContext {
    /** @deprecated */
    static: boolean

    type: InjectionType
}

export interface Injection<TNode extends Node = Node, TInjectionNode extends Node = Node> extends ConverterPlugin<TNode> {
    inject(node: TInjectionNode, context: InjectionContext, render: Render): string | null
}

export type SimpleInjection<TInjectionNode extends Node = Node> =
    (node: TInjectionNode, context: InjectionContext, next: Render) => string | null

export function createSimpleInjection<TNode extends Node = Node, TInjectionNode extends Node = Node>(
    inject: SimpleInjection<TInjectionNode>
): Injection<TNode, TInjectionNode> {
    return {
        setup: () => undefined,

        traverse: () => undefined,

        render: () => null,

        inject,

        generate: () => []
    }
}
