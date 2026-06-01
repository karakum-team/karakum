package io.github.sgrishchenko.karakum.extension

import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.currentAbortable
import io.github.sgrishchenko.karakum.util.getSourceFileOrNull
import js.promise.Promise
import js.promise.await
import node.path.path
import typescript.NamedDeclaration
import typescript.Node
import typescript.isIdentifier
import web.abort.Abortable

interface Matcher<in TContext : Context> {
    val predicate: suspend (Node, TContext) -> Boolean
    val children: List<Matcher<TContext>>
}

private class MatcherImpl<in TContext : Context>(
    override val predicate: suspend (Node, TContext) -> Boolean,
    override val children: List<Matcher<TContext>>,
) : Matcher<TContext>

private class MatcherDelegateImpl<in TContext : Context>(
    override val predicate: suspend (Node, TContext) -> Boolean,
    val childrenProvider: () -> List<Matcher<TContext>>,
) : Matcher<TContext> {
    override val children: List<Matcher<TContext>>
        get() = childrenProvider()
}

interface MatcherScope<TContext : Context> {
    val children: MutableList<Matcher<TContext>>
}

private class MatcherScopeImpl<TContext : Context>(
    override val children: MutableList<Matcher<TContext>> = mutableListOf(),
) : MatcherScope<TContext>

private fun <TContext : Context> all(
    vararg predicates: suspend (Node, TContext) -> Boolean,
): suspend (Node, TContext) -> Boolean {
    return { node, context -> predicates.all { it(node, context) } }
}

private fun <TContext : Context> (suspend (Node) -> Boolean).wrap(): suspend (Node, TContext) -> Boolean {
    return { node, _ -> invoke(node) }
}

fun <TContext : Context> MatcherScope<TContext>.match(
    predicate: suspend (Node, TContext) -> Boolean,
    vararg predicates: suspend (Node, TContext) -> Boolean,
    block: MatcherScope<TContext>.() -> Unit = { },
) {
    val scope = MatcherScopeImpl<TContext>().also(block)
    val matcher = MatcherImpl(all(predicate, *predicates), scope.children)
    children.add(matcher)
}

fun <TContext : Context> MatcherScope<TContext>.match(
    predicate: suspend (Node) -> Boolean,
    vararg predicates: suspend (Node, TContext) -> Boolean,
    block: MatcherScope<TContext>.() -> Unit = { },
) {
    match(predicate.wrap(), *predicates, block = block)
}

fun <TContext : Context> MatcherScope<TContext>.match(
    predicate: suspend (Node, TContext) -> Boolean,
): MatcherScope<TContext> {
    return match(predicate, predicates = emptyArray())
}

fun <TContext : Context> MatcherScope<TContext>.match(
    predicate: suspend (Node, TContext) -> Boolean,
    vararg predicates: suspend (Node, TContext) -> Boolean,
): MatcherScope<TContext> {
    val scope = MatcherScopeImpl<TContext>()
    val matcher = MatcherDelegateImpl(all(predicate, *predicates), scope::children)
    children.add(matcher)
    return scope
}

fun <TContext : Context> MatcherScope<TContext>.match(
    predicate: suspend (Node) -> Boolean,
): MatcherScope<TContext> {
    return match(predicate, predicates = emptyArray())
}

fun <TContext : Context> MatcherScope<TContext>.match(
    predicate: suspend (Node) -> Boolean,
    vararg predicates: suspend (Node, TContext) -> Boolean,
): MatcherScope<TContext> {
    return match(predicate.wrap(), *predicates)
}

fun withName(name: String): (Node, Any?) -> Boolean {
    return { node, _ ->
        @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
        (node as NamedDeclaration).name.let { it != null && isIdentifier(it) && it.text == name }
    }
}

fun withFile(glob: String): (Node, Any?) -> Boolean {
    return { node, _ ->
        node.getSourceFileOrNull().let { it != null && path.matchesGlob(it.fileName, glob) }
    }
}

fun <TContext : Context> match(
    block: MatcherScope<TContext>.() -> Unit,
): List<Matcher<TContext>> {
    val scope = MatcherScopeImpl<TContext>().also(block)
    return scope.children
}

fun <TContext : Context> resolve(
    vararg matchers: Pair<String, List<Matcher<TContext>>>,
): suspend (Node, TContext) -> String? {
    return { node, context ->
        matchers.firstNotNullOfOrNull { (result, matcher) ->
            result.takeIf { matcher.matches(node, context) }
        }
    }
}

fun <TContext : Context> match(
    predicate: suspend (Node, TContext) -> Boolean,
): List<Matcher<TContext>> {
    return match { match(predicate) }
}

private fun <TContext : Context> Matcher<TContext>.toPredicateChains(): List<List<suspend (Node, TContext) -> Boolean>> {
    if (children.isEmpty()) return listOf(listOf(predicate))
    return children.flatMap { child -> child.toPredicateChains().map { it + predicate } }
}

suspend fun <TContext : Context> Matcher<TContext>.matches(node: Node, context: TContext): Boolean {
    val predicateChains = toPredicateChains()

    val typeScriptService = context.lookupService(typeScriptServiceKey)

    predicateChains@ for (predicateChain in predicateChains) {
        val firstPredicate = predicateChain.firstOrNull() ?: return true
        if (!firstPredicate(node, context)) continue@predicateChains

        var current = node

        for (predicate in predicateChain.drop(1)) {
            current = typeScriptService?.getParent(current) ?: continue@predicateChains

            if (!predicate(current, context)) continue@predicateChains
        }

        return true
    }

    return false
}

suspend fun <TContext : Context> Iterable<Matcher<TContext>>.matches(node: Node, context: TContext): Boolean {
    return any { it.matches(node, context) }
}

internal fun <TContext : Context> ((Node, TContext, Abortable) -> Promise<Boolean>).wrap(): suspend (Node, TContext) -> Boolean {
    return { node, context ->
        this(node, context, currentAbortable()).await()
    }
}
