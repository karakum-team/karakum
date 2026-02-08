package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.configuration.`object`
import io.github.sgrishchenko.karakum.configuration.`package`
import io.github.sgrishchenko.karakum.extension.Context
import io.github.sgrishchenko.karakum.extension.GeneratedFile
import io.github.sgrishchenko.karakum.extension.Plugin
import io.github.sgrishchenko.karakum.extension.Render
import io.github.sgrishchenko.karakum.extension.ifPresent
import io.github.sgrishchenko.karakum.structure.derived.DerivedDeclaration
import io.github.sgrishchenko.karakum.structure.derived.generateDerivedDeclarations
import io.github.sgrishchenko.karakum.util.getSourceFileOrNull
import js.array.ReadonlyArray
import typescript.*

class TypeAliasDeclarationPlugin : Plugin {
    private val generated = mutableListOf<DerivedDeclaration>()

    override fun setup(context: Context) = Unit

    override fun traverse(node: Node, context: Context) = Unit

    private fun resolveQualifiedName(node: TypeAliasDeclaration, context: Context, next: Render<Node>): String {
        val name = next(node.name)
        val result = mutableListOf<String>()

        val typeScriptService = context.lookupService(typeScriptServiceKey)
        val namespaceInfoService = context.lookupService(namespaceInfoServiceKey)

        typeScriptService?.findClosest(node) {
            if (!isModuleDeclaration(it)) return@findClosest false

            val namespaceStrategy = namespaceInfoService?.resolveNamespaceStrategy(it)

            if (namespaceStrategy == NamespaceStrategy.`object`) {
                result.add(0, next(it.name))
            }

            namespaceStrategy == NamespaceStrategy.`package`
        }

        return (result + name).joinToString(separator = ".")
    }

    override fun render(node: Node, context: Context, next: Render<Node>): String? {
        if (!isTypeAliasDeclaration(node)) return null

        val checkCoverageService = context.lookupService(checkCoverageServiceKey)
        checkCoverageService?.cover(node)

        val exportModifier = node.modifiers?.asArray()?.find { it.kind == SyntaxKind.ExportKeyword }
        exportModifier?.let { checkCoverageService?.cover(exportModifier) }

        val declareModifier = node.modifiers?.asArray()?.find { it.kind == SyntaxKind.DeclareKeyword }
        declareModifier?.let { checkCoverageService?.cover(declareModifier) }

        val name = next(node.name)

        val typeParameters = node.typeParameters?.asArray()
            ?.map { next(it) }
            ?.filter { it.isNotEmpty() }
            ?.joinToString(separator = ", ")

        val typeNode = node.type

        if (isTypeLiteralNode(typeNode)) {
            return convertTypeLiteral(typeNode, name, typeParameters, true, context, next)
        }

        if (isMappedTypeNode(typeNode)) {
            return convertMappedType(typeNode, name, typeParameters, true, context, next)
        }

        if (isLiteralUnionType(typeNode, context)) {
            val qualifiedName = resolveQualifiedName(node, context, next)

            val renderResult = convertLiteralUnionType(typeNode, name, qualifiedName, true, context, next)

            val typeScriptService = context.requireService(typeScriptServiceKey)

            val sourceFileName = node.getSourceFileOrNull()?.fileName ?: "generated.d.ts"
            val namespace = typeScriptService.findClosestNamespace(node)

            generated += DerivedDeclaration(
                sourceFileName = sourceFileName,
                namespace = namespace,
                fileName = "${name}.kt",
                body = renderResult.generated,
            )

            return renderResult.declaration
        }

        if (isInheritedTypeLiteral(typeNode)) {
            return convertInheritedTypeLiteral(typeNode, name, typeParameters, true, context, next)
        }

        if (isFunctionTypeNode(typeNode)) {
            val functionTypeParameters = typeNode.typeParameters

            if (functionTypeParameters != null) {

                checkCoverageService?.cover(typeNode)

                val mergedTypeParameters = (node.typeParameters?.asArray() ?: emptyArray())
                    .plus(functionTypeParameters.asArray())
                    .map { next(it) }
                    .filter { it.isNotEmpty() }
                    .joinToString(separator = ", ")

                val returnType = next(typeNode.type)

                val type = convertParameterDeclarations(
                    typeNode, context, next, ParameterDeclarationsConfiguration(
                        strategy = ParameterDeclarationStrategy.lambda,
                        template = { parameters, _ ->
                            "(${parameters}) -> $returnType"
                        }
                    ))

                return "typealias ${name}${ifPresent(mergedTypeParameters) { "<${it}>"}} = $type"
            }
        }

        val type = next(typeNode)

        return "typealias ${name}${ifPresent(typeParameters) { "<${it}>" }} = $type"
    }

    override fun generate(context: Context, render: Render<Node>): ReadonlyArray<GeneratedFile> {
        return generateDerivedDeclarations(generated.toTypedArray(), context)
    }
}
