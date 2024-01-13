import {Node} from "typescript"
import {ConverterContext} from "./context.js";
import {Signature} from "./plugins/convertParameterDeclaration.js";

export interface InheritanceModifierContext extends ConverterContext {
    signature?: Signature | undefined
    getter?: boolean | undefined
    setter?: boolean | undefined
}

export type InheritanceModifier<TNode extends Node = Node> =
    (node: TNode, context: InheritanceModifierContext) => string | null
