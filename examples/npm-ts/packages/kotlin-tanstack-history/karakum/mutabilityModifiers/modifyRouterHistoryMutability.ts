import ts, {type Node} from "typescript"

export default function (node: Node) {
    if (
        ts.isPropertySignature(node)

        && node.type
        && !ts.isFunctionTypeNode(node.type)

        && node.parent
        && ts.isInterfaceDeclaration(node.parent)
        && node.parent.name.text === "RouterHistory"
    ) {
        return "val"
    }

    return null
}
