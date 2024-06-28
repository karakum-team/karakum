import {ConverterContext} from "./context.js";
import {Node} from "typescript"

export interface AnnotationContext extends ConverterContext {
    isAnonymousDeclaration: boolean
}

export type Annotation<TNode extends Node = Node> =
    (node: TNode, context: AnnotationContext) => string | null
