import ts from "typescript";
import {NameResolver} from "../nameResolver.js";
import {TypeScriptService, typeScriptServiceKey} from "../plugins/TypeScriptPlugin.js";

export function resolveParenthesizedTypeName(resolver: NameResolver): NameResolver {
    const parenthesizedResolver: NameResolver = (node, context) => {
        const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
        const getParent = typeScriptService?.getParent.bind(typeScriptService) ?? ((node: ts.Node) => node.parent)

        const parent = getParent(node)
        if (
            !parent
            || !ts.isParenthesizedTypeNode(parent)
        ) {
            return resolver(node, context)
        }

        return parenthesizedResolver(parent, context)
    }

    return parenthesizedResolver
}
