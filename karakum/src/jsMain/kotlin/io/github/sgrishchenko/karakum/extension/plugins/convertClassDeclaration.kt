package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.extension.Context
import io.github.sgrishchenko.karakum.extension.InjectionType
import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import io.github.sgrishchenko.karakum.extension.ifPresent
import js.array.ReadonlyArray
import typescript.*

private fun extractModifiers(member: Declaration): ReadonlyArray<ModifierLike> {
    if (isPropertyDeclaration(member)) return member.modifiers?.asArray() ?: emptyArray()
    if (isMethodDeclaration(member)) return member.modifiers?.asArray() ?: emptyArray()
    if (isConstructorDeclaration(member)) return member.modifiers?.asArray() ?: emptyArray()
    if (isGetAccessorDeclaration(member)) return member.modifiers?.asArray() ?: emptyArray()
    if (isSetAccessorDeclaration(member)) return member.modifiers?.asArray() ?: emptyArray()
    if (isIndexSignatureDeclaration(member)) return member.modifiers?.asArray() ?: emptyArray()

    return emptyArray()
}

private fun resolveConstructors(
    node: ClassDeclaration,
    members: ReadonlyArray<Declaration>,
    context: Context,
): ReadonlyArray<Declaration> {
    val constructors = members.filter { isConstructorDeclaration(it) }
    if (constructors.isNotEmpty()) return constructors.toTypedArray()

    if (node.heritageClauses == null) return emptyArray()

    val heritageClause = node.heritageClauses?.asArray()?.find { it.token == SyntaxKind.ExtendsKeyword }
    if (heritageClause == null) return emptyArray()

    val parentReference = heritageClause.types.asArray()[0].expression

    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    val typeChecker = typeScriptService?.program?.getTypeChecker()
    val parentSymbol = typeChecker?.getSymbolAtLocation(parentReference)
    if (parentSymbol == null) return emptyArray()

    val parentDeclaration = parentSymbol.valueDeclaration
    if (parentDeclaration == null || !isClassDeclaration(parentDeclaration)) return emptyArray()

    val declarationMergingService = context.lookupService<DeclarationMergingService>(declarationMergingServiceKey)
    val namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)

    val mergedMembers = declarationMergingService
        ?.getMembers(parentDeclaration) { namespaceInfoService?.resolveNamespaceStrategy(it) }
        ?: parentDeclaration.members.asArray()

    return resolveConstructors(parentDeclaration, mergedMembers, context)
}

val convertClassDeclaration = createSimplePlugin plugin@{ node: Node, context, render ->
    if (!isClassDeclaration(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    val declarationMergingService = context.lookupService<DeclarationMergingService>(declarationMergingServiceKey)
    declarationMergingService?.cover(node)

    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    val namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)
    val inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)
    val injectionService = context.lookupService<InjectionService>(injectionServiceKey)

    val exportModifier = node.modifiers?.asArray()?.find { it.kind === SyntaxKind.ExportKeyword }
    if (exportModifier != null) checkCoverageService?.cover(exportModifier)

    val declareModifier = node.modifiers?.asArray()?.find { it.kind === SyntaxKind.DeclareKeyword }
    if (declareModifier != null) checkCoverageService?.cover(declareModifier)

    val name = node.name?.let { render(it) } ?: "Anonymous"

    val inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)
    val injections = injectionService?.resolveInjections(node, InjectionType.MEMBER, context, render)
    val staticInjections = injectionService?.resolveInjections(node, InjectionType.STATIC_MEMBER, context, render)
    val heritageInjections = injectionService?.resolveInjections(node, InjectionType.HERITAGE_CLAUSE, context, render)

    val namespace = typeScriptService?.findClosestNamespace(node)

    var externalModifier = "external "

    if (namespace != null && namespaceInfoService?.resolveNamespaceStrategy(namespace) == NamespaceStrategy.`object`) {
        externalModifier = ""
    }

    val typeParameters = (declarationMergingService?.getTypeParameters(node) ?: node.typeParameters?.asArray())
        ?.map { render(it) }
        ?.filter { it.isNotEmpty() }
        ?.joinToString(separator = ", ")

    val heritageClauses = (declarationMergingService?.getHeritageClauses(node) ?: node.heritageClauses?.asArray())
        ?.map { render(it) }
        ?.filter { it.isNotEmpty() }
        ?.joinToString(separator = ", ")

    val injectedHeritageClauses = heritageInjections
        ?.filter { it.isNotEmpty() }
        ?.joinToString(separator = ", ")

    val fullHeritageClauses = arrayOf(heritageClauses ?: "", injectedHeritageClauses ?: "")
        .filter { it.isNotEmpty() }
        .joinToString(separator = ", ")

    val mergedMembers = declarationMergingService
        ?.getMembers(node) { namespaceInfoService?.resolveNamespaceStrategy(it) }
        ?: node.members.asArray()

    // cover private members
    mergedMembers
        .filter { member -> extractModifiers(member).any { it.kind == SyntaxKind.PrivateKeyword } }
        .forEach { member -> checkCoverageService?.cover(member) }

    val publicMembers = mergedMembers
        .filter { member -> extractModifiers(member).all { it.kind != SyntaxKind.PrivateKeyword } }

    val constructors = resolveConstructors(node, publicMembers.toTypedArray(), context)
    val otherMembers = publicMembers.filter { !isConstructorDeclaration(it) }

    val members = (constructors + otherMembers)
        .filter { member -> extractModifiers(member).all { it.kind != SyntaxKind.StaticKeyword } }
        .joinToString(separator = "\n") { render(it) }

    val staticMembers = otherMembers
        .filter { member -> extractModifiers(member).any { it.kind == SyntaxKind.StaticKeyword} }
        .joinToString(separator = "\n") { render(it) }

    val injectedMembers = (injections ?: emptyArray())
        .joinToString(separator = "\n")

    val staticInjectedMembers = (staticInjections ?: emptyArray())
        .joinToString(separator = "\n")

    var companionObject = ""

    if (staticMembers.isNotEmpty()) {
        companionObject = "\n" + """
        
companion object {
${staticMembers}${ifPresent(staticInjectedMembers) { "\n${it}" }}
}
        """.trim()
    }

    """
${ifPresent(inheritanceModifier) { "$it " }}${externalModifier}class ${name}${ifPresent(typeParameters) { "<${it}>"}}${ifPresent(fullHeritageClauses) { " : $it" }} {
${members}${ifPresent(injectedMembers) { "\n${it}" }}${companionObject}
}
    """.trim()
}
