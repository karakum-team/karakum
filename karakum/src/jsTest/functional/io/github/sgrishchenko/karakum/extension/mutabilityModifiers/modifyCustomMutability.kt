package io.github.sgrishchenko.karakum.extension.mutabilityModifiers

import io.github.sgrishchenko.karakum.extension.Context
import js.reflect.invoke
import typescript.*

suspend fun modifyCustomMutability(node: Node, context: Context): String? {
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
