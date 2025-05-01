package io.github.sgrishchenko.karakum.base.inheritanceModifiers

import io.github.sgrishchenko.karakum.extension.InheritanceModifier
import io.github.sgrishchenko.karakum.util.getSourceFileOrNull
import typescript.isClassDeclaration
import typescript.isIdentifier
import typescript.isPropertyDeclaration

val modifyPropertyInheritance: InheritanceModifier = inheritanceModifier@{ node, context ->
    val sourceFileName = node.getSourceFileOrNull()?.fileName ?: return@inheritanceModifier null

    if (!isPropertyDeclaration(node)) return@inheritanceModifier null

    val name = node.name
    val parent = node.parent

    if (!isIdentifier(name)) return@inheritanceModifier null
    if (!isClassDeclaration(parent)) return@inheritanceModifier null

    if (
        sourceFileName.endsWith("interface/inheritance.d.ts") &&
        parent.name?.text == "ChildClass" &&
        (name.text == "firstField" ||
                name.text == "secondField" ||
                name.text == "thirdField" ||
                name.text == "fourthField" ||
                name.text == "otherField")
    ) {
        return@inheritanceModifier "override"
    }

    null
}
