package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import io.github.sgrishchenko.karakum.extension.ifPresent
import typescript.isTypeParameterDeclaration

val convertTypeParameterDeclaration = createPlugin plugin@{ node, context, render ->
    if (!isTypeParameterDeclaration(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    val varianceModifierService = context.lookupService<VarianceModifierService>(varianceModifierServiceKey)

    val name = render(node.name)

    val varianceModifier = varianceModifierService?.resolveVarianceModifier(node, context)
    val constraintType = node.constraint?.let { render(it) }
    val defaultType = node.default?.let { render(it) }

    "${ifPresent(varianceModifier) { "$it "}}${name}${ifPresent(constraintType) { " : $it"}}${ifPresent(defaultType) { " /* default is $it */"}}"
}
