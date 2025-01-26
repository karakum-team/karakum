package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.configuration.Granularity
import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.Node
import typescript.NodeFlags
import typescript.isVariableDeclaration

val convertVariableDeclaration = createSimplePlugin plugin@{ node: Node, context, render ->
    if (!isVariableDeclaration(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    // skip initializer
    node.initializer?.let { checkCoverageService?.cover(it) }

    val commentService = context.lookupService<CommentService>(commentServiceKey)
    val configurationService = context.lookupService<ConfigurationService>(configurationServiceKey)
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    val namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)

    val modifier = if (node.parent.flags.toString().toInt() and NodeFlags.Const.toString().toInt() != 0) {
        "val "
    } else {
        "var "
    }

    val name = render(node.name)

    val namespace = typeScriptService?.findClosestNamespace(node)

    var externalModifier = "external "

    if (namespace != null && namespaceInfoService?.resolveNamespaceStrategy(namespace) == NamespaceStrategy.`object`) {
        externalModifier = ""
    }

    var leadingComment = ""

    if (
        configurationService?.configuration?.granularity === Granularity.topLevel
        && (
            namespace == null
            || namespaceInfoService?.resolveNamespaceStrategy(namespace) == NamespaceStrategy.`package`
        )
    ) {
        leadingComment = commentService?.renderLeadingComments(node.parent) ?: ""
    }

    val type = node.type
        ?.let { render(it) }
        ?: "Any? /* should be inferred */" // TODO: infer types

    "${leadingComment}${externalModifier}${modifier}${name}: $type"
}
