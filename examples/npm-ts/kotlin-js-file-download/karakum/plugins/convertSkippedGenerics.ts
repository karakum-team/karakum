import ts, {type Node} from "typescript"
import type {Context} from "karakum"

console.log("NODE_OPTIONS", process.env.NODE_OPTIONS)

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
