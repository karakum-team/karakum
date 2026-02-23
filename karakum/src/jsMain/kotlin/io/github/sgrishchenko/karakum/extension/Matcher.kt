package io.github.sgrishchenko.karakum.extension

import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.NamedDeclaration
import typescript.Node
import typescript.isIdentifier

interface Matcher {
    val predicate: (Node) -> Boolean
    val children: List<Matcher>
}

private class MatcherImpl(
    override val predicate: (Node) -> Boolean,
    override val children: List<Matcher>,
) : Matcher

private class MatcherDelegateImpl(
    override val predicate: (Node) -> Boolean,
    val childrenProvider: () -> List<Matcher>,
) : Matcher {
    override val children: List<Matcher>
        get() = childrenProvider()
}

interface MatcherScope {
    val children: MutableList<Matcher>
}

private class MatcherScopeImpl(
    override val children: MutableList<Matcher> = mutableListOf(),
) : MatcherScope

fun MatcherScope.match(
    predicate: (Node) -> Boolean,
    block: MatcherScope.() -> Unit = { },
) {
    val scope = MatcherScopeImpl().also(block)
    val matcher = MatcherImpl(predicate, scope.children)
    children.add(matcher)
}

fun MatcherScope.match(predicate: (Node) -> Boolean): MatcherScope {
    val scope = MatcherScopeImpl()
    val matcher = MatcherDelegateImpl(predicate, scope::children)
    children.add(matcher)
    return scope
}

private fun ((Node) -> Boolean).withName(name: String): (Node) -> Boolean {
    return { node ->
        @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
        this(node) && (node as NamedDeclaration).name.let { it != null && isIdentifier(it) && it.text == name }
    }
}

fun MatcherScope.match(
    predicate: (Node) -> Boolean,
    name: String,
    block: MatcherScope.() -> Unit,
) {
    match(predicate.withName(name), block)
}

fun MatcherScope.match(
    predicate: (Node) -> Boolean,
    name: String
): MatcherScope {
    return match(predicate.withName(name))
}

fun match(
    block: MatcherScope.() -> Unit,
): List<Matcher> {
    val scope = MatcherScopeImpl().also(block)
    return scope.children
}

private fun Matcher.toPredicateChains(): List<List<(Node) -> Boolean>> {
    if (children.isEmpty()) return listOf(listOf(predicate))
    return children.flatMap { child -> child.toPredicateChains().map { it + predicate } }
}

fun Matcher.matches(node: Node, context: Context): Boolean {
    val predicateChains = toPredicateChains()

    val typeScriptService = context.lookupService(typeScriptServiceKey)

    predicateChains@ for (predicateChain in predicateChains) {
        val firstPredicate = predicateChain.firstOrNull() ?: return true
        if (!firstPredicate(node)) continue@predicateChains

        var current = node

        for (predicate in predicateChain.drop(1)) {
            current = typeScriptService?.getParent(current)
                ?: current.getParentOrNull()
                        ?: continue@predicateChains

            if (!predicate(current)) continue@predicateChains
        }

        return true
    }

    return false
}
