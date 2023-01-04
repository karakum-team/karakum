import {Node} from "typescript";
import {ConverterContext} from "./context";
import {ConverterPlugin} from "./plugin";

export type Render<TNode extends Node = Node> = (node: TNode) => string

export function ifPresent(part: string | undefined, render: (part: string) => string) {
    return part ? render(part) : ""
}

export function createRender(context: ConverterContext, plugins: ConverterPlugin[]) : Render{
    const render = (node: Node) => {
        for (const plugin of plugins) {
            const result = plugin.render(node, context, render)

            if (result !== null) return result
        }

        return `/* ${node.getText()} */`
    }

    return render;
}
