package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.extension.*
import typescript.Node
import typescript.TypeLiteralNode
import typescript.asArray
import typescript.isTypeLiteralNode

fun convertTypeLiteralBody(node: TypeLiteralNode, context: ConverterContext, render: Render<Node>): String {
    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    val injectionService = context.lookupService<InjectionService>(injectionServiceKey)
    val injections = injectionService?.resolveInjections(node, InjectionType.MEMBER, context, render)

    val members = node.members.asArray().joinToString(separator = "\n") { render(it) }

    val injectedMembers = (injections ?: emptyArray()).joinToString(separator = "\n")

    return "${members}${ifPresent(injectedMembers) { "\n${it}" }}"
}

fun convertTypeLiteral(
    node: TypeLiteralNode,
    name: String,
    typeParameters: String?,
    isInlined: Boolean,
    context: ConverterContext,
    render: Render<Node>,
): String {
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    val namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)
    val inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)
    val inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)

    val injectionService = context.lookupService<InjectionService>(injectionServiceKey)
    val heritageInjections = injectionService?.resolveInjections(node, InjectionType.HERITAGE_CLAUSE, context, render)

    val namespace = typeScriptService?.findClosestNamespace(node)

    var externalModifier = "external "

    if (isInlined && namespace != null && namespaceInfoService?.resolveNamespaceStrategy(namespace) == NamespaceStrategy.`object`) {
        externalModifier = ""
    }

    val injectedHeritageClauses = heritageInjections
        ?.filter { it.isNotEmpty() }
        ?.joinToString(separator = ", ")

    return """
${ifPresent(inheritanceModifier) { "${it} "}}${externalModifier}interface ${name}${ifPresent(typeParameters) { "<${it}>"}}${(ifPresent(injectedHeritageClauses) { ": $it"})} {
${convertTypeLiteralBody(node, context, render)}
}
    """.trim()
}

val typeLiteralPlugin = createAnonymousDeclarationPlugin plugin@{ node, context, render ->
    if (!isTypeLiteralNode(node)) return@plugin null

    // handle empty type literal
    if (node.members.asArray().isEmpty()) return@plugin AnonymousDeclaration("Any")

    val name = context.resolveName(node)

    val typeParameters = extractTypeParameters(node, context)

    val declaration = convertTypeLiteral(node, name, renderDeclaration(typeParameters, render), false, context, render)

    val reference = "${name}${ifPresent(renderReference(typeParameters, render)) { "<${it}>" }}"

    AnonymousDeclaration(
        name = name,
        declaration = declaration,
        reference = reference
    )
}
