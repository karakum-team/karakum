package io.github.sgrishchenko.karakum.extension

import io.github.sgrishchenko.karakum.extension.plugins.TypeScriptService
import io.github.sgrishchenko.karakum.extension.plugins.isPossiblyNullableType
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import js.array.ReadonlyArray
import seskar.js.JsNativeInvoke
import typescript.*

external interface Render<in TNode : Node> {
    @JsNativeInvoke
    operator fun invoke(node: TNode): String
}

fun <TNode : Node> Render(render: (node: TNode) -> String) = render.unsafeCast<Render<TNode>>()

@JsExport
fun ifPresent(part: String?, render: (part: String) -> String): String {
    return part?.takeIf { it.isNotEmpty() }?.let(render) ?: ""
}

private val primitiveKinds = setOf(
    SyntaxKind.AnyKeyword,
    SyntaxKind.UnknownKeyword,
    SyntaxKind.UndefinedKeyword,
    SyntaxKind.ObjectKeyword,
    SyntaxKind.StringKeyword,
    SyntaxKind.NumberKeyword,
    SyntaxKind.BooleanKeyword,
    SyntaxKind.VoidKeyword,
    SyntaxKind.NeverKeyword,
    SyntaxKind.SymbolKeyword,
    SyntaxKind.BigIntKeyword,
)

private fun isPrimitiveType(node: Node): Boolean {
    return node.kind in primitiveKinds
            || isLiteralTypeNode(node)
            || isThisTypeNode(node)
}

@JsExport
fun renderNullable(
    node: TypeNode?,
    isNullable: Boolean,
    context: Context,
    render: Render<Node>,
): String {
    val isReallyNullable = (
        isNullable
        && node != null
        && !isPossiblyNullableType(node, context)
    )

    return renderResolvedNullable(node, isReallyNullable, render)
}

fun renderResolvedNullable(
    node: TypeNode?,
    isNullable: Boolean,
    render: Render<Node>,
): String {
    val type = if (node != null) {
        render(node).let {
            if (
                isNullable
                && !isPrimitiveType(node)
                && !isArrayTypeNode(node)
                && !isTypeReferenceNode(node)
                && !isParenthesizedTypeNode(node)
            ) {
                // wrap complex types in parentheses
                "($it)"
            } else {
                it
            }
        }
    } else {
        "Any? /* type isn't declared */"
    }

    return "$type${if (isNullable) "?" else ""}"
}

fun createRender(context: Context, plugins: ReadonlyArray<Plugin>): Render<Node> {
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    fun render(node: Node, parentNode: Node?, parentIndex: Int): String {
        for ((index, plugin) in plugins.withIndex()) {
            if (node == parentNode && index <= parentIndex) continue

            val result = plugin.render(node, context, Render { currentNode -> render(currentNode, node, index) })

            if (result != null) return result
        }

        return "/* ${typeScriptService?.printNode(node)} */"
    }

    return Render { node -> render(node, null, -1) }
}
