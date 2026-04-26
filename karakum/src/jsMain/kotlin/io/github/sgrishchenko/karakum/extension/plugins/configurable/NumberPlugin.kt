package io.github.sgrishchenko.karakum.extension.plugins.configurable

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.extension.plugins.checkCoverageServiceKey
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.getSourceFileOrNull
import js.array.ReadonlyArray
import js.array.component1
import js.array.component2
import js.objects.Object
import js.objects.ReadonlyRecord
import js.reflect.unsafeCast
import kotlinx.js.JsPlainObject
import typescript.Node
import typescript.SyntaxKind

sealed external interface NumberPluginStrategy {
    companion object
}

inline val NumberPluginStrategy.Companion.strict: NumberPluginStrategy
    get() = unsafeCast("strict")

inline val NumberPluginStrategy.Companion.loose: NumberPluginStrategy
    get() = unsafeCast("loose")

class NumberPlugin(
    private val strategy: NumberPluginStrategy = NumberPluginStrategy.loose,
    private val defaultNumberType: String = "Double",
    vararg matchers: Pair<String, List<Matcher<Context>>>,
) : Plugin {
    private val matchers = matchers.toList()

    private val uncoveredNodes = mutableSetOf<Node>()

    constructor(
        strategy: NumberPluginStrategy = NumberPluginStrategy.loose,
        defaultNumberType: String = "Double",
    ) : this(
        strategy,
        defaultNumberType,
        matchers = emptyArray<Pair<String, List<Matcher<Context>>>>(),
    )

    constructor(
        strategy: NumberPluginStrategy = NumberPluginStrategy.loose,
        defaultNumberType: String = "Double",
        vararg matchers: Pair<String, (Node, Context) -> Boolean>,
    ) : this(
        strategy,
        defaultNumberType,
        matchers = matchers
            .map { (numberType, predicate) ->
                numberType to match(predicate)
            }
            .toTypedArray()
    )

    override suspend fun setup(context: Context) = Unit

    override suspend fun traverse(node: Node, context: Context) = Unit

    override suspend fun render(node: Node, context: Context, next: Render<Node>): String? {
        if (node.kind != SyntaxKind.NumberKeyword) return null

        val checkCoverageService = context.lookupService(checkCoverageServiceKey)
        checkCoverageService?.cover(node)

        val typeScriptService = context.lookupService(typeScriptServiceKey)

        val parent = typeScriptService?.getParent(node) ?: return null

        for ((numberType, matchers) in matchers) {
            if (matchers.matches(parent, context)) return numberType
        }

        if (strategy == NumberPluginStrategy.loose) return defaultNumberType

        uncoveredNodes += parent
        return null
    }

    override suspend fun generate(context: Context, render: Render<Node>): ReadonlyArray<GeneratedFile> {
        val typeScriptService = context.lookupService(typeScriptServiceKey)

        for (uncoveredNode in uncoveredNodes) {
            val message = "Unresolved number type"
            val sourceFile = uncoveredNode.getSourceFileOrNull()

            if (sourceFile != null) {
                val lineAndCharacter = sourceFile.getLineAndCharacterOfPosition(uncoveredNode.pos)
                val line = lineAndCharacter.line
                val character = lineAndCharacter.character

                console.error("${sourceFile.fileName}: (${line + 1}, ${character + 1}): $message")
            } else {
                console.error(message)
            }

            console.error("--- Node Start ---")
            console.error(typeScriptService?.printNode(uncoveredNode))
            console.error("--- Node End ---")

            console.error()
        }

        return emptyArray()
    }
}

@JsExport
@JsPlainObject
external interface NumberPluginConfiguration {
    val strategy: NumberPluginStrategy?
    val defaultNumberType: String?
    val matchers: ReadonlyRecord<String, (Node, Context) -> Boolean>?
}

@JsExport
fun createNumberPlugin(configuration: NumberPluginConfiguration): JsPlugin =
    NumberPlugin(
        strategy = configuration.strategy ?: NumberPluginStrategy.loose,
        defaultNumberType = configuration.defaultNumberType ?: "Double",
        matchers = configuration.matchers
            ?.let { Object.entries(it) }
            ?.map { (key, value) -> key to value }
            ?.toTypedArray()
            ?: emptyArray(),
    ).toJsPlugin()
