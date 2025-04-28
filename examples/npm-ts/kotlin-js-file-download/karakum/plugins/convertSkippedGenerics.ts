import ts, {type Node} from "typescript"
import type {Context} from "karakum"

export default function (node: Node, context: Context, render: (node: Node) => string) {
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
