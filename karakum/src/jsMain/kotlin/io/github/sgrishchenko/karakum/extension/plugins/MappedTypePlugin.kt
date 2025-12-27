package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.configuration.`object`
import io.github.sgrishchenko.karakum.extension.*
import typescript.MappedTypeNode
import typescript.Node
import typescript.SyntaxKind
import typescript.isMappedTypeNode

fun convertMappedTypeBody(node: MappedTypeNode, context: Context, render: Render<Node>): String {
    val checkCoverageService = context.lookupService(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    val inheritanceModifierService = context.lookupService(inheritanceModifierServiceKey)
    val getterInheritanceModifier = inheritanceModifierService?.resolveGetterInheritanceModifier(node, context)
    val setterInheritanceModifier = inheritanceModifierService?.resolveSetterInheritanceModifier(node, context)

    val injectionService = context.lookupService(injectionServiceKey)
    val injections = injectionService?.resolveInjections(node, InjectionType.MEMBER, context, render)

    val readonlyToken = node.readonlyToken
    val readonly = readonlyToken != null && readonlyToken.kind != SyntaxKind.MinusToken

    val typeParameter = render(node.typeParameter)

    val type = renderNullable(node.type, true, context, render)

    val keyType = node.nameType
        ?.let { render(it) }
        ?: node.typeParameter.name.text

    val getter = """
${ifPresent(getterInheritanceModifier) { "$it "}}operator fun <${typeParameter}> get(key: ${keyType}): $type
    """.trim()

    var setter = ""

    if (!readonly) {
        setter = """
${ifPresent(setterInheritanceModifier) { "$it "}}operator fun <${typeParameter}> set(key: ${keyType}, value: ${type})
        """.trim()
    }

    val injectedMembers = (injections ?: emptyArray()).joinToString(separator = "\n")

    return "${getter}${ifPresent(setter) { "\n\n${it}" }}${ifPresent(injectedMembers) { "\n${it}" }}"
}

fun convertMappedType (
    node: MappedTypeNode,
    name: String,
    typeParameters: String?,
    isInlined: Boolean,
    context: Context,
    render: Render<Node>,
): String {
    val typeScriptService = context.lookupService(typeScriptServiceKey)
    val namespaceInfoService = context.lookupService(namespaceInfoServiceKey)
    val inheritanceModifierService = context.lookupService(inheritanceModifierServiceKey)
    val inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)

    val injectionService = context.lookupService(injectionServiceKey)
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
${ifPresent(inheritanceModifier) { "$it " }}${externalModifier}interface ${name}${ifPresent(typeParameters) { "<${it}>" }}${(ifPresent(injectedHeritageClauses) { " : $it" })} {
${convertMappedTypeBody(node, context, render)}
}
    """.trim()
}

fun createMappedTypePlugin() = createAnonymousDeclarationPlugin plugin@{ node, context, render ->
    if (!isMappedTypeNode(node)) return@plugin null

    val name = context.resolveName(node)

    val typeParameters = extractTypeParameters(node, context)

    val declaration = convertMappedType(node, name, renderDeclaration(typeParameters, render), false, context, render)

    val reference = "${name}${ifPresent(renderReference(typeParameters, render)) { "<${it}>" }}"

    AnonymousDeclaration(
        name = name,
        declaration = declaration,
        reference = reference
    )
}
