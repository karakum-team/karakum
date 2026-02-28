package io.github.sgrishchenko.karakum.extension.mutabilityModifiers

import io.github.sgrishchenko.karakum.extension.Context
import typescript.Node
import typescript.isGetAccessor
import typescript.isIdentifier
import typescript.isPropertyDeclaration
import typescript.isPropertySignature
import typescript.isVariableDeclaration

fun modifyCustomMutability(node: Node, context: Context): String? {
    if (isPropertySignature(node)) {
        val name = node.name
        if (!isIdentifier(name)) return null
        if (name.text != "customMutability") return null

        return "val"
    }

    if (isPropertyDeclaration(node)) {
        val name = node.name
        if (!isIdentifier(name)) return null
        if (name.text != "customMutability") return null

        return "val"
    }

    if (isGetAccessor(node)) {
        val name = node.name
        if (!isIdentifier(name)) return null
        if (name.text != "customMutability2") return null

        return "val"
    }

    if (isVariableDeclaration(node)) {
        val name = node.name
        if (!isIdentifier(name)) return null
        if (name.text != "customMutability") return null

        return "val"
    }

    return null
}
