package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.extension.InjectionType
import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import io.github.sgrishchenko.karakum.extension.ifPresent
import typescript.Node
import typescript.asArray
import typescript.isEnumDeclaration

val convertEnumDeclaration = createSimplePlugin plugin@{ node: Node, context, render ->
    if (!isEnumDeclaration(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    val declarationMergingService = context.lookupService<DeclarationMergingService>(declarationMergingServiceKey)
    if (declarationMergingService?.isCovered(node) == true) return@plugin ""
    declarationMergingService?.cover(node)

    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    val namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)
    val injectionService = context.lookupService<InjectionService>(injectionServiceKey)

    val name = render(node.name)

    val heritageInjections = injectionService?.resolveInjections(node, InjectionType.HERITAGE_CLAUSE, context, render)

    val namespace = typeScriptService?.findClosestNamespace(node)

    var externalModifier = "external "

    if (namespace != null && namespaceInfoService?.resolveNamespaceStrategy(namespace) == NamespaceStrategy.`object`) {
        externalModifier = ""
    }

    val injectedHeritageClauses = heritageInjections
        ?.filter { it.isNotEmpty() }
        ?.joinToString(separator = ", ")

    val members = (
            declarationMergingService
                ?.getMembers(node) { namespaceInfoService?.resolveNamespaceStrategy(it) }
                ?: node.members.asArray()
            )
        .joinToString(separator = ", ") { member -> "${render(member)}: $name" }

    """
sealed ${externalModifier}interface ${name}${ifPresent(injectedHeritageClauses) { " : $it" }} {
companion object {
$members
}
}
    """.trim()
}
