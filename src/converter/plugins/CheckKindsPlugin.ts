import {ConverterPlugin} from "../plugin";
import {Node, SyntaxKind} from "typescript";
import {ConverterContext} from "../context";
import {Render} from "../render";
import {ConfigurationService, configurationServiceKey} from "./ConfigurationPlugin";

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
    SyntaxKind.EnumDeclaration, // sealed external interface name { companion object { members } }
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
    SyntaxKind.ImportType,
]

export class CheckKindsPlugin implements ConverterPlugin {
    generate(): Record<string, string> {
        return {};
    }

    render(node: Node, context: ConverterContext, next: Render): string | null {
        return null;
    }

    traverse(node: Node, context: ConverterContext): void {
        const configurationService = context.lookupService<ConfigurationService>(configurationServiceKey)

        if (configurationService?.configuration?.verbose && !supportedKinds.includes(node.kind)) {
            console.error(`Unknown syntax kind ${SyntaxKind[node.kind]}`)
        }
    }

    setup(context: ConverterContext): void {
    }
}
