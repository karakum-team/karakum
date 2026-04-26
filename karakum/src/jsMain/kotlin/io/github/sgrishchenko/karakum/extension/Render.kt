package io.github.sgrishchenko.karakum.extension

import io.github.sgrishchenko.karakum.extension.plugins.isPossiblyNullableType
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import js.array.ReadonlyArray
import js.coroutines.promise
import js.promise.Promise
import typescript.*
import web.abort.Abortable
import web.abort.asCoroutineScope

@JsExport
interface Render<in TNode : Node> {
    @JsExport.Ignore
    suspend operator fun invoke(node: TNode): String

    fun run(node: TNode, options: Abortable): Promise<String>  =
        options.asCoroutineScope().promise {
            invoke(node)
        }
}

fun <TNode : Node> Render(render: suspend (node: TNode) -> String) = object : Render<TNode> {
    override suspend fun invoke(node: TNode): String = render(node)
}

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

suspend fun renderNullable(
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

@JsExport
@JsName("renderNullable")
fun renderNullableAsync(
    node: TypeNode?,
    isNullable: Boolean,
    context: Context,
    render: Render<Node>,
    options: Abortable = Abortable(),
): Promise<String> =
    options.asCoroutineScope().promise {
        renderNullable(node, isNullable, context, render)
    }

suspend fun renderResolvedNullable(
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
    val typeScriptService = context.lookupService(typeScriptServiceKey)

    suspend fun render(node: Node, parentNode: Node?, parentIndex: Int): String {
        for ((index, plugin) in plugins.withIndex()) {
            if (node == parentNode && index <= parentIndex) continue

            val result = plugin.render(node, context, Render { currentNode -> render(currentNode, node, index) })

            if (result != null) return result
        }

        return "/* ${typeScriptService?.printNode(node)} */"
    }

    return Render { node -> render(node, null, -1) }
}
