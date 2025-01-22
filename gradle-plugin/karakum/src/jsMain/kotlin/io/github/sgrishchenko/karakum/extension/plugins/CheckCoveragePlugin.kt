package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.ConverterContext
import io.github.sgrishchenko.karakum.extension.ConverterPlugin
import io.github.sgrishchenko.karakum.extension.GeneratedFile
import io.github.sgrishchenko.karakum.extension.Render
import io.github.sgrishchenko.karakum.util.getSourceFileOrNull
import io.github.sgrishchenko.karakum.util.traverse
import js.array.ReadonlyArray
import js.objects.JsPlainObject
import js.objects.ReadonlyRecord
import js.symbol.Symbol
import typescript.Node
import typescript.SyntaxKind

val checkCoverageServiceKey = Symbol()

@JsPlainObject
external interface CheckCoverageResult {
    val coveredNodes: Int
    val uncoveredNodes: Int
}

class CheckCoverageService {
    private val coveredNodes = mutableSetOf<Node>()

    private val allNodes = mutableSetOf<Node>()

    fun register(node: Node) {
        this.allNodes.add(node)
    }

    fun cover(node: Node) {
        this.coveredNodes.add(node)
    }

    fun deepCover(node: Node) {
        traverse(node) { cover(it) }
    }

    fun emit(callback: (uncoveredNode: Node) -> Unit): CheckCoverageResult {
        for (node in allNodes){
            if (node !in coveredNodes) {
                callback(node);
            }
        }

        val result = CheckCoverageResult(
            coveredNodes = coveredNodes.size,
            uncoveredNodes = allNodes.size - coveredNodes.size,
        )

        coveredNodes.clear()
        allNodes.clear()

        return result
    }
}

class CheckCoveragePlugin : ConverterPlugin<Node> {
    private val checkCoverageService = CheckCoverageService()

    override fun generate(context: ConverterContext, render: Render<Node>): ReadonlyArray<GeneratedFile> {
        val configurationService = context.lookupService<ConfigurationService>(configurationServiceKey)
        val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

        val result = this.checkCoverageService.emit{ uncoveredNode ->
            if (configurationService?.configuration?.verbose == true) {
                val syntaxKindRecord = SyntaxKind.unsafeCast<ReadonlyRecord<SyntaxKind, String>>()
                val message = "Node with kind ${syntaxKindRecord[uncoveredNode.kind]} is uncovered"
                val sourceFile = uncoveredNode.getSourceFileOrNull()

                if (sourceFile != null) {
                    val lineAndCharacter = sourceFile.getLineAndCharacterOfPosition(uncoveredNode.pos)
                    val line = lineAndCharacter.line
                    val character = lineAndCharacter.character

                    console.error("${sourceFile.fileName}: (${line + 1}, ${character + 1}): $message")
                } else {
                    console.error(message)
                }

                console.error("--- Node Start ---");
                console.error(typeScriptService?.printNode(uncoveredNode));
                console.error("--- Node End ---");

                console.error();
            }
        }

        console.log("Covered nodes: ${result.coveredNodes}")
        console.log("Uncovered nodes: ${result.uncoveredNodes}")

        return emptyArray()
    }

    override fun render(node: Node, context: ConverterContext, next: Render<Node>) = null

    override fun traverse(node: Node, context: ConverterContext) {
        this.checkCoverageService.register(node)
    }

    override fun setup(context: ConverterContext) {
        context.registerService(checkCoverageServiceKey, this.checkCoverageService)
    }
}
