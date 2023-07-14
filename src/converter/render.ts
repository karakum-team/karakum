import ts, {Node, TypeNode} from "typescript";
import {ConverterContext} from "./context.js";
import {ConverterPlugin} from "./plugin.js";
import {TypeScriptService, typeScriptServiceKey} from "./plugins/TypeScriptPlugin.js";
import {isPossiblyNullableType} from "./plugins/NullableUnionTypePlugin.js";

export type Render<TNode extends Node = Node> = (node: TNode) => string

export function ifPresent(part: string | null | undefined, render: (part: string) => string) {
    return part ? render(part) : ""
}

const primitiveKinds = new Set([
    ts.SyntaxKind.AnyKeyword,
    ts.SyntaxKind.UnknownKeyword,
    ts.SyntaxKind.UndefinedKeyword,
    ts.SyntaxKind.ObjectKeyword,
    ts.SyntaxKind.StringKeyword,
    ts.SyntaxKind.NumberKeyword,
    ts.SyntaxKind.BooleanKeyword,
    ts.SyntaxKind.VoidKeyword,
    ts.SyntaxKind.NeverKeyword,
    ts.SyntaxKind.SymbolKeyword,
])

function isPrimitiveType(node: Node) {
    return (
        primitiveKinds.has(node.kind)
        || ts.isLiteralTypeNode(node)
        || ts.isThisTypeNode(node)
    )
}

export function renderNullable(
    node: TypeNode | null | undefined,
    isNullable: boolean,
    context: ConverterContext,
    render: Render,
) {
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    // handle double nullability
    const resolvedType = node && typeScriptService?.resolveType(node)

    const isReallyNullable = (
        isNullable
        && !!resolvedType
        && !isPossiblyNullableType(resolvedType, context)
    )

    return renderResolvedNullable(node, isReallyNullable, context, render)
}

export function renderResolvedNullable(
    node: TypeNode | null | undefined,
    isNullable: boolean,
    context: ConverterContext,
    render: Render,
) {
    let type: string

    if (node) {
        type = render(node)

        // wrap complex types in parentheses
        if (
            isNullable
            && !isPrimitiveType(node)
            && !ts.isArrayTypeNode(node)
            && !ts.isTypeReferenceNode(node)
            && !ts.isParenthesizedTypeNode(node)
        ) {
            type = `(${type})`
        }
    } else {
        type = "Any? /* type isn't declared */"
    }

    return `${type}${isNullable ? "?" : ""}`
}

export function createRender(context: ConverterContext, plugins: ConverterPlugin[]): Render {
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    const render = (node: Node) => {
        for (const plugin of plugins) {
            const result = plugin.render(node, context, render)

            if (result !== null) return result
        }

        return `/* ${typeScriptService?.printNode(node)} */`
    }

    return render;
}
