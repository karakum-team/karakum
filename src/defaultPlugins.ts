import ts, {Node, Program} from "typescript";
import {Configuration} from "./configuration/configuration.js";
import {ConverterPlugin} from "./converter/plugin.js";
import {NameResolver} from "./converter/nameResolver.js";
import {ConfigurationPlugin} from "./converter/plugins/ConfigurationPlugin.js";
import {CheckKindsPlugin} from "./converter/plugins/CheckKindsPlugin.js";
import {CheckCoveragePlugin} from "./converter/plugins/CheckCoveragePlugin.js";
import {NullableUnionTypePlugin} from "./converter/plugins/NullableUnionTypePlugin.js";
import {convertPrimitive} from "./converter/plugins/convertPrimitive.js";
import {convertVoid} from "./converter/plugins/convertVoid.js";
import {typeLiteralPlugin} from "./converter/plugins/TypeLiteralPlugin.js";
import {convertModuleDeclaration} from "./converter/plugins/convertModuleDeclaration.js";
import {convertModuleBlock} from "./converter/plugins/convertModuleBlock.js";
import {convertInterfaceDeclaration} from "./converter/plugins/convertInterfaceDeclaration.js";
import {convertClassDeclaration} from "./converter/plugins/convertClassDeclaration.js";
import {convertTypeParameterDeclaration} from "./converter/plugins/convertTypeParameterDeclaration.js";
import {convertParameterDeclaration} from "./converter/plugins/convertParameterDeclaration.js";
import {convertTypeReference} from "./converter/plugins/convertTypeReference.js";
import {convertHeritageClause} from "./converter/plugins/convertHeritageClause.js";
import {convertExpressionWithTypeArguments} from "./converter/plugins/convertExpressionWithTypeArguments.js";
import {convertPropertySignature} from "./converter/plugins/convertPropertySignature.js";
import {convertPropertyDeclaration} from "./converter/plugins/convertPropertyDeclaration.js";
import {convertMethodSignature} from "./converter/plugins/convertMethodSignature.js";
import {convertMethodDeclaration} from "./converter/plugins/convertMethodDeclaration.js";
import {convertConstructorDeclaration} from "./converter/plugins/convertConstructorDeclaration.js";
import {convertFunctionType} from "./converter/plugins/convertFunctionType.js";
import {convertLiteralType} from "./converter/plugins/convertLiteralType.js";
import {convertArrayType} from "./converter/plugins/convertArrayType.js";
import {convertTypeAliasDeclaration} from "./converter/plugins/convertTypeAliasDeclaration.js";
import {convertEnumDeclaration} from "./converter/plugins/convertEnumDeclaration.js";
import {convertEnumMember} from "./converter/plugins/convertEnumMember.js";
import {convertVariableStatement} from "./converter/plugins/convertVariableStatement.js";
import {convertVariableDeclaration} from "./converter/plugins/convertVariableDeclaration.js";
import {convertQualifiedName} from "./converter/plugins/convertQualifiedName.js";
import {convertParenthesizedType} from "./converter/plugins/convertParenthesizedType.js";
import {convertUnionType} from "./converter/plugins/convertUnionType.js";
import {convertIntersectionType} from "./converter/plugins/convertIntersectionType.js";
import {convertIndexedAccessType} from "./converter/plugins/convertIndexedAccessType.js";
import {convertCallSignatureDeclaration} from "./converter/plugins/convertCallSignatureDeclaration.js";
import {convertFunctionDeclaration} from "./converter/plugins/convertFunctionDeclaration.js";
import {convertTypePredicate} from "./converter/plugins/convertTypePredicate.js";
import {TypeScriptPlugin} from "./converter/plugins/TypeScriptPlugin.js";
import {convertTypeOperator} from "./converter/plugins/convertTypeOperator.js";
import {convertImportType} from "./converter/plugins/convertImportType.js";
import {convertPropertyAccessExpression} from "./converter/plugins/convertPropertyAccessExpression.js";
import {NameResolverPlugin} from "./converter/plugins/NameResolverPlugin.js";
import {InheritanceModifierPlugin} from "./converter/plugins/InheritanceModifierPlugin.js";
import {InheritanceModifier} from "./converter/inheritanceModifier.js";
import {mappedTypePlugin} from "./converter/plugins/MappedTypePlugin.js";
import {NamespaceInfoPlugin} from "./converter/plugins/NamespaceInfoPlugin.js";
import {NamespaceInfo} from "./structure/namespace/collectNamespaceInfo.js";
import {AccessorsPlugin} from "./converter/plugins/AccessorsPlugin.js";
import {convertIndexedSignatureDeclaration} from "./converter/plugins/convertIndexedSignatureDeclaration.js";
import {DeclarationMergingPlugin} from "./converter/plugins/DeclarationMergingPlugin.js";
import {convertTypeQuery} from "./converter/plugins/convertTypeQuery.js";
import {literalUnionTypePlugin} from "./converter/plugins/LiteralUnionTypePlugin.js";
import {convertLiteral} from "./converter/plugins/convertLiteral.js";
import {inheritedTypeLiteralPlugin} from "./converter/plugins/InheritedTypeLiteralPlugin.js";
import {Injection} from "./converter/injection.js";
import {InjectionPlugin} from "./converter/plugins/InjectionPlugin.js";
import {convertConditionalType} from "./converter/plugins/convertConditionalType.js";
import {convertNamedTupleMember} from "./converter/plugins/convertNamedTupleMember.js";
import {ImportInfo} from "./structure/import/collectImportInfo.js";
import {ImportInfoPlugin} from "./converter/plugins/ImportInfoPlugin.js";
import {VarianceModifierPlugin} from "./converter/plugins/VarianceModifierPlugin.js";
import {VarianceModifier} from "./converter/varianceModifier.js";
import {convertMemberName} from "./converter/plugins/convertMemberName.js";

const hasKind = (kind: ts.SyntaxKind) => (node: Node) => node.kind === kind

export const createPlugins = (
    configuration: Configuration,
    injections: Injection[],
    nameResolvers: NameResolver[],
    inheritanceModifiers: InheritanceModifier[],
    varianceModifiers: VarianceModifier[],
    program: Program,
    namespaceInfo: NamespaceInfo,
    importInfo: ImportInfo,
): ConverterPlugin[] => [
    new ConfigurationPlugin(configuration),
    new TypeScriptPlugin(program),
    new InjectionPlugin(injections),
    new NameResolverPlugin(nameResolvers),
    new InheritanceModifierPlugin(inheritanceModifiers),
    new VarianceModifierPlugin(varianceModifiers),
    new NamespaceInfoPlugin(namespaceInfo),
    new ImportInfoPlugin(program, importInfo),
    new DeclarationMergingPlugin(program),
    new CheckKindsPlugin(),
    new CheckCoveragePlugin(),

    new NullableUnionTypePlugin(),
    new AccessorsPlugin(),

    typeLiteralPlugin,
    mappedTypePlugin,
    literalUnionTypePlugin,
    inheritedTypeLiteralPlugin,

    convertPrimitive(hasKind(ts.SyntaxKind.AnyKeyword), () => "Any?"),
    convertPrimitive(hasKind(ts.SyntaxKind.UnknownKeyword), () => "Any?"),
    convertPrimitive(hasKind(ts.SyntaxKind.UndefinedKeyword), () => "Nothing?"),
    convertPrimitive(hasKind(ts.SyntaxKind.NullKeyword), () => "Nothing?"),
    convertPrimitive(hasKind(ts.SyntaxKind.ObjectKeyword), () => "Any"),
    convertPrimitive(hasKind(ts.SyntaxKind.StringKeyword), () => "String"),
    convertPrimitive(hasKind(ts.SyntaxKind.NumberKeyword), () => "Double"),
    convertPrimitive(hasKind(ts.SyntaxKind.BooleanKeyword), () => "Boolean"),
    convertPrimitive(hasKind(ts.SyntaxKind.FalseKeyword), () => "Boolean /* false */"),
    convertPrimitive(hasKind(ts.SyntaxKind.TrueKeyword), () => "Boolean /* true */"),
    convertPrimitive(hasKind(ts.SyntaxKind.NeverKeyword), () => "Nothing"),
    convertPrimitive(hasKind(ts.SyntaxKind.SymbolKeyword), () => "js.symbol.Symbol"),
    convertPrimitive(hasKind(ts.SyntaxKind.BigIntKeyword), () => "js.core.BigInt"),

    convertPrimitive(ts.isIdentifier, node => node.text),

    convertMemberName, // should be applied before literals

    convertLiteral(ts.isStringLiteral, () => "String"),
    convertLiteral(ts.isNumericLiteral, () => "Double"),
    convertLiteral(ts.isBigIntLiteral, () => "js.core.BigInt"),

    convertLiteral(ts.isThisTypeNode, () => "Unit"),
    convertLiteral(ts.isTemplateLiteralTypeNode, () => "String"),

    convertVoid,
    convertModuleDeclaration,
    convertModuleBlock,
    convertInterfaceDeclaration,
    convertClassDeclaration,
    convertTypeParameterDeclaration,
    convertParameterDeclaration,
    convertTypeReference,
    convertHeritageClause,
    convertExpressionWithTypeArguments,
    convertPropertySignature,
    convertPropertyDeclaration,
    convertMethodSignature,
    convertMethodDeclaration,
    convertConstructorDeclaration,
    convertFunctionType,
    convertLiteralType,
    convertArrayType,
    convertTypeAliasDeclaration,
    convertEnumDeclaration,
    convertEnumMember,
    convertVariableStatement,
    convertVariableDeclaration,
    convertQualifiedName,
    convertPropertyAccessExpression,
    convertParenthesizedType,
    convertUnionType,
    convertIntersectionType,
    convertIndexedAccessType,
    convertCallSignatureDeclaration,
    convertFunctionDeclaration,
    convertTypePredicate,
    convertTypeOperator,
    convertImportType,
    convertIndexedSignatureDeclaration,
    convertTypeQuery,
    convertConditionalType,
    convertNamedTupleMember,
]
