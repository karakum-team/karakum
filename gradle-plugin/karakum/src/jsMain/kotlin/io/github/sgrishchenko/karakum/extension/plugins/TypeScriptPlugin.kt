package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.ConverterContext
import io.github.sgrishchenko.karakum.extension.ConverterPlugin
import io.github.sgrishchenko.karakum.extension.GeneratedFile
import io.github.sgrishchenko.karakum.extension.Render
import io.github.sgrishchenko.karakum.util.getParentOrNull
import io.github.sgrishchenko.karakum.util.getSourceFileOrNull
import io.github.sgrishchenko.karakum.util.setParentNodes
import js.objects.jso
import js.symbol.Symbol
import typescript.*

val typeScriptServiceKey = Symbol()

class TypeScriptService(val program: Program) {
    private val virtualSourceFile = createSourceFile("virtual.d.ts", "", ScriptTarget.Latest)
    private val printer = createPrinter(jso {
        removeComments = true
        newLine = NewLineKind.LineFeed
    })
    private val virtualParents = mutableMapOf<Node, Node>()

    fun printNode(node: Node): String {
        val sourceFile = node.getSourceFileOrNull() ?: this.virtualSourceFile

        return printer.printNode(EmitHint.Unspecified, node, sourceFile)
    }

    fun getParent(node: Node): Node? {
        val realParent = node.getParentOrNull()
        if (realParent != null) return realParent

        return virtualParents[node]
    }

    fun findClosest(rootNode: Node?, predicate: (node: Node) -> Boolean): Node? {
        if (rootNode == null) return null
        if (predicate(rootNode)) return rootNode

        return findClosest(getParent(rootNode), predicate)
    }

    fun resolveType(node: TypeNode): Node? {
        val typeChecker = program.getTypeChecker()
        val type = typeChecker.getTypeAtLocation(node)
        val typeNode = typeChecker.typeToTypeNode(type, undefined, NodeBuilderFlags.NoTruncation)

        val parent = getParent(node)
        if (parent != null && typeNode != null) {
            this.virtualParents[typeNode] = parent
        }

        return typeNode?.also { setParentNodes(typeNode) }
    }
}

class TypeScriptPlugin(program: Program) : ConverterPlugin<Node> {
    private val typeScriptService = TypeScriptService(program)

    override fun generate(context: ConverterContext, render: Render<Node>) = emptyArray<GeneratedFile>()

    override fun render(node: Node, context: ConverterContext, next: Render<Node>) = null

    override fun traverse(node: Node, context: ConverterContext) = Unit

    override fun setup(context: ConverterContext) {
        context.registerService(typeScriptServiceKey, this.typeScriptService)
    }
}