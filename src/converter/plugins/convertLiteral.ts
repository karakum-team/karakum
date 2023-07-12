import {ConverterPlugin, createSimplePlugin} from "../plugin.js";
import {Node} from "typescript";
import {Render} from "../render.js";
import {convertPrimitive} from "./convertPrimitive.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";

export function convertLiteral<TNode extends Node = Node>(
    predicate: (node: Node) => node is TNode,
    render: Render<TNode>
): ConverterPlugin {
    const primitivePlugin = convertPrimitive(predicate, render)

    return createSimplePlugin((node, context, render) => {
        const result = primitivePlugin.render(node, context, render)
        if (result === null) return null

        const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

        return `${result} /* ${typeScriptService?.printNode(node)} */`
    })
}
