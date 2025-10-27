package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import io.github.sgrishchenko.karakum.extension.ifPresent
import io.github.sgrishchenko.karakum.extension.renderNullable
import io.github.sgrishchenko.karakum.util.escapeIdentifier
import typescript.SyntaxKind
import typescript.asArray
import typescript.isPropertyDeclaration

val convertPropertyDeclaration = createPlugin plugin@{ node, context, render ->
    if (!isPropertyDeclaration(node)) return@plugin null

    val checkCoverageService = context.lookupService(checkCoverageServiceKey)

    checkCoverageService?.cover(node)

    val inheritanceModifierService = context.lookupService(inheritanceModifierServiceKey)

    val inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)

    val readonly = node.modifiers?.asArray()?.find { modifier -> modifier.kind === SyntaxKind.ReadonlyKeyword }
    readonly?.let { checkCoverageService?.cover(it) }

    node.questionToken?.let { checkCoverageService?.cover(it) }

    val modifier = if (readonly != null) "val " else "var "

    val name = escapeIdentifier(render(node.name))
    val annotation = createKebabAnnotation(node.name)

    val isOptional = node.questionToken != null

    val type = renderNullable(node.type, isOptional, context, render)

    "${ifPresent(annotation) { "${it}\n" }}${ifPresent(inheritanceModifier) { "$it "}}${modifier}${name}: $type"
}
