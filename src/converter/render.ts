import {Node} from "typescript";
import {ConverterContext} from "./context";
import {ConverterPlugin} from "./plugin";
import {TypeScriptService, typeScriptServiceKey} from "./plugins/TypeScriptPlugin";

export type Render<TNode extends Node = Node> = (node: TNode) => string

export function ifPresent(part: string | null | undefined, render: (part: string) => string) {
    return part ? render(part) : ""
}

export function createRender(context: ConverterContext, plugins: ConverterPlugin[]): Render {
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    const render = (node: Node) => {
        for (const plugin of plugins) {
            const result = plugin.render(node, context, render)

            if (result !== null) return result
        }

        return `/* ${typeScriptService?.printNode(node)} */`
    }

    return render;
}
