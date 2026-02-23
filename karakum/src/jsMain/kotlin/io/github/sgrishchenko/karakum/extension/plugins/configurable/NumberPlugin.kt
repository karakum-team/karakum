package io.github.sgrishchenko.karakum.extension.plugins.configurable

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.extension.plugins.checkCoverageServiceKey
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.getSourceFileOrNull
import js.array.ReadonlyArray
import js.array.component1
import js.array.component2
import js.array.tupleOf
import js.objects.Object
import js.objects.ReadonlyRecord
import js.objects.recordOf
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

@JsExport
@JsPlainObject
external interface NumberPluginConfiguration {
    val strategy: NumberPluginStrategy?
    val defaultNumberType: String?
    val matchers: ReadonlyRecord<String, (Node) -> Boolean>?
}

@JsExport
class NumberPlugin(configuration: NumberPluginConfiguration) : Plugin {
    private val strategy = configuration.strategy ?: NumberPluginStrategy.loose
    private val defaultNumberType = configuration.defaultNumberType ?: "Double"
    private lateinit var matchers: List<Pair<String, List<Matcher>>>

    private val uncoveredNodes = mutableSetOf<Node>()

    @JsExport.Ignore
    constructor(
        strategy: NumberPluginStrategy? = NumberPluginStrategy.loose,
        defaultNumberType: String? = null,
        vararg matchers: Pair<String, (Node) -> Boolean>,
    ) : this(
        NumberPluginConfiguration(
            strategy,
            defaultNumberType,
            Object.fromEntries(
                matchers
                    .map { tupleOf(it.first, it.second) }
                    .toTypedArray()
            ),
        )
    )

    @JsExport.Ignore
    constructor(
        strategy: NumberPluginStrategy? = NumberPluginStrategy.loose,
        defaultNumberType: String? = null,
        vararg matchers: Pair<String, List<Matcher>>,
    ) : this(
        NumberPluginConfiguration(
            strategy,
            defaultNumberType,
            recordOf(),
        )
    ) {
        this.matchers = matchers.toList()
    }

    init {
        require(strategy == NumberPluginStrategy.loose || configuration.defaultNumberType == null) {
            "defaultNumberType can't be specified for strict strategy"
        }

        if (!::matchers.isInitialized) {
            matchers = Object.entries(configuration.matchers ?: recordOf())
                .map { (numberType, predicate) ->
                    numberType to match { match(predicate) }
                }
        }
    }

    override fun setup(context: Context) = Unit

    override fun traverse(node: Node, context: Context) = Unit

    override fun render(node: Node, context: Context, next: Render<Node>): String? {
        if (node.kind != SyntaxKind.NumberKeyword) return null

        val checkCoverageService = context.lookupService(checkCoverageServiceKey)
        checkCoverageService?.cover(node)

        val typeScriptService = context.lookupService(typeScriptServiceKey)

        val parent = typeScriptService?.getParent(node) ?: return null

        for ((numberType, matchers) in matchers) {
            for (matcher in matchers) {
                if (matcher.matches(parent, context)) return numberType
            }
        }

        if (strategy == NumberPluginStrategy.loose) return defaultNumberType

        uncoveredNodes += parent
        return null
    }

    override fun generate(context: Context, render: Render<Node>): ReadonlyArray<GeneratedFile> {
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
