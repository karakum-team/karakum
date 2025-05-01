package io.github.sgrishchenko.karakum.base.inheritanceModifiers

import io.github.sgrishchenko.karakum.extension.InheritanceModifier
import io.github.sgrishchenko.karakum.util.getSourceFileOrNull
import typescript.isClassDeclaration

val modifyClassInheritance: InheritanceModifier = inheritanceModifier@{ node, context ->
    val sourceFileName = node.getSourceFileOrNull()?.fileName ?: return@inheritanceModifier null

    if (!isClassDeclaration(node)) return@inheritanceModifier null

    if (
        sourceFileName.endsWith("class/parentConstructors.d.ts") &&
        (node.name?.text == "GrandparentWithConstructor" ||
                node.name?.text == "ParentWithoutConstructor" ||
                node.name?.text == "ParentWithConstructor")

    ) {
        return@inheritanceModifier "open"
    }

    if (
        sourceFileName.endsWith("interface/inheritance.d.ts") &&
        (node.name?.text == "ParentClass")

    ) {
        return@inheritanceModifier "open"
    }

    null
}
