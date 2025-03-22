package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.extension.*
import typescript.*
import kotlin.contracts.contract

@Suppress("CANNOT_CHECK_FOR_EXTERNAL_INTERFACE")
fun isInheritedTypeLiteral(node: Node): Boolean {
    contract {
        returns(true) implies (node is IntersectionTypeNode)
    }

    return isIntersectionTypeNode(node) && node.types.asArray().all {
        isTypeReferenceNode(it)
                || isTypeLiteralNode(it)
                || isMappedTypeNode(it)
    }
}

fun convertInheritedTypeLiteral(
    node: IntersectionTypeNode,
    name: String,
    typeParameters: String?,
    isInlined: Boolean,
    context: Context,
    render: Render<Node>,
): String {
    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    val namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)
    val inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)
    val injectionService = context.lookupService<InjectionService>(injectionServiceKey)

    val inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)
    val injections = injectionService?.resolveInjections(node, InjectionType.MEMBER, context, render)
    val heritageInjections = injectionService?.resolveInjections(node, InjectionType.HERITAGE_CLAUSE, context, render)

    val namespace = typeScriptService?.findClosestNamespace(node)

    var externalModifier = "external "

    if (isInlined && namespace != null && namespaceInfoService?.resolveNamespaceStrategy(namespace) == NamespaceStrategy.`object`) {
        externalModifier = ""
    }

    val typeReferences = node.types.asArray().filter { isTypeReferenceNode(it) }
    val typeLiterals = node.types.asArray().filter { isTypeLiteralNode(it) }
    val mappedType = node.types.asArray().find { isMappedTypeNode(it) }

    val heritageTypes = typeReferences
        .map { render(it) }
        .filter { it.isNotEmpty() }
        .joinToString(separator = ", ")

    val injectedHeritageClauses = heritageInjections
        ?.filter { it.isNotEmpty() }
        ?.joinToString(separator = ", ")

    val fullHeritageClauses = arrayOf(heritageTypes, injectedHeritageClauses ?: "")
        .filter { it.isNotEmpty() }
        .joinToString(separator = ", ")

    val members = typeLiterals
        .mapNotNull {
            if (!isTypeLiteralNode(it)) return@mapNotNull null
            convertTypeLiteralBody(it, context, render)
        }
        .joinToString(separator = "\n")

    var accessors = ""

    if (mappedType != null && isMappedTypeNode(mappedType)) {
        accessors = convertMappedTypeBody(mappedType, context, render)
    }

    val injectedMembers = (injections ?: emptyArray()).joinToString(separator = "\n")

    return """
${ifPresent(inheritanceModifier) { "$it "}}${externalModifier}interface ${name}${ifPresent(typeParameters) { "<${it}> "}}${(ifPresent(fullHeritageClauses) { " : $it"})} {
${ifPresent(accessors) { "${it}\n" }}${members}${ifPresent(injectedMembers) { "\n${it}"}}
}
    """.trim()
}

fun createInheritedTypeLiteralPlugin() = createAnonymousDeclarationPlugin plugin@{ node, context, render ->
    if (!isInheritedTypeLiteral(node)) return@plugin null

    val name = context.resolveName(node)

    val typeParameters = extractTypeParameters(node, context)

    val declaration =
        convertInheritedTypeLiteral(node, name, renderDeclaration(typeParameters, render), false, context, render)

    val reference = "${name}${ifPresent(renderReference(typeParameters, render)) { "<${it}>" }}"

    AnonymousDeclaration(
        name = name,
        declaration = declaration,
        reference = reference
    )
}
