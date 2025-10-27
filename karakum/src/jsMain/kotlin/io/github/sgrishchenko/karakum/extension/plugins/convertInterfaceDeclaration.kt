package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.extension.InjectionType
import io.github.sgrishchenko.karakum.extension.createPlugin
import io.github.sgrishchenko.karakum.extension.ifPresent
import typescript.SyntaxKind
import typescript.asArray
import typescript.isInterfaceDeclaration

val convertInterfaceDeclaration = createPlugin plugin@{ node, context, render ->
    if (!isInterfaceDeclaration(node)) return@plugin null

    val checkCoverageService = context.lookupService(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    val declarationMergingService = context.lookupService(declarationMergingServiceKey)
    if (declarationMergingService?.isCovered(node) == true) return@plugin ""
    declarationMergingService?.cover(node)

    val typeScriptService = context.lookupService(typeScriptServiceKey)
    val namespaceInfoService = context.lookupService(namespaceInfoServiceKey)
    val inheritanceModifierService = context.lookupService(inheritanceModifierServiceKey)
    val injectionService = context.lookupService(injectionServiceKey)

    val exportModifier = node.modifiers?.asArray()?.find { it.kind === SyntaxKind.ExportKeyword }
    exportModifier?.let { checkCoverageService?.cover(exportModifier) }

    val name = render(node.name)

    val inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)
    val injections = injectionService?.resolveInjections(node, InjectionType.MEMBER, context, render)
    val heritageInjections = injectionService?.resolveInjections(node, InjectionType.HERITAGE_CLAUSE, context, render)

    val namespace = typeScriptService?.findClosestNamespace(node)

    var externalModifier = "external "

    if (namespace != null && namespaceInfoService?.resolveNamespaceStrategy(namespace) == NamespaceStrategy.`object`) {
        externalModifier = ""
    }

    val typeParameters = (declarationMergingService?.getTypeParameters(node) ?: node.typeParameters?.asArray())
        ?.map { render(it) }
        ?.filter { it.isNotEmpty() }
        ?.joinToString(separator = ", ")

    val heritageClauses = (declarationMergingService?.getHeritageClauses(node) ?: node.heritageClauses?.asArray())
        ?.map { render(it) }
        ?.filter { it.isNotEmpty() }
        ?.joinToString(separator = ", ")

    val injectedHeritageClauses = heritageInjections
        ?.filter { it.isNotEmpty() }
        ?.joinToString(separator = ", ")

    val fullHeritageClauses = arrayOf(heritageClauses ?: "", injectedHeritageClauses ?: "")
        .filter { it.isNotEmpty() }
        .joinToString(separator = ", ")

    val members = (
            declarationMergingService
                ?.getMembers(node, context)
                ?: node.members.asArray()
            )
        .joinToString(separator = "\n") { render(it) }

    val injectedMembers = (injections ?: emptyArray())
        .joinToString(separator = "\n")

    """
${ifPresent(inheritanceModifier) { "$it " }}${externalModifier}interface ${name}${ifPresent(typeParameters) { "<${it}>" }}${ifPresent(fullHeritageClauses) { " : $it"}} {
${members}${ifPresent(injectedMembers) { "\n${it}"}}
}
    """.trim()
}
