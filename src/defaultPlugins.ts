import ts, {Node, Program, SyntaxKind} from "typescript";
import {Configuration} from "./configuration/configuration";
import {ConverterPlugin} from "./converter/plugin";
import {NameResolver} from "./converter/nameResolver";
import {ConfigurationPlugin} from "./converter/plugins/ConfigurationPlugin";
import {CheckKindsPlugin} from "./converter/plugins/CheckKindsPlugin";
import {CheckCoveragePlugin} from "./converter/plugins/CheckCoveragePlugin";
import {NullableUnionTypePlugin} from "./converter/plugins/NullableUnionTypePlugin";
import {TypeLiteralPlugin} from "./converter/plugins/TypeLiteralPlugin";
import {convertPrimitive} from "./converter/plugins/convertPrimitive";
import {convertModuleDeclaration} from "./converter/plugins/convertModuleDeclaration";
import {convertModuleBlock} from "./converter/plugins/convertModuleBlock";
import {convertInterfaceDeclaration} from "./converter/plugins/convertInterfaceDeclaration";
import {convertClassDeclaration} from "./converter/plugins/convertClassDeclaration";
import {convertTypeParameterDeclaration} from "./converter/plugins/convertTypeParameterDeclaration";
import {convertParameterDeclaration} from "./converter/plugins/convertParameterDeclaration";
import {convertTypeReferenceNode} from "./converter/plugins/convertTypeReferenceNode";
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
import {convertIndexedAccessTypeNode} from "./converter/plugins/convertIndexedAccessTypeNode";
import {convertCallSignature} from "./converter/plugins/convertCallSignature";
import {convertFunctionDeclaration} from "./converter/plugins/convertFunctionDeclaration";
import {convertTypePredicate} from "./converter/plugins/convertTypePredicate";
import {TypeScriptPlugin} from "./converter/plugins/TypeScriptPlugin";
import {convertStringUnionTypeAliasDeclaration} from "./converter/plugins/convertStringUnionTypeAliasDeclaration";
import {convertTypeOperator} from "./converter/plugins/convertTypeOperator";
import {convertImportType} from "./converter/plugins/convertImportType";
import {convertPropertyAccessExpression} from "./converter/plugins/convertPropertyAccessExpression";

const hasKind = (kind: SyntaxKind) => (node: Node) => node.kind === kind

export const createPlugins = (
    sourceFileRoot: string,
    configuration: Configuration,
    nameResolvers: NameResolver[],
    program: Program,
): ConverterPlugin[] => [
    new ConfigurationPlugin(configuration),
    new TypeScriptPlugin(program),
    new CheckKindsPlugin(),
    new CheckCoveragePlugin(),

    new NullableUnionTypePlugin(),
    new TypeLiteralPlugin(sourceFileRoot, nameResolvers),

    convertPrimitive(hasKind(SyntaxKind.DeclareKeyword), () => ""),

    convertPrimitive(hasKind(SyntaxKind.AnyKeyword), () => "Any?"),
    convertPrimitive(hasKind(SyntaxKind.UnknownKeyword), () => "Any?"),
    convertPrimitive(hasKind(SyntaxKind.UndefinedKeyword), () => ""), // covered by nullability
    convertPrimitive(hasKind(SyntaxKind.NullKeyword), () => ""), // covered by nullability
    convertPrimitive(hasKind(SyntaxKind.ObjectKeyword), () => "Any"),
    convertPrimitive(hasKind(SyntaxKind.StringKeyword), () => "String"),
    convertPrimitive(hasKind(SyntaxKind.NumberKeyword), () => "Double"),
    convertPrimitive(hasKind(SyntaxKind.BooleanKeyword), () => "Boolean"),
    convertPrimitive(hasKind(SyntaxKind.FalseKeyword), () => "Boolean"),
    convertPrimitive(hasKind(SyntaxKind.TrueKeyword), () => "Boolean"),
    convertPrimitive(hasKind(SyntaxKind.VoidKeyword), () => "Unit"),
    convertPrimitive(hasKind(SyntaxKind.NeverKeyword), () => "Nothing"),

    convertPrimitive(ts.isIdentifier, node => node.text),
    convertPrimitive(ts.isStringLiteral, () => "String"),
    convertPrimitive(ts.isNumericLiteral, () => "Double"),

    convertModuleDeclaration,
    convertModuleBlock,
    convertInterfaceDeclaration,
    convertClassDeclaration,
    convertTypeParameterDeclaration,
    convertParameterDeclaration,
    convertTypeReferenceNode,
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
    convertStringUnionTypeAliasDeclaration,
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
    convertIndexedAccessTypeNode,
    convertCallSignature,
    convertFunctionDeclaration,
    convertTypePredicate,
    convertTypeOperator,
    convertImportType,
]
