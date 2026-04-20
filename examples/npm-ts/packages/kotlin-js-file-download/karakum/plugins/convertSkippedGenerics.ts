import ts, {type Node} from "typescript"
import type {Context, Render} from "karakum"

export default function (node: Node, context: Context, render: Render<Node>) {
    if (
        ts.isTypeReferenceNode(node)

        && ts.isIdentifier(node.typeName)
        && node.typeName.text === "ArrayBufferView"

        && node.typeArguments === undefined
    ) {
        return `${render(node.typeName)}<*>`
    }

    return null
}
