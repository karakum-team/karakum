package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.extension.createPlugin
import typescript.isModuleDeclaration

val convertModuleDeclaration = createPlugin plugin@{ node, context, render ->
    if (!isModuleDeclaration(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    val namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)

    val namespaceStrategy = namespaceInfoService?.resolveNamespaceStrategy(node)

    if (namespaceStrategy == NamespaceStrategy.ignore) {
        return@plugin node.body ?.let { render(it) } ?: ""
    }

    if (namespaceStrategy == NamespaceStrategy.`object`) {
        val name = render(node.name)

        val namespace = typeScriptService?.findClosestNamespace(node.parent)

        var externalModifier = "external "

        if (namespace != null && namespaceInfoService.resolveNamespaceStrategy(namespace) == NamespaceStrategy.`object`) {
            externalModifier = ""
        }

        val body = node.body?.let { render(it) } ?: ""

        return@plugin """
${externalModifier}object ${name} {
${body}
}
        """.trim()
    }

    if (namespaceStrategy == NamespaceStrategy.`package`) {
        // was handled by file structure
        return@plugin ""
    }

    error("Unknown namespace strategy: $namespaceStrategy")
}
