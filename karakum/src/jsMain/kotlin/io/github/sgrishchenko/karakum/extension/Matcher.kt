package io.github.sgrishchenko.karakum.extension

import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.getSourceFileOrNull
import node.path.path
import typescript.NamedDeclaration
import typescript.Node
import typescript.isIdentifier

interface Matcher<in TContext : Context> {
    val predicate: (Node, TContext) -> Boolean
    val children: List<Matcher<TContext>>
}

private class MatcherImpl<in TContext : Context>(
    override val predicate: (Node, TContext) -> Boolean,
    override val children: List<Matcher<TContext>>,
) : Matcher<TContext>

private class MatcherDelegateImpl<in TContext : Context>(
    override val predicate: (Node, TContext) -> Boolean,
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
    vararg predicates: (Node, TContext) -> Boolean,
): (Node, TContext) -> Boolean {
    return { node, context -> predicates.all { it(node, context) } }
}

private fun <TContext : Context> ((Node) -> Boolean).wrap(): (Node, TContext) -> Boolean {
    return { node, _ -> invoke(node) }
}

fun <TContext : Context> MatcherScope<TContext>.match(
    predicate: (Node, TContext) -> Boolean,
    vararg predicates: (Node, TContext) -> Boolean,
    block: MatcherScope<TContext>.() -> Unit = { },
) {
    val scope = MatcherScopeImpl<TContext>().also(block)
    val matcher = MatcherImpl(all(predicate, *predicates), scope.children)
    children.add(matcher)
}

fun <TContext : Context> MatcherScope<TContext>.match(
    predicate: (Node) -> Boolean,
    vararg predicates: (Node, TContext) -> Boolean,
    block: MatcherScope<TContext>.() -> Unit = { },
) {
    match(predicate.wrap(), *predicates, block = block)
}

fun <TContext : Context> MatcherScope<TContext>.match(
    predicate: (Node, TContext) -> Boolean,
): MatcherScope<TContext> {
    return match(predicate, predicates = emptyArray())
}

fun <TContext : Context> MatcherScope<TContext>.match(
    predicate: (Node, TContext) -> Boolean,
    vararg predicates: (Node, TContext) -> Boolean,
): MatcherScope<TContext> {
    val scope = MatcherScopeImpl<TContext>()
    val matcher = MatcherDelegateImpl(all(predicate, *predicates), scope::children)
    children.add(matcher)
    return scope
}

fun <TContext : Context> MatcherScope<TContext>.match(
    predicate: (Node) -> Boolean,
): MatcherScope<TContext> {
    return match(predicate, predicates = emptyArray())
}

fun <TContext : Context> MatcherScope<TContext>.match(
    predicate: (Node) -> Boolean,
    vararg predicates: (Node, TContext) -> Boolean,
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
): (Node, TContext) -> String? {
    return { node, context ->
        matchers.firstNotNullOfOrNull { (result, matcher) ->
            result.takeIf { matcher.matches(node, context) }
        }
    }
}

fun <TContext : Context> match(
    predicate: (Node, TContext) -> Boolean,
): List<Matcher<TContext>> {
    return match { match(predicate) }
}

private fun <TContext : Context> Matcher<TContext>.toPredicateChains(): List<List<(Node, TContext) -> Boolean>> {
    if (children.isEmpty()) return listOf(listOf(predicate))
    return children.flatMap { child -> child.toPredicateChains().map { it + predicate } }
}

fun <TContext : Context> Matcher<TContext>.matches(node: Node, context: TContext): Boolean {
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

fun <TContext : Context> Iterable<Matcher<TContext>>.matches(node: Node, context: TContext): Boolean {
    return any { it.matches(node, context) }
}
