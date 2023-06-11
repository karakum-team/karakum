import ts from "typescript";
import {NameResolver} from "../nameResolver";

export function resolveParenthesizedTypeName(resolver: NameResolver): NameResolver {
    return (node, context) => {
        if (
            !node.parent
            || !ts.isParenthesizedTypeNode(node.parent)
        ) {
            return resolver(node, context)
        }

        return resolver(node.parent, context)
    }
}
