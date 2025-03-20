package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.extension.Context
import io.github.sgrishchenko.karakum.extension.Plugin
import io.github.sgrishchenko.karakum.extension.GeneratedFile
import io.github.sgrishchenko.karakum.extension.Render
import io.github.sgrishchenko.karakum.util.DeepMap
import io.github.sgrishchenko.karakum.util.getSourceFileOrNull
import js.array.JsArrays
import js.array.ReadonlyArray
import js.objects.jso
import js.symbol.Symbol
import typescript.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalJsExport::class)
@JsExport
val declarationMergingServiceKey = Symbol()

@OptIn(ExperimentalJsExport::class)
@JsExport
class DeclarationMergingService @JsExport.Ignore constructor(private val program: Program) {
    private val coveredSymbols = mutableSetOf<typescript.Symbol>()
    private val virtualSourceFile = createSourceFile("virtual.d.ts", "", ScriptTarget.Latest)
    private val printer = createPrinter(jso {
        removeComments = true
        newLine = NewLineKind.LineFeed
    })

    fun isCovered(node: NamedDeclaration): Boolean {
        val symbol = this.getSymbol(node)
        if (symbol == null) return false

        if (symbol in this.coveredSymbols) return true

        val valueDeclaration = symbol.valueDeclaration

        // it is one of the additional declarations, and it will be covered in value declaration
        return valueDeclaration != null
            && isClassDeclaration(valueDeclaration)
            && node !== valueDeclaration
    }

    fun cover(node: NamedDeclaration) {
        val symbol = this.getSymbol(node) ?: return

        this.coveredSymbols.add(symbol)
    }

    fun getTypeParameters(node: NamedDeclaration): ReadonlyArray<TypeParameterDeclaration>? {
        val symbol = this.getSymbol(node)
        if (symbol == null) return null

        val declarationTypeParameters = symbol.declarations
            ?.mapNotNull {
                if (!isInterfaceDeclaration(it)) return@mapNotNull null
                it.typeParameters?.asArray()
            }

        val valueDeclaration = symbol.valueDeclaration
        val valueDeclarationTypeParameters =
            if (valueDeclaration != null && isClassDeclaration(valueDeclaration)) {
                valueDeclaration.typeParameters?.asArray()
            } else {
                null
            }

        val typeParameters =
            (declarationTypeParameters ?: emptyList()) +
                    listOf(valueDeclarationTypeParameters ?: emptyArray())

        return typeParameters.maxBy { it.size }
    }

    fun getHeritageClauses(node: NamedDeclaration): ReadonlyArray<HeritageClause>? {
        val symbol = this.getSymbol(node)
        if (symbol == null) return null

        val heritageClauses = symbol.declarations
            ?.mapNotNull {
                if (!isInterfaceDeclaration(it)) return@mapNotNull null
                it.heritageClauses?.asArray()
            }
            ?.flatMap { it.asIterable() }

        if (isClassDeclaration(node)) {
            val mainHeritageClauses = node.heritageClauses?.asArray()

            return (mainHeritageClauses ?: emptyArray()) +
                    (heritageClauses?.toTypedArray() ?: emptyArray())
        } else {
            return heritageClauses?.toTypedArray() ?: emptyArray()
        }
    }

    fun getMembers(
        node: NamedDeclaration,
        resolveNamespaceStrategy: (node: ModuleDeclaration) -> NamespaceStrategy?,
    ): ReadonlyArray<Declaration>? {
        val symbol = this.getSymbol(node)
        if (symbol == null) return null

        val members = this.getUniqMembers(symbol.members)
            .filter { member -> !isTypeParameterDeclaration(member) }

        val exports = this.getUniqMembers(symbol.exports)
            .filter { member -> !isTypeParameterDeclaration(member) }

        return (members + exports).filter { member ->
            val parent = member.parent

            if (
                isModuleBlock(parent)
                && resolveNamespaceStrategy(parent.parent) == NamespaceStrategy.`package`
            ) {
                return@filter false
            }

            if (isVariableDeclarationList(parent)) {
                val grandparent = parent.parent.parent

                if (
                    isModuleBlock(grandparent)
                    && resolveNamespaceStrategy(grandparent.parent) == NamespaceStrategy.`package`
                ) {
                    return@filter false
                }
            }

            return@filter true
        }.toTypedArray()
    }

    private fun getUniqMembers(symbolTable: SymbolTable?): ReadonlyArray<Declaration> {
        val typeChecker = this.program.getTypeChecker()

        return (symbolTable?.let { JsArrays.from(it.values()) } ?: emptyArray())
            .flatMap { symbol ->
                val declarations = symbol.declarations ?: emptyArray()
                val firstDeclaration = declarations.getOrNull(0) ?: return@flatMap emptyList()
                if (!isSignatureDeclaration(firstDeclaration)) return@flatMap listOf(firstDeclaration)

                val declarationMap = declarations.fold(DeepMap<Any, Declaration>()) { acc, declaration ->
                    if (!isSignatureDeclaration(declaration)) return@fold acc

                    val parameterSymbols = declaration.parameters.asArray()
                        .map { parameter ->
                            val typeNode = parameter.type ?: return@map "any"
                            typeChecker.getTypeFromTypeNode(typeNode).symbol.unsafeCast<typescript.Symbol?>()
                                ?: printNode(typeNode)
                        }
                        .toTypedArray()

                    acc[parameterSymbols] = declaration

                    acc
                }

                declarationMap.values().asIterable()
            }
            .toTypedArray()
    }

    @OptIn(ExperimentalContracts::class)
    @Suppress("CANNOT_CHECK_FOR_EXTERNAL_INTERFACE")
    private fun isSignatureDeclaration(declaration: Declaration): Boolean {
        contract {
            returns(true) implies (declaration is SignatureDeclarationBase)
        }

        return isCallSignatureDeclaration(declaration)
            || isConstructSignatureDeclaration(declaration)
            || isConstructorDeclaration(declaration)
            || isMethodSignature(declaration)
            || isMethodDeclaration(declaration)
    }

    private fun getSymbol(node: NamedDeclaration): typescript.Symbol? {
        val name = node.name ?: return null

        val typeChecker = program.getTypeChecker()
        return typeChecker.getSymbolAtLocation(name)
    }

    private fun printNode(node: Node): String {
        val sourceFile = node.getSourceFileOrNull() ?: virtualSourceFile

        return printer.printNode(EmitHint.Unspecified, node, sourceFile)
    }
}

class DeclarationMergingPlugin(program: Program) : Plugin<Node> {
    private val declarationMergingService = DeclarationMergingService(program)

    override fun setup(context: Context) {
        context.registerService(declarationMergingServiceKey, this.declarationMergingService)
    }

    override fun traverse(node: Node, context: Context) = Unit

    override fun render(node: Node, context: Context, next: Render<Node>) = null

    override fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
}
