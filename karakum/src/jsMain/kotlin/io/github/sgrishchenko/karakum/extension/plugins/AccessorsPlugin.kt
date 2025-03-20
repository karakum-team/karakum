package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.util.escapeIdentifier
import js.objects.JsPlainObject
import typescript.*

@JsPlainObject
private external interface AccessorInfo {
    val getter: GetAccessorDeclaration?
    val setter: SetAccessorDeclaration?
}

private fun emptyAccessorInfo() = AccessorInfo(
    getter = null,
    setter = null,
)

class AccessorsPlugin : Plugin<Node> {
    private val accessors = mutableMapOf<Symbol, AccessorInfo>()
    private val coveredAccessors = mutableSetOf<Symbol>()

    override fun setup(context: Context) = Unit

    override fun traverse(node: Node, context: Context) {
        if (isGetAccessor(node) || isSetAccessor(node)) {
            val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
            val typeChecker = typeScriptService?.program?.getTypeChecker()

            val name = when {
                isGetAccessor(node) -> node.name
                isSetAccessor(node) -> node.name
                else -> return
            }

            val symbol = typeChecker?.getSymbolAtLocation(name)
            if (symbol == null) return

            var accessorInfo = accessors[symbol] ?: emptyAccessorInfo()

            if (isGetAccessor(node)) {
                accessorInfo = accessorInfo.copy(getter = node)
            }

            if (isSetAccessor(node)) {
                accessorInfo = accessorInfo.copy(setter = node)
            }

            accessors[symbol] = accessorInfo
        }
    }

    override fun render(node: Node, context: Context, next: Render<Node>): String? {
        if (isSetAccessor(node) || isGetAccessor(node)) {
            val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
            val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
            val inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)

            val inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)

            val typeChecker = typeScriptService?.program?.getTypeChecker()

            val accessorName = when {
                isGetAccessor(node) -> node.name
                isSetAccessor(node) -> node.name
                else -> return null
            }

            val symbol = typeChecker?.getSymbolAtLocation(accessorName)
            if (symbol == null) return null

            if (symbol in this.coveredAccessors) {
                checkCoverageService?.deepCover(node)
                return ""
            }

            checkCoverageService?.cover(node)

            if (isSetAccessor(node)) {
                checkCoverageService?.deepCover(node.parameters.asArray()[0])
            }

            this.coveredAccessors.add(symbol)

            val accessorInfo = this.accessors[symbol] ?: emptyAccessorInfo()

            val modifier = if (accessorInfo.setter != null) "var " else "val "

            val name = escapeIdentifier(next(accessorName))
            val annotation = createKebabAnnotation(accessorName)

            val getterType = accessorInfo.getter?.type
            val setterType = accessorInfo.setter?.parameters?.asArray()?.get(0)?.type

            val type = if (getterType != null) {
                next(getterType)
            } else if (setterType != null) {
                next(setterType)
            } else {
                "Any? /* type isn't declared */"
            }

            return """
${ifPresent(annotation) { "${it}\n" }}${ifPresent(inheritanceModifier) { "$it "}}${modifier}${name}: $type
            """.trim();
        }

        return null
    }

    override fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
}
