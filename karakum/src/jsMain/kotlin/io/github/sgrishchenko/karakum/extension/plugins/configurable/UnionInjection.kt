package io.github.sgrishchenko.karakum.extension.plugins.configurable

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.extension.plugins.*
import io.github.sgrishchenko.karakum.util.getParentOrNull
import js.array.ReadonlyArray
import js.symbol.Symbol
import typescript.*

@OptIn(ExperimentalJsExport::class)
@JsExport
val unionServiceKey = Symbol()

@OptIn(ExperimentalJsExport::class)
@JsExport
class UnionService @JsExport.Ignore constructor(private val context: ConverterContext) {
    private val unionParents = mutableMapOf<typescript.Symbol, ReadonlyArray<String>>()
    private val coveredUnionParents = mutableSetOf<typescript.Symbol>()

    val uncoveredUnionParents
        get() = unionParents.filter { it.key !in coveredUnionParents }

    fun isCovered(node: NamedDeclaration): Boolean {
        val symbol = getSymbol(node)
        if (symbol == null) return false

        return symbol in coveredUnionParents
    }

    fun cover(node: NamedDeclaration) {
        val symbol = getSymbol(node)
        if (symbol == null) return

        coveredUnionParents.add(symbol)
    }

    fun register(union: UnionTypeNode, reference: TypeReferenceNode) {
        val nameResolverService = context.lookupService<NameResolverService>(nameResolverServiceKey)

        val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
        val typeChecker = typeScriptService?.program?.getTypeChecker()

        val symbol = typeChecker?.getSymbolAtLocation(reference.typeName)

        var name: String

        val parent = union.getParentOrNull()

        if (
            parent != null
            && isTypeAliasDeclaration(parent)
            && parent.type == union
        ) {
            name = parent.name.text
        } else {
            name = nameResolverService?.resolveName(union, context) ?: "Anonymous"
        }

        if (symbol != null) {
            val parentNames = unionParents[symbol] ?: emptyArray()

            unionParents[symbol] = parentNames + name
        }
    }

    fun getParents(node: NamedDeclaration): ReadonlyArray<String> {
        val symbol = getSymbol(node)
        if (symbol == null) return emptyArray()

        return unionParents[symbol] ?: emptyArray()
    }

    private fun getSymbol(node: NamedDeclaration): typescript.Symbol? {
        val name = node.name ?: return null

        val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
        val typeChecker = typeScriptService?.program?.getTypeChecker()
        return typeChecker?.getSymbolAtLocation(name)
    }
}

class UnionInjection : Injection<Node, Node> {
    private var unionService: UnionService? = null

    private val anonymousUnionDeclarationPlugin = createAnonymousDeclarationPlugin plugin@{ node, context, render ->
        if (
            isUnionTypeNode(node)
            && node.types.asArray().all { type -> isTypeReferenceNode(type) && type.typeArguments == null }
        ) {
            val typeParameters = extractTypeParameters(node, context)

            val renderedTypeParameters = renderDeclaration(typeParameters, render)

            val name = context.resolveName(node)

            val injectionService = context.lookupService<InjectionService>(injectionServiceKey)
            val heritageInjections =
                injectionService?.resolveInjections(node, InjectionType.HERITAGE_CLAUSE, context, render)

            val injectedHeritageClauses = heritageInjections
                ?.filter { it.isNotEmpty() }
                ?.joinToString(separator = ", ")

            // TODO: support template literals
            // TODO: support nullable unions
            val declaration = """
sealed external interface ${name}${ifPresent(renderedTypeParameters) { "<${it}>" }}${ifPresent(injectedHeritageClauses) { " : $it" }} {
}
            """.trim()

            val reference = "${name}${ifPresent(renderReference(typeParameters, render)) { "<${it}>" }}"

            AnonymousDeclaration(
                name = name,
                declaration = declaration,
                reference = reference
            )
        }

        null
    }

    override fun setup(context: ConverterContext) {
        unionService = UnionService(context)
        context.registerService(unionServiceKey, requireNotNull(unionService))
    }

    override fun traverse(node: Node, context: ConverterContext) {
        if (
            isUnionTypeNode(node)
            && node.types.asArray().all { type -> isTypeReferenceNode(type) && type.typeArguments == null }

            // ignore parameters because they are expanded as overloads
            && !isParameter(node.parent)
        ) {
            for (type in node.types.asArray()) {
                if (!isTypeReferenceNode(type)) continue

                unionService?.register(node, type)
            }
        }
    }

    override fun render(node: Node, context: ConverterContext, next: Render<Node>): String? {
        val anonymousUnionDeclaration = anonymousUnionDeclarationPlugin.render(node, context, next)
        if (anonymousUnionDeclaration != null) return anonymousUnionDeclaration

        if (isTypeAliasDeclaration(node)) {
            val typeNode = node.type

            if (
                isUnionTypeNode(typeNode)
                    && typeNode.types.asArray().all { type -> isTypeReferenceNode(type) && type.typeArguments == null }
            ) {
                val name = next(node.name)

                val typeParameters = node.typeParameters?.asArray()
                    ?.map { next(it) }
                    ?.filter { it.isNotEmpty() }
                    ?.joinToString(separator = ", ")

                val injectionService = context.lookupService<InjectionService>(injectionServiceKey)
                val heritageInjections =
                    injectionService?.resolveInjections(node.type, InjectionType.HERITAGE_CLAUSE, context, next)

                val injectedHeritageClauses = heritageInjections
                    ?.filter { it.isNotEmpty() }
                    ?.joinToString(separator = ", ")

                // TODO: support template literals
                // TODO: support nullable unions
                return """
sealed external interface ${name}${ifPresent(typeParameters) { "<${it}>" }}${ifPresent(injectedHeritageClauses) { " : $it" }} {
}
                """.trim()
            }
        }

        if (isTypeAliasDeclaration(node)) {
            val typeNode = node.type

            if (
                isTypeReferenceNode(typeNode)
                && typeNode.typeArguments == null
            ) {

                val name = next(node.name)

                val typeParameters = node.typeParameters?.asArray()
                    ?.map { next(it) }
                    ?.filter { it.isNotEmpty() }
                    ?.joinToString(separator = ", ")

                val type = next(node.type)

                val injectionService = context.lookupService<InjectionService>(injectionServiceKey)
                val heritageInjections =
                    injectionService?.resolveInjections(node.type, InjectionType.HERITAGE_CLAUSE, context, next)

                val injectedHeritageClauses = heritageInjections
                    ?.filter { it.isNotEmpty() }
                    ?.joinToString(separator = ", ")

                // TODO: invert logic, inherit type from typealias
                val fullHeritageClauses = arrayOf(type, injectedHeritageClauses ?: "")
                    .filter { it.isNotEmpty() }
                    .joinToString(separator = ", ")

                if (injectedHeritageClauses === "") return null

                // TODO: support template literals
                // TODO: support nullable unions
                return """
sealed external interface ${name}${ifPresent(typeParameters) { "<${it}>" }}${ifPresent(fullHeritageClauses) { " : ${it}" }} {
}
                """.trim()

            }
        }

        return null
    }

    override fun inject(node: Node, context: InjectionContext, render: Render<Node>): ReadonlyArray<String>? {
        if (context.type === InjectionType.HERITAGE_CLAUSE) {
            if (isClassDeclaration(node)) {
                val parentNames = unionService?.getParents(node) ?: emptyArray()
                unionService?.cover(node)

                return parentNames
            }

            if (isInterfaceDeclaration(node)) {
                val parentNames = unionService?.getParents(node) ?: emptyArray()
                unionService?.cover(node)

                return parentNames
            }

            if (isEnumDeclaration(node)) {
                val parentNames = unionService?.getParents(node) ?: emptyArray()
                unionService?.cover(node)

                return parentNames
            }

            if (isEnumMember(node)) {
                val parentNames = unionService?.getParents(node) ?: emptyArray()
                unionService?.cover(node)

                return parentNames
            }

            val parent = node.getParentOrNull()

            if (
                parent != null
                && isTypeAliasDeclaration(parent)
                && parent.type === node
            ) {
                val parentNames = unionService?.getParents(parent) ?: emptyArray()
                unionService?.cover(parent)

                return parentNames
            }
        }

        return null
    }

    override fun generate(context: ConverterContext, render: Render<Node>): ReadonlyArray<GeneratedFile> {
        val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

        for ((symbol, parentNames) in unionService?.uncoveredUnionParents ?: emptyMap()) {
            @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
            val firstDeclaration = symbol.declarations?.getOrNull(0) as NamedDeclaration?
            val name = firstDeclaration?.name
                ?.let { typeScriptService?.printNode(it) }
                ?: "Anonymous"

            console.log("Declaration $name was not handled by Union Injection.")
            console.log("Please, consider providing conversions for next types:")

            for (parentName in parentNames) {
                console.log("\t${name} -> $parentName")
            }
        }

        return anonymousUnionDeclarationPlugin.generate(context, render)
    }
}
