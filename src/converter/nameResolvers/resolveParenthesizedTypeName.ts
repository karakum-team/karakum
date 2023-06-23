import ts from "typescript";
import {NameResolver} from "../nameResolver";

export function resolveParenthesizedTypeName(resolver: NameResolver): NameResolver {
    const parenthesizedResolver: NameResolver = (node, context) => {
        if (
            !node.parent
            || !ts.isParenthesizedTypeNode(node.parent)
        ) {
            return resolver(node, context)
        }

        return parenthesizedResolver(node.parent, context)
    }

    return parenthesizedResolver
}
