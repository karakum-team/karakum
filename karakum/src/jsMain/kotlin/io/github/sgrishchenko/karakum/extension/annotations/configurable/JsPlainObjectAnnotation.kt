package io.github.sgrishchenko.karakum.extension.annotations.configurable

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import js.numbers.contains
import js.promise.Promise
import kotlinx.js.JsPlainObject
import typescript.*
import web.abort.Abortable

@JsExport
@JsPlainObject
external interface JsPlainObjectAnnotationConfiguration {
    val isJsPlainObject: ((Node, AnnotationContext, Abortable) -> Promise<Boolean>)?
    val ignore: ((Node, AnnotationContext, Abortable) -> Promise<Boolean>)?
}

private fun getDeclarations(node: Node, context: Context): List<Node>? {
    val typeScriptService = context.requireService(typeScriptServiceKey)
    val typeChecker = typeScriptService.program.getTypeChecker()

    var symbol = typeChecker.getSymbolAtLocation(node) ?: return null

    if (SymbolFlags.Alias in symbol.flags) {
        symbol = typeChecker.getAliasedSymbol(symbol)
    }

    return symbol.declarations?.flatMap {
        if (isTypeAliasDeclaration(it)) {
            val type = it.type

            if (isTypeReferenceNode(type)) {
                getDeclarations(type.typeName, context) ?: return null
            } else {
                listOf(it)
            }
        } else {
            listOf(it)
        }
    }
}

private fun isJsPlainObject(node: Node, context: AnnotationContext): Boolean {
    if (
        isInterfaceDeclaration(node)
        && node.members.asArray().all(::isPropertySignature)
        && node.heritageClauses
            ?.asArray()
            ?.flatMap { it.types.asArray().asIterable() }
            ?.all { type ->
                val declarations = getDeclarations(type.expression, context)

                declarations != null && declarations.all { isJsPlainObject(it, context) }
            }
        ?: true
    ) {
        return true
    }

    if (isTypeAliasDeclaration(node)) {
        return isJsPlainObject(node.type, context)
    }

    if (
        isTypeLiteralNode(node)
        && node.members.asArray().all(::isPropertySignature)
    ) {
        return true
    }

    if (isIntersectionTypeNode(node)) {
        return node.types.asArray().all { type ->
            if (isTypeReferenceNode(type)) {
                val declarations = getDeclarations(type.typeName, context)

                declarations != null && declarations.all { isJsPlainObject(it, context) }
            } else {
                isJsPlainObject(type, context)
            }
        }
    }

    return false
}

@JsExport
fun createJsPlainObjectAnnotation(configuration: JsPlainObjectAnnotationConfiguration): JsAnnotation {
    return JsPlainObjectAnnotation(
        isJsPlainObject = configuration.isJsPlainObject?.wrap(),
        ignore = configuration.ignore?.wrap(),
    ).toJsExtension()
}

fun JsPlainObjectAnnotation(): Annotation =
    JsPlainObjectAnnotation(ignore = emptyList())

fun JsPlainObjectAnnotation(
    isJsPlainObject: (suspend (Node, AnnotationContext) -> Boolean)? = null,
    ignore: (suspend (Node, AnnotationContext) -> Boolean)? = null,
): Annotation {
    return JsPlainObjectAnnotation(
        isJsPlainObject = isJsPlainObject?.let { match(it) },
        ignore = ignore?.let { match(it) }
    )
}

fun JsPlainObjectAnnotation(
    isJsPlainObject: List<Matcher<AnnotationContext>>? = null,
    ignore: List<Matcher<AnnotationContext>>? = null,
): Annotation {
    val isJsPlainObjectMatchers = isJsPlainObject
        ?: match(::isJsPlainObject)

    val ignoreMatchers = ignore ?: emptyList()

    return annotation@{ node, context ->
        if (!isJsPlainObjectMatchers.matches(node, context)) return@annotation null
        if (ignoreMatchers.matches(node, context)) return@annotation null

        if (
            isInterfaceDeclaration(node)
            || isTypeAliasDeclaration(node)
            || context.isAnonymousDeclaration
        ) {
            "@kotlinx.js.JsPlainObject"
        } else {
            null
        }
    }
}
