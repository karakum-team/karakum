package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.configuration.`object`
import io.github.sgrishchenko.karakum.configuration.`package`
import io.github.sgrishchenko.karakum.extension.createPlugin
import typescript.NodeFlags
import typescript.isVariableDeclaration

val convertVariableDeclaration = createPlugin plugin@{ node, context, render ->
    if (!isVariableDeclaration(node)) return@plugin null

    val checkCoverageService = context.lookupService(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    // skip initializer
    node.initializer?.let { checkCoverageService?.cover(it) }

    val commentService = context.lookupService(commentServiceKey)
    val typeScriptService = context.lookupService(typeScriptServiceKey)
    val namespaceInfoService = context.lookupService(namespaceInfoServiceKey)

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
        namespace == null
        || namespaceInfoService?.resolveNamespaceStrategy(namespace) == NamespaceStrategy.`package`
    ) {
        leadingComment = commentService?.renderLeadingComments(node.parent) ?: ""
    }

    val type = node.type
        ?.let { render(it) }
        ?: "Any? /* should be inferred */" // TODO: infer types

    "${leadingComment}${externalModifier}${modifier}${name}: $type"
}
