package io.github.sgrishchenko.karakum

import io.github.sgrishchenko.karakum.configuration.Configuration
import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.extension.plugins.convertLiteral
import io.github.sgrishchenko.karakum.extension.plugins.convertMemberName
import io.github.sgrishchenko.karakum.extension.plugins.convertPrimitive
import io.github.sgrishchenko.karakum.structure.import.ImportInfo
import io.github.sgrishchenko.karakum.structure.namespace.NamespaceInfo
import js.array.ReadonlyArray
import typescript.*

private fun hasKind(kind: SyntaxKind): (node: Node) -> Boolean = { it.kind == kind }

fun createPlugins(
    configuration: Configuration,
    injections: ReadonlyArray<Injection<Node, Node>>,
    nameResolvers: ReadonlyArray<NameResolver<Node>>,
    inheritanceModifiers: ReadonlyArray<InheritanceModifier<Node>>,
    varianceModifiers: ReadonlyArray<VarianceModifier<Node>>,
    program: Program,
    namespaceInfo: NamespaceInfo,
    importInfo: ImportInfo,
): ReadonlyArray<ConverterPlugin<Node>> = arrayOf(
//    ConfigurationPlugin(configuration),
//    TypeScriptPlugin(program),
//    InjectionPlugin(injections),
//    NameResolverPlugin(nameResolvers),
//    InheritanceModifierPlugin(inheritanceModifiers),
//    VarianceModifierPlugin(varianceModifiers),
//    NamespaceInfoPlugin(namespaceInfo),
//    ImportInfoPlugin(program, importInfo),
//    DeclarationMergingPlugin(program),
//    CheckKindsPlugin(),
//    CheckCoveragePlugin(),
//
//    NullableUnionTypePlugin(),
//    AccessorsPlugin(),

//    typeLiteralPlugin,
//    mappedTypePlugin,
//    literalUnionTypePlugin,
//    inheritedTypeLiteralPlugin,

    convertPrimitive(hasKind(SyntaxKind.AnyKeyword)) { "Any?" },
    convertPrimitive(hasKind(SyntaxKind.UnknownKeyword)) { "Any?" },
    convertPrimitive(hasKind(SyntaxKind.UndefinedKeyword)) { "Nothing?" },
    convertPrimitive(hasKind(SyntaxKind.NullKeyword)) { "Nothing?" },
    convertPrimitive(hasKind(SyntaxKind.ObjectKeyword)) { "Any" },
    convertPrimitive(hasKind(SyntaxKind.StringKeyword)) { "String" },
    convertPrimitive(hasKind(SyntaxKind.NumberKeyword)) { "Double" },
    convertPrimitive(hasKind(SyntaxKind.BooleanKeyword)) { "Boolean" },
    convertPrimitive(hasKind(SyntaxKind.FalseKeyword)) { "Boolean /* false */" },
    convertPrimitive(hasKind(SyntaxKind.TrueKeyword)) { "Boolean /* true */" },
    convertPrimitive(hasKind(SyntaxKind.NeverKeyword)) { "Nothing" },
    convertPrimitive(hasKind(SyntaxKind.SymbolKeyword)) { "js.symbol.Symbol" },
    convertPrimitive(hasKind(SyntaxKind.BigIntKeyword)) { "js.core.BigInt" },

    createSimplePlugin { node: Node, _, _ -> if (isIdentifier(node)) node.text else null },

    convertMemberName, // should be applied before literals

    convertLiteral(::isStringLiteral) { "String" },
    convertLiteral(::isNumericLiteral) { "Double" },
    convertLiteral(::isBigIntLiteral) { "js.core.BigInt" },

    convertLiteral(::isThisTypeNode) { "Unit" },
    convertLiteral(::isTemplateLiteralTypeNode) { "String" },

//    convertVoid,
//    convertModuleDeclaration,
//    convertModuleBlock,
//    convertInterfaceDeclaration,
//    convertClassDeclaration,
//    convertTypeParameterDeclaration,
//    convertParameterDeclaration,
//    convertTypeReference,
//    convertHeritageClause,
//    convertExpressionWithTypeArguments,
//    convertPropertySignature,
//    convertPropertyDeclaration,
//    convertMethodSignature,
//    convertMethodDeclaration,
//    convertConstructorDeclaration,
//    convertFunctionType,
//    convertLiteralType,
//    convertArrayType,
//    convertTypeAliasDeclaration,
//    convertEnumDeclaration,
//    convertEnumMember,
//    convertVariableStatement,
//    convertVariableDeclaration,
//    convertQualifiedName,
//    convertPropertyAccessExpression,
//    convertParenthesizedType,
//    convertUnionType,
//    convertIntersectionType,
//    convertIndexedAccessType,
//    convertCallSignatureDeclaration,
//    convertFunctionDeclaration,
//    convertTypePredicate,
//    convertTypeOperator,
//    convertImportType,
//    convertIndexedSignatureDeclaration,
//    convertTypeQuery,
//    convertConditionalType,
//    convertNamedTupleMember,
)
