import ts, {EmitHint, Node, SourceFile, SyntaxKind} from "typescript"
import {ConverterPlugin} from "./plugin";
import {ConverterContext} from "./context";
import {convertPrimitive} from "./plugins/convertPrimitive";
import {convertInterfaceDeclaration} from "./plugins/convertInterfaceDeclaration";
import {convertTypeParameterDeclaration} from "./plugins/convertTypeParameterDeclaration";
import {convertParameterDeclaration} from "./plugins/convertParameterDeclaration";
import {convertTypeReferenceNode} from "./plugins/convertTypeReferenceNode";
import {convertHeritageClause} from "./plugins/convertHeritageClause";
import {convertExpressionWithTypeArguments} from "./plugins/convertExpressionWithTypeArguments";
import {convertPropertySignature} from "./plugins/convertPropertySignature";
import {convertSourceFile} from "./plugins/convertSourceFile";
import {convertModuleDeclaration} from "./plugins/convertModuleDeclaration";
import {convertModuleBlock} from "./plugins/convertModuleBlock";
import {convertMethodSignature} from "./plugins/convertMethodSignature";
import {convertFunctionType} from "./plugins/convertFunctionType";
import {convertLiteralType} from "./plugins/convertLiteralType";
import {convertArrayType} from "./plugins/convertArrayType";
import {traverse} from "../utils/traverse";
import {convertTypeAliasDeclaration} from "./plugins/convertTypeAliasDeclaration";
import {convertEnumDeclaration} from "./plugins/convertEnumDeclaration";
import {convertEnumMember} from "./plugins/convertEnumMember";
import {convertVariableStatement} from "./plugins/convertVariableStatement";
import {convertVariableDeclaration} from "./plugins/convertVariableDeclaration";
import {convertQualifiedName} from "./plugins/convertQualifiedName";
import {convertPrefixUnaryExpression} from "./plugins/convertPrefixUnaryExpression";
import {convertReadonlyArrayType} from "./plugins/convertReadonlyArrayType";
import {convertParenthesizedType} from "./plugins/convertParenthesizedType";
import {convertOptionalUnionType} from "./plugins/convertOptionalUnionType";
import {convertCallSignature} from "./plugins/convertCallSignature";
import {convertFunctionDeclaration} from "./plugins/convertFunctionDeclaration";
import {convertTypePredicate} from "./plugins/convertTypePredicate";

const foundKinds = new Set<SyntaxKind>()

const supportedKinds: SyntaxKind[] = [
    SyntaxKind.SourceFile, // lines
    SyntaxKind.ModuleDeclaration, // skip
    SyntaxKind.DeclareKeyword, // skip
    SyntaxKind.Identifier, // node.text
    SyntaxKind.ModuleBlock, // skip
    SyntaxKind.VariableStatement, // variables
    SyntaxKind.VariableDeclarationList, // variables
    SyntaxKind.VariableDeclaration, // (val|var) name: type(?)
    SyntaxKind.StringLiteral, // text
    SyntaxKind.StringKeyword, // String
    SyntaxKind.InterfaceDeclaration, // interface <params> : parents { members }
    SyntaxKind.TypeParameter, // name : constraint /* default */
    SyntaxKind.IndexSignature, // TODO: get operator
    SyntaxKind.Parameter, // (vararg) name: type ( = definedExternally)
    SyntaxKind.TypeReference, // typeName.right or typeName (typeArguments)
    SyntaxKind.HeritageClause, // :
    SyntaxKind.ExpressionWithTypeArguments, // expression (typeArguments)
    SyntaxKind.PropertySignature, // var name: type(?)
    SyntaxKind.AnyKeyword, // Any?
    SyntaxKind.ReadonlyKeyword, // skip, handled by PropertySignature and readonly arrays converter
    SyntaxKind.NumberKeyword, // Double TODO: detect integers
    SyntaxKind.MethodSignature, // fun <params> name(params): returnType
    SyntaxKind.BooleanKeyword, // Boolean
    SyntaxKind.VoidKeyword, // Unit
    SyntaxKind.UnionType, // optional types are supported TODO: preprocess unions
    SyntaxKind.UndefinedKeyword, // null
    SyntaxKind.TupleType, // TODO: support tuples
    SyntaxKind.FunctionType, // (params) -> returnType // TODO: generate type aliases for generics
    SyntaxKind.ThisType, // TODO: inline this type
    SyntaxKind.TypeLiteral, // TODO: generate type aliases for type literal
    SyntaxKind.QuestionToken, // ?
    SyntaxKind.LiteralType, // skip
    SyntaxKind.FalseKeyword, // Boolean
    SyntaxKind.TrueKeyword, // Boolean
    SyntaxKind.DotDotDotToken, // vararg
    SyntaxKind.ArrayType, // Array<type>
    SyntaxKind.TypeAliasDeclaration, // typealias name<params> = type
    SyntaxKind.ExportKeyword, // skip
    SyntaxKind.IntersectionType, // TODO: convert intersection to inheritance
    SyntaxKind.EnumDeclaration, // enum class name { members }
    SyntaxKind.EnumMember, // node.name
    SyntaxKind.NumericLiteral, // node.text
    SyntaxKind.QualifiedName, // left.right
    SyntaxKind.IndexedAccessType, // should be preprocessed
    SyntaxKind.NeverKeyword, // Nothing
    SyntaxKind.PrefixUnaryExpression, // operator operand
    SyntaxKind.TypeOperator, // supported only readonly arrays
    SyntaxKind.ParenthesizedType, // (type)
    SyntaxKind.ClassDeclaration, // interface <params> : parents { members }
    SyntaxKind.NullKeyword, // null
    SyntaxKind.CallSignature, // operator fun <params> invoke(params): returnType
    SyntaxKind.ExportDeclaration, // skip, public by default
    SyntaxKind.NamedExports, // ignore
    SyntaxKind.FunctionDeclaration, // fun <params> name(params): returnType
    SyntaxKind.TypePredicate, // Boolean TODO: support contracts
    SyntaxKind.TypeQuery, // ignore
    SyntaxKind.ObjectBindingPattern, // TODO: infer name
    SyntaxKind.BindingElement, // ignore
    SyntaxKind.UnknownKeyword, // Any?
    SyntaxKind.ExportAssignment, // ignore
    SyntaxKind.EndOfFileToken, // skip
    SyntaxKind.ComputedPropertyName,
    SyntaxKind.PropertyAccessExpression,
    SyntaxKind.ConstructSignature,
    SyntaxKind.ObjectKeyword, // Any
]

function checkKinds(sourceFile: SourceFile) {
    traverse(sourceFile, node => {
        foundKinds.add(node.kind)

        if (!supportedKinds.includes(node.kind)) {
            console.error(`Unknown syntax kind ${SyntaxKind[node.kind]}`)
        }
    })
}

const coveredNodes = new Set<Node>()

const uncoveredNodes = new Set<Node>()

function checkCoveredNodes(sourceFile: SourceFile) {
    const printer = ts.createPrinter({newLine: ts.NewLineKind.LineFeed});

    traverse(sourceFile, node => {
        if (!coveredNodes.has(node)) {
            uncoveredNodes.add(node)

            console.error(`Node with kind ${SyntaxKind[node.kind]} is uncovered`)

            console.error("--- Node Start ---");
            console.error(printer.printNode(EmitHint.Unspecified, node, sourceFile));
            console.error("--- Node End ---");

            console.error();
        }
    })
}

const hasKind = (kind: SyntaxKind) => (node: Node) => node.kind === kind

const plugins: ConverterPlugin[] = [
    convertPrimitive(hasKind(SyntaxKind.DeclareKeyword), () => ""),
    convertPrimitive(hasKind(SyntaxKind.ExportDeclaration), () => ""),

    convertPrimitive(hasKind(SyntaxKind.AnyKeyword), () => "Any?"),
    convertPrimitive(hasKind(SyntaxKind.UnknownKeyword), () => "Any?"),
    convertPrimitive(hasKind(SyntaxKind.UndefinedKeyword), () => "null"),
    convertPrimitive(hasKind(SyntaxKind.NullKeyword), () => "null"),
    convertPrimitive(hasKind(SyntaxKind.ObjectKeyword), () => "Any"),
    convertPrimitive(hasKind(SyntaxKind.StringKeyword), () => "String"),
    convertPrimitive(hasKind(SyntaxKind.NumberKeyword), () => "Double"),
    convertPrimitive(hasKind(SyntaxKind.BooleanKeyword), () => "Boolean"),
    convertPrimitive(hasKind(SyntaxKind.FalseKeyword), () => "Boolean"),
    convertPrimitive(hasKind(SyntaxKind.TrueKeyword), () => "Boolean"),
    convertPrimitive(hasKind(SyntaxKind.VoidKeyword), () => "Unit"),
    convertPrimitive(hasKind(SyntaxKind.NeverKeyword), () => "Nothing"),

    convertPrimitive(ts.isIdentifier, node => node.text),
    convertPrimitive(ts.isStringLiteral, node => node.text),
    convertPrimitive(ts.isNumericLiteral, node => node.text),

    convertSourceFile,
    convertModuleDeclaration,
    convertModuleBlock,
    convertInterfaceDeclaration,
    convertTypeParameterDeclaration,
    convertParameterDeclaration,
    convertTypeReferenceNode,
    convertHeritageClause,
    convertExpressionWithTypeArguments,
    convertPropertySignature,
    convertMethodSignature,
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
    convertPrefixUnaryExpression,
    convertParenthesizedType,
    convertOptionalUnionType,
    convertCallSignature,
    convertFunctionDeclaration,
    convertTypePredicate,
]

export function convert(sourceFile: SourceFile): string {
    const context: ConverterContext = {
        cover(node: Node) {
            coveredNodes.add(node)
        },
        deepCover(node: Node) {
            traverse(node, it => this.cover(it))
        }
    }

    const render = (node: Node) => {
        for (const plugin of plugins) {
            const result = plugin(node, context, render)

            if (result !== null) return result
        }

        return `/* ${node.getText()} */`
    }

    checkKinds(sourceFile)

    const output = render(sourceFile)

    checkCoveredNodes(sourceFile)

    console.log(`Covered nodes: ${coveredNodes.size}`)
    console.log(`Uncovered nodes: ${uncoveredNodes.size}`)

    return output
}

