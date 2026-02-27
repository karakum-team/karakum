package io.github.sgrishchenko.karakum.extension

import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import typescript.NamedDeclaration
import typescript.Node
import typescript.isIdentifier

interface Matcher<in TContext: Context> {
    val predicate: (Node, TContext) -> Boolean
    val children: List<Matcher<TContext>>
}

private class MatcherImpl<in TContext: Context>(
    override val predicate: (Node, TContext) -> Boolean,
    override val children: List<Matcher<TContext>>,
) : Matcher<TContext>

private class MatcherDelegateImpl<in TContext: Context>(
    override val predicate: (Node, TContext) -> Boolean,
    val childrenProvider: () -> List<Matcher<TContext>>,
) : Matcher<TContext> {
    override val children: List<Matcher<TContext>>
        get() = childrenProvider()
}

interface MatcherScope<TContext: Context> {
    val children: MutableList<Matcher<TContext>>
}

private class MatcherScopeImpl<TContext: Context>(
    override val children: MutableList<Matcher<TContext>> = mutableListOf(),
) : MatcherScope<TContext>

fun <TContext: Context> MatcherScope<TContext>.match(
    predicate: (Node, TContext) -> Boolean,
    block: MatcherScope<TContext>.() -> Unit = { },
) {
    val scope = MatcherScopeImpl<TContext>().also(block)
    val matcher = MatcherImpl(predicate, scope.children)
    children.add(matcher)
}

fun <TContext: Context> MatcherScope<TContext>.match(
    predicate: (Node) -> Boolean,
    block: MatcherScope<TContext>.() -> Unit = { },
) {
    val scope = MatcherScopeImpl<TContext>().also(block)
    val matcher = MatcherImpl({ node, _ -> predicate(node) }, scope.children)
    children.add(matcher)
}

fun <TContext: Context> MatcherScope<TContext>.match(
    predicate: (Node, TContext) -> Boolean
): MatcherScope<TContext> {
    val scope = MatcherScopeImpl<TContext>()
    val matcher = MatcherDelegateImpl(predicate, scope::children)
    children.add(matcher)
    return scope
}

fun <TContext: Context> MatcherScope<TContext>.match(
    predicate: (Node) -> Boolean
): MatcherScope<TContext> {
    val scope = MatcherScopeImpl<TContext>()
    val matcher = MatcherDelegateImpl({ node, _ -> predicate(node) }, scope::children)
    children.add(matcher)
    return scope
}

private fun <TContext: Context> ((Node, TContext) -> Boolean).withName(name: String): (Node, TContext) -> Boolean {
    return { node, context ->
        @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
        this(node, context) && (node as NamedDeclaration).name.let { it != null && isIdentifier(it) && it.text == name }
    }
}

fun <TContext: Context> MatcherScope<TContext>.match(
    predicate: (Node, TContext) -> Boolean,
    name: String,
    block: MatcherScope<TContext>.() -> Unit,
) {
    match(predicate.withName(name), block)
}

fun <TContext: Context> MatcherScope<TContext>.match(
    predicate: (Node) -> Boolean,
    name: String,
    block: MatcherScope<TContext>.() -> Unit,
) {
    match({ node: Node, _: TContext -> predicate(node) }.withName(name), block)
}

fun <TContext: Context> MatcherScope<TContext>.match(
    predicate: (Node, TContext) -> Boolean,
    name: String
): MatcherScope<TContext> {
    return match(predicate.withName(name))
}

fun <TContext: Context> MatcherScope<TContext>.match(
    predicate: (Node) -> Boolean,
    name: String
): MatcherScope<TContext> {
    return match({ node: Node, _: TContext -> predicate(node) }.withName(name))
}

fun <TContext: Context> match(
    block: MatcherScope<TContext>.() -> Unit,
): List<Matcher<TContext>> {
    val scope = MatcherScopeImpl<TContext>().also(block)
    return scope.children
}

private fun <TContext: Context> Matcher<TContext>.toPredicateChains(): List<List<(Node, TContext) -> Boolean>> {
    if (children.isEmpty()) return listOf(listOf(predicate))
    return children.flatMap { child -> child.toPredicateChains().map { it + predicate } }
}

fun <TContext: Context> Matcher<TContext>.matches(node: Node, context: TContext): Boolean {
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
