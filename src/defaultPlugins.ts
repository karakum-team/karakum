import ts, {Node, Program, SyntaxKind} from "typescript";
import {Configuration} from "./configuration/configuration";
import {ConverterPlugin} from "./converter/plugin";
import {NameResolver} from "./converter/nameResolver";
import {ConfigurationPlugin} from "./converter/plugins/ConfigurationPlugin";
import {CheckKindsPlugin} from "./converter/plugins/CheckKindsPlugin";
import {CheckCoveragePlugin} from "./converter/plugins/CheckCoveragePlugin";
import {NullableUnionTypePlugin} from "./converter/plugins/NullableUnionTypePlugin";
import {convertPrimitive} from "./converter/plugins/convertPrimitive";
import {createTypeLiteralPlugin} from "./converter/plugins/TypeLiteralPlugin";
import {convertModuleDeclaration} from "./converter/plugins/convertModuleDeclaration";
import {convertModuleBlock} from "./converter/plugins/convertModuleBlock";
import {convertInterfaceDeclaration} from "./converter/plugins/convertInterfaceDeclaration";
import {convertClassDeclaration} from "./converter/plugins/convertClassDeclaration";
import {convertTypeParameterDeclaration} from "./converter/plugins/convertTypeParameterDeclaration";
import {convertParameterDeclaration} from "./converter/plugins/convertParameterDeclaration";
import {convertTypeReference} from "./converter/plugins/convertTypeReference";
import {convertHeritageClause} from "./converter/plugins/convertHeritageClause";
import {convertExpressionWithTypeArguments} from "./converter/plugins/convertExpressionWithTypeArguments";
import {convertPropertySignature} from "./converter/plugins/convertPropertySignature";
import {convertPropertyDeclaration} from "./converter/plugins/convertPropertyDeclaration";
import {convertMethodSignature} from "./converter/plugins/convertMethodSignature";
import {convertMethodDeclaration} from "./converter/plugins/convertMethodDeclaration";
import {convertConstructorDeclaration} from "./converter/plugins/convertConstructorDeclaration";
import {convertFunctionType} from "./converter/plugins/convertFunctionType";
import {convertLiteralType} from "./converter/plugins/convertLiteralType";
import {convertArrayType} from "./converter/plugins/convertArrayType";
import {convertReadonlyArrayType} from "./converter/plugins/convertReadonlyArrayType";
import {convertTypeAliasDeclaration} from "./converter/plugins/convertTypeAliasDeclaration";
import {convertEnumDeclaration} from "./converter/plugins/convertEnumDeclaration";
import {convertEnumMember} from "./converter/plugins/convertEnumMember";
import {convertVariableStatement} from "./converter/plugins/convertVariableStatement";
import {convertVariableDeclaration} from "./converter/plugins/convertVariableDeclaration";
import {convertQualifiedName} from "./converter/plugins/convertQualifiedName";
import {convertPrefixUnaryExpression} from "./converter/plugins/convertPrefixUnaryExpression";
import {convertParenthesizedType} from "./converter/plugins/convertParenthesizedType";
import {convertUnionType} from "./converter/plugins/convertUnionType";
import {convertIntersectionType} from "./converter/plugins/convertIntersectionType";
import {convertIndexedAccessType} from "./converter/plugins/convertIndexedAccessType";
import {convertCallSignatureDeclaration} from "./converter/plugins/convertCallSignatureDeclaration";
import {convertFunctionDeclaration} from "./converter/plugins/convertFunctionDeclaration";
import {convertTypePredicate} from "./converter/plugins/convertTypePredicate";
import {TypeScriptPlugin} from "./converter/plugins/TypeScriptPlugin";
import {convertTypeOperator} from "./converter/plugins/convertTypeOperator";
import {convertImportType} from "./converter/plugins/convertImportType";
import {convertPropertyAccessExpression} from "./converter/plugins/convertPropertyAccessExpression";
import {InheritanceModifierPlugin} from "./converter/plugins/InheritanceModifierPlugin";
import {InheritanceModifier} from "./converter/inheritanceModifier";
import {convertMappedType} from "./converter/plugins/convertMappedType";
import {NamespaceInfoPlugin} from "./converter/plugins/NamespaceInfoPlugin";
import {NamespaceInfo} from "./structure/namespace/collectNamespaceInfo";
import {AccessorsPlugin} from "./converter/plugins/AccessorsPlugin";
import {convertIndexedSignatureDeclaration} from "./converter/plugins/convertIndexedSignatureDeclaration";
import {DeclarationMergingPlugin} from "./converter/plugins/DeclarationMergingPlugin";
import {convertTypeQuery} from "./converter/plugins/convertTypeQuery";
import {createStringUnionTypePlugin} from "./converter/plugins/StringUnionTypePlugin";

const hasKind = (kind: SyntaxKind) => (node: Node) => node.kind === kind

export const createPlugins = (
    configuration: Configuration,
    nameResolvers: NameResolver[],
    inheritanceModifiers: InheritanceModifier[],
    program: Program,
    namespaceInfo: NamespaceInfo,
): ConverterPlugin[] => [
    new ConfigurationPlugin(configuration),
    new TypeScriptPlugin(program),
    new InheritanceModifierPlugin(inheritanceModifiers),
    new NamespaceInfoPlugin(namespaceInfo),
    new DeclarationMergingPlugin(program),
    new CheckKindsPlugin(),
    new CheckCoveragePlugin(),

    new NullableUnionTypePlugin(),
    new AccessorsPlugin(),

    createTypeLiteralPlugin(nameResolvers),
    createStringUnionTypePlugin(nameResolvers),

    convertPrimitive(hasKind(SyntaxKind.AnyKeyword), () => "Any?"),
    convertPrimitive(hasKind(SyntaxKind.UnknownKeyword), () => "Any?"),
    convertPrimitive(hasKind(SyntaxKind.UndefinedKeyword), () => "Nothing?"),
    convertPrimitive(hasKind(SyntaxKind.NullKeyword), () => "Nothing?"),
    convertPrimitive(hasKind(SyntaxKind.ObjectKeyword), () => "Any"),
    convertPrimitive(hasKind(SyntaxKind.StringKeyword), () => "String"),
    convertPrimitive(hasKind(SyntaxKind.NumberKeyword), () => "Double"),
    convertPrimitive(hasKind(SyntaxKind.BooleanKeyword), () => "Boolean"),
    convertPrimitive(hasKind(SyntaxKind.FalseKeyword), () => "Boolean"),
    convertPrimitive(hasKind(SyntaxKind.TrueKeyword), () => "Boolean"),
    convertPrimitive(hasKind(SyntaxKind.VoidKeyword), () => "Unit"),
    convertPrimitive(hasKind(SyntaxKind.NeverKeyword), () => "Nothing"),
    convertPrimitive(hasKind(SyntaxKind.SymbolKeyword), () => "js.core.Symbol"),

    convertPrimitive(ts.isIdentifier, node => node.text),
    convertPrimitive(ts.isStringLiteral, () => "String"),
    convertPrimitive(ts.isNumericLiteral, () => "Double"),
    convertPrimitive(ts.isThisTypeNode, () => "Unit"),

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
    convertReadonlyArrayType,
    convertTypeAliasDeclaration,
    convertEnumDeclaration,
    convertEnumMember,
    convertVariableStatement,
    convertVariableDeclaration,
    convertQualifiedName,
    convertPropertyAccessExpression,
    convertPrefixUnaryExpression,
    convertParenthesizedType,
    convertUnionType,
    convertIntersectionType,
    convertIndexedAccessType,
    convertCallSignatureDeclaration,
    convertFunctionDeclaration,
    convertTypePredicate,
    convertTypeOperator,
    convertImportType,
    convertMappedType,
    convertIndexedSignatureDeclaration,
    convertTypeQuery,
]
