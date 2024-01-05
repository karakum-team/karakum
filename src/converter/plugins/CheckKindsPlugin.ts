import {ConverterPlugin} from "../plugin.js";
import ts, {Node} from "typescript";
import {ConverterContext} from "../context.js";
import {Render} from "../render.js";
import {ConfigurationService, configurationServiceKey} from "./ConfigurationPlugin.js";
import {GeneratedFile} from "../generated.js";

const supportedKinds: ts.SyntaxKind[] = [
    ts.SyntaxKind.SourceFile, // lines
    ts.SyntaxKind.ModuleDeclaration, // skip
    ts.SyntaxKind.DeclareKeyword, // skip
    ts.SyntaxKind.Identifier, // node.text
    ts.SyntaxKind.ModuleBlock, // skip
    ts.SyntaxKind.VariableStatement, // variables
    ts.SyntaxKind.VariableDeclarationList, // variables
    ts.SyntaxKind.VariableDeclaration, // (val|var) name: type(?)
    ts.SyntaxKind.StringLiteral, // text
    ts.SyntaxKind.StringKeyword, // String
    ts.SyntaxKind.TemplateLiteralType, // String
    ts.SyntaxKind.InterfaceDeclaration, // interface <params> : parents { members }
    ts.SyntaxKind.TypeParameter, // name : constraint /* default */
    ts.SyntaxKind.IndexSignature, // TODO: get operator
    ts.SyntaxKind.Parameter, // (vararg) name: type ( = definedExternally)
    ts.SyntaxKind.TypeReference, // typeName.right or typeName (typeArguments)
    ts.SyntaxKind.HeritageClause, // :
    ts.SyntaxKind.ExpressionWithTypeArguments, // expression (typeArguments)
    ts.SyntaxKind.Constructor, // constructor(params)
    ts.SyntaxKind.PropertySignature, // var name: type(?)
    ts.SyntaxKind.PropertyDeclaration, // var name: type(?)
    ts.SyntaxKind.GetAccessor, // val/var name: type(?)
    ts.SyntaxKind.SetAccessor, // var name: type(?)
    ts.SyntaxKind.AnyKeyword, // Any?
    ts.SyntaxKind.ReadonlyKeyword, // skip, handled by PropertySignature and readonly arrays converter
    ts.SyntaxKind.NumberKeyword, // Double TODO: detect integers
    ts.SyntaxKind.MethodSignature, // fun <params> name(params): returnType
    ts.SyntaxKind.MethodDeclaration, // fun <params> name(params): returnType
    ts.SyntaxKind.BooleanKeyword, // Boolean
    ts.SyntaxKind.VoidKeyword, // Unit
    ts.SyntaxKind.UnionType, // optional types are supported TODO: preprocess unions
    ts.SyntaxKind.UndefinedKeyword, // null
    ts.SyntaxKind.TupleType, // TODO: support tuples
    ts.SyntaxKind.FunctionType, // (params) -> returnType // TODO: generate type aliases for generics
    ts.SyntaxKind.ThisType, // TODO: inline this type
    ts.SyntaxKind.TypeLiteral, // TODO: generate type aliases for type literal
    ts.SyntaxKind.QuestionToken, // ?
    ts.SyntaxKind.LiteralType, // skip
    ts.SyntaxKind.FalseKeyword, // Boolean
    ts.SyntaxKind.TrueKeyword, // Boolean
    ts.SyntaxKind.DotDotDotToken, // vararg
    ts.SyntaxKind.ArrayType, // Array<type>
    ts.SyntaxKind.TypeAliasDeclaration, // typealias name<params> = type
    ts.SyntaxKind.ExportKeyword, // skip
    ts.SyntaxKind.IntersectionType, // TODO: convert intersection to inheritance
    ts.SyntaxKind.EnumDeclaration, // sealed external interface name { companion object { members } }
    ts.SyntaxKind.EnumMember, // node.name
    ts.SyntaxKind.NumericLiteral, // node.text
    ts.SyntaxKind.QualifiedName, // left.right
    ts.SyntaxKind.IndexedAccessType, // should be preprocessed
    ts.SyntaxKind.NeverKeyword, // Nothing
    ts.SyntaxKind.PrefixUnaryExpression, // operator operand
    ts.SyntaxKind.TypeOperator, // supported only readonly arrays
    ts.SyntaxKind.ParenthesizedType, // (type)
    ts.SyntaxKind.ClassDeclaration, // interface <params> : parents { members }
    ts.SyntaxKind.NullKeyword, // null
    ts.SyntaxKind.CallSignature, // operator fun <params> invoke(params): returnType
    ts.SyntaxKind.ExportDeclaration, // skip, public by default
    ts.SyntaxKind.NamedExports, // ignore
    ts.SyntaxKind.FunctionDeclaration, // fun <params> name(params): returnType
    ts.SyntaxKind.TypePredicate, // Boolean TODO: support contracts
    ts.SyntaxKind.TypeQuery, // ignore
    ts.SyntaxKind.ObjectBindingPattern, // TODO: infer name
    ts.SyntaxKind.BindingElement, // ignore
    ts.SyntaxKind.UnknownKeyword, // Any?
    ts.SyntaxKind.SymbolKeyword, // js.symbol.Symbol
    ts.SyntaxKind.BigIntKeyword, // js.core.BigInt
    ts.SyntaxKind.StaticKeyword, // companion object
    ts.SyntaxKind.ExportAssignment, // ignore
    ts.SyntaxKind.EndOfFileToken, // skip
    ts.SyntaxKind.ComputedPropertyName,
    ts.SyntaxKind.PropertyAccessExpression,
    ts.SyntaxKind.ConstructSignature,
    ts.SyntaxKind.ObjectKeyword, // Any
    ts.SyntaxKind.ImportType,
    ts.SyntaxKind.MappedType,
]

export class CheckKindsPlugin implements ConverterPlugin {
    generate(): GeneratedFile[] {
        return [];
    }

    render(node: Node, context: ConverterContext, next: Render): string | null {
        return null;
    }

    traverse(node: Node, context: ConverterContext): void {
        const configurationService = context.lookupService<ConfigurationService>(configurationServiceKey)

        if (configurationService?.configuration?.verbose && !supportedKinds.includes(node.kind)) {
            console.error(`Unknown syntax kind ${ts.SyntaxKind[node.kind]}`)
        }
    }

    setup(context: ConverterContext): void {
    }
}
