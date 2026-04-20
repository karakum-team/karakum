import ts, {isFunctionTypeNode, isInterfaceDeclaration, isPropertySignature, type Node} from "typescript";

export default function (node: Node) {
    if (
        isPropertySignature(node)

        // && node.type
        && !isFunctionTypeNode(node.type)

        && node.parent
        && isInterfaceDeclaration(node.parent)
        && node.parent.name.text === "RouterHistory"
    ) {
        return "val"
    }

    return null
}
