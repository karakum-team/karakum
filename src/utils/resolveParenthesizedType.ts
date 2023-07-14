import ts, {TypeNode} from "typescript";

export function resolveParenthesizedType(node: TypeNode) {
    if (ts.isParenthesizedTypeNode(node)) {
        return resolveParenthesizedType(node.type)
    } else {
        return node
    }
}
