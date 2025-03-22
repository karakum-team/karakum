package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import io.github.sgrishchenko.karakum.extension.ifPresent
import io.github.sgrishchenko.karakum.extension.renderNullable
import typescript.SyntaxKind
import typescript.asArray
import typescript.isIndexSignatureDeclaration

val convertIndexedSignatureDeclaration = createPlugin plugin@{ node, context, render ->
    if (!isIndexSignatureDeclaration(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)

    checkCoverageService?.cover(node)

    val inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)

    val getterInheritanceModifier = inheritanceModifierService?.resolveGetterInheritanceModifier(node, context)
    val setterInheritanceModifier = inheritanceModifierService?.resolveSetterInheritanceModifier(node, context)

    val readonly = node.modifiers?.asArray()?.find { modifier -> modifier.kind == SyntaxKind.ReadonlyKeyword }
    readonly?.let { checkCoverageService?.cover(it) }

    val firstParameterType = node.parameters.asArray().getOrNull(0)?.type

    val keyType = if (firstParameterType != null) {
        render(firstParameterType)
    } else {
        "Any? /* type isn't declared */"
    }

    val type = renderNullable(node.type, true, context, render)

    val getter = """
@seskar.js.JsNativeGetter
${ifPresent(getterInheritanceModifier) { "$it "}}operator fun get(key: ${keyType}): $type
    """.trim()

    var setter = ""

    if (readonly == null) {
        setter = """
@seskar.js.JsNativeSetter
${ifPresent(setterInheritanceModifier) { "$it "}}operator fun set(key: ${keyType}, value: ${type})
        """.trim()
    }


    "${getter}${ifPresent(setter) { "\n\n${it}" }}"
}
