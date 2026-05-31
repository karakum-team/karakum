import ts, {type Node} from "typescript"
import type {Context, Render, Abortable} from "karakum"

export default async function (node: Node, context: Context, render: Render<Node>, options: Abortable) {
    if (
        ts.isTypeReferenceNode(node)

        && ts.isIdentifier(node.typeName)
        && node.typeName.text === "ArrayBufferView"

        && node.typeArguments === undefined
    ) {
        return `${await render.run(node.typeName, options)}<*>`
    }

    return null
}
