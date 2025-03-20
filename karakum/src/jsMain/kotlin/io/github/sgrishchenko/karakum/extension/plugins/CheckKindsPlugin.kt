package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.Context
import io.github.sgrishchenko.karakum.extension.ConverterPlugin
import io.github.sgrishchenko.karakum.extension.GeneratedFile
import io.github.sgrishchenko.karakum.extension.Render
import js.objects.ReadonlyRecord
import typescript.Node
import typescript.SyntaxKind

private val supportedKinds = setOf(
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
    SyntaxKind.TemplateLiteralType, // String
    SyntaxKind.InterfaceDeclaration, // interface <params> : parents { members }
    SyntaxKind.TypeParameter, // name : constraint /* default */
    SyntaxKind.IndexSignature, // TODO: get operator
    SyntaxKind.Parameter, // (vararg) name: type ( = definedExternally)
    SyntaxKind.TypeReference, // typeName.right or typeName (typeArguments)
    SyntaxKind.HeritageClause, // :
    SyntaxKind.ExpressionWithTypeArguments, // expression (typeArguments)
    SyntaxKind.Constructor, // constructor(params)
    SyntaxKind.PropertySignature, // var name: type(?)
    SyntaxKind.PropertyDeclaration, // var name: type(?)
    SyntaxKind.GetAccessor, // val/var name: type(?)
    SyntaxKind.SetAccessor, // var name: type(?)
    SyntaxKind.AnyKeyword, // Any?
    SyntaxKind.ReadonlyKeyword, // skip, handled by PropertySignature and readonly arrays converter
    SyntaxKind.NumberKeyword, // Double TODO: detect integers
    SyntaxKind.MethodSignature, // fun <params> name(params): returnType
    SyntaxKind.MethodDeclaration, // fun <params> name(params): returnType
    SyntaxKind.BooleanKeyword, // Boolean
    SyntaxKind.VoidKeyword, // Unit
    SyntaxKind.UnionType, // optional types are supported TODO: preprocess unions
    SyntaxKind.UndefinedKeyword, // null
    SyntaxKind.TupleType, // TODO: support tuples
    SyntaxKind.FunctionType, // (params) -> returnType // TODO: generate type aliases for generics
    SyntaxKind.ThisType, // Unit /* this */
    SyntaxKind.TypeLiteral, // TODO: generate type aliases for type literal
    SyntaxKind.QuestionToken, // ?
    SyntaxKind.LiteralType, // skip
    SyntaxKind.FalseKeyword, // Boolean
    SyntaxKind.TrueKeyword, // Boolean
    SyntaxKind.DotDotDotToken, // vararg
    SyntaxKind.ArrayType, // Array<type>
    SyntaxKind.TypeAliasDeclaration, // typealias name<params> = type
    SyntaxKind.ExportKeyword, // skip
    SyntaxKind.IntersectionType, // sealed external interface name : parent
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
    SyntaxKind.SymbolKeyword, // js.symbol.Symbol
    SyntaxKind.BigIntKeyword, // js.core.BigInt
    SyntaxKind.StaticKeyword, // companion object
    SyntaxKind.ExportAssignment, // ignore
    SyntaxKind.EndOfFileToken, // skip
    SyntaxKind.ComputedPropertyName,
    SyntaxKind.PropertyAccessExpression,
    SyntaxKind.ConstructSignature,
    SyntaxKind.ObjectKeyword, // Any
    SyntaxKind.ImportType,
    SyntaxKind.MappedType,
)

class CheckKindsPlugin : ConverterPlugin<Node> {
    override fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()

    override fun render(node: Node, context: Context, next: Render<Node>) = null

    override fun traverse(node: Node, context: Context){
        val configurationService = context.lookupService<ConfigurationService>(configurationServiceKey)

        if (
            configurationService?.configuration?.verbose == true
            && node.kind !in supportedKinds
        ) {
            val syntaxKindRecord = SyntaxKind.unsafeCast<ReadonlyRecord<SyntaxKind, String>>()
            console.error("Unknown syntax kind ${syntaxKindRecord[node.kind]}")
        }
    }

    override fun setup(context: Context) = Unit
}
