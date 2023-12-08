import {Node} from "typescript";
import {ConverterContext} from "./context.js";
import {GeneratedFile} from "./generated.js";

export interface Lifecycle {
    setup(context: ConverterContext): void

    traverse(node: Node, context: ConverterContext): void

    generate(context: ConverterContext): GeneratedFile[]
}
