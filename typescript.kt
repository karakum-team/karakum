external val versionMajorMinor: Any? /* should be inferred */
external val version: String

        external interface MapLike <T  >  {
            /* [index: string]: T; */
        }
    

        external interface SortedReadonlyArray <T  >  : ReadonlyArray<T> {
            var  __sortedArrayBrand: Any?
        }
    

        external interface SortedArray <T  >  : Array<T> {
            var  __sortedArrayBrand: Any?
        }
    

        external interface ReadonlyCollection <K  >  {
            val size: Double
fun  has(key: K): Boolean
fun  keys(): Iterator<K>
        }
    

        external interface Collection <K  >  : ReadonlyCollection<K> {
            fun  delete(key: K): Boolean
fun  clear(): Unit
        }
    

        external interface ReadonlyESMap <K  , V  >  : ReadonlyCollection<K> {
            fun  get(key: K): V?
fun  values(): Iterator<V>
fun  entries(): Iterator</* [K, V] */>
fun  forEach(action: (value: V, key: K) -> Unit): Unit
        }
    

        external interface ReadonlyMap <T  >  : ReadonlyESMap<String, T> {
            
        }
    

        external interface ESMap <K  , V  >  : ReadonlyESMap<K, V>, Collection<K> {
            fun  set(key: K, value: V): /* this */
        }
    

        external interface Map <T  >  : ESMap<String, T> {
            
        }
    

        external interface ReadonlySet <T  >  : ReadonlyCollection<T> {
            fun  has(value: T): Boolean
fun  values(): Iterator<T>
fun  entries(): Iterator</* [T, T] */>
fun  forEach(action: (value: T, key: T) -> Unit): Unit
        }
    

        external interface Set <T  >  : ReadonlySet<T>, Collection<T> {
            fun  add(value: T): /* this */
fun  delete(value: T): Boolean
        }
    

        external interface Iterator <T  >  {
            fun  next(): /* {
            value: T;
            done?: false;
        } | {
            value: void;
            done: true;
        } */
        }
    

        external interface Push <T  >  {
            fun  push(vararg values: T): Unit
        }
    
typealias Path = /* string & {
        __pathBrand: any;
    } */

        external interface TextRange   {
            var pos: Double
var end: Double
        }
    

        external interface ReadonlyTextRange   {
            val pos: Double
val end: Double
        }
    

        external enum class SyntaxKind {
            Unknown,
EndOfFileToken,
SingleLineCommentTrivia,
MultiLineCommentTrivia,
NewLineTrivia,
WhitespaceTrivia,
ShebangTrivia,
ConflictMarkerTrivia,
NumericLiteral,
BigIntLiteral,
StringLiteral,
JsxText,
JsxTextAllWhiteSpaces,
RegularExpressionLiteral,
NoSubstitutionTemplateLiteral,
TemplateHead,
TemplateMiddle,
TemplateTail,
OpenBraceToken,
CloseBraceToken,
OpenParenToken,
CloseParenToken,
OpenBracketToken,
CloseBracketToken,
DotToken,
DotDotDotToken,
SemicolonToken,
CommaToken,
QuestionDotToken,
LessThanToken,
LessThanSlashToken,
GreaterThanToken,
LessThanEqualsToken,
GreaterThanEqualsToken,
EqualsEqualsToken,
ExclamationEqualsToken,
EqualsEqualsEqualsToken,
ExclamationEqualsEqualsToken,
EqualsGreaterThanToken,
PlusToken,
MinusToken,
AsteriskToken,
AsteriskAsteriskToken,
SlashToken,
PercentToken,
PlusPlusToken,
MinusMinusToken,
LessThanLessThanToken,
GreaterThanGreaterThanToken,
GreaterThanGreaterThanGreaterThanToken,
AmpersandToken,
BarToken,
CaretToken,
ExclamationToken,
TildeToken,
AmpersandAmpersandToken,
BarBarToken,
QuestionToken,
ColonToken,
AtToken,
QuestionQuestionToken,
BacktickToken,
HashToken,
EqualsToken,
PlusEqualsToken,
MinusEqualsToken,
AsteriskEqualsToken,
AsteriskAsteriskEqualsToken,
SlashEqualsToken,
PercentEqualsToken,
LessThanLessThanEqualsToken,
GreaterThanGreaterThanEqualsToken,
GreaterThanGreaterThanGreaterThanEqualsToken,
AmpersandEqualsToken,
BarEqualsToken,
BarBarEqualsToken,
AmpersandAmpersandEqualsToken,
QuestionQuestionEqualsToken,
CaretEqualsToken,
Identifier,
PrivateIdentifier,
BreakKeyword,
CaseKeyword,
CatchKeyword,
ClassKeyword,
ConstKeyword,
ContinueKeyword,
DebuggerKeyword,
DefaultKeyword,
DeleteKeyword,
DoKeyword,
ElseKeyword,
EnumKeyword,
ExportKeyword,
ExtendsKeyword,
FalseKeyword,
FinallyKeyword,
ForKeyword,
FunctionKeyword,
IfKeyword,
ImportKeyword,
InKeyword,
InstanceOfKeyword,
NewKeyword,
NullKeyword,
ReturnKeyword,
SuperKeyword,
SwitchKeyword,
ThisKeyword,
ThrowKeyword,
TrueKeyword,
TryKeyword,
TypeOfKeyword,
VarKeyword,
VoidKeyword,
WhileKeyword,
WithKeyword,
ImplementsKeyword,
InterfaceKeyword,
LetKeyword,
PackageKeyword,
PrivateKeyword,
ProtectedKeyword,
PublicKeyword,
StaticKeyword,
YieldKeyword,
AbstractKeyword,
AsKeyword,
AssertsKeyword,
AnyKeyword,
AsyncKeyword,
AwaitKeyword,
BooleanKeyword,
ConstructorKeyword,
DeclareKeyword,
GetKeyword,
InferKeyword,
IntrinsicKeyword,
IsKeyword,
KeyOfKeyword,
ModuleKeyword,
NamespaceKeyword,
NeverKeyword,
ReadonlyKeyword,
RequireKeyword,
NumberKeyword,
ObjectKeyword,
SetKeyword,
StringKeyword,
SymbolKeyword,
TypeKeyword,
UndefinedKeyword,
UniqueKeyword,
UnknownKeyword,
FromKeyword,
GlobalKeyword,
BigIntKeyword,
OverrideKeyword,
OfKeyword,
QualifiedName,
ComputedPropertyName,
TypeParameter,
Parameter,
Decorator,
PropertySignature,
PropertyDeclaration,
MethodSignature,
MethodDeclaration,
ClassStaticBlockDeclaration,
Constructor,
GetAccessor,
SetAccessor,
CallSignature,
ConstructSignature,
IndexSignature,
TypePredicate,
TypeReference,
FunctionType,
ConstructorType,
TypeQuery,
TypeLiteral,
ArrayType,
TupleType,
OptionalType,
RestType,
UnionType,
IntersectionType,
ConditionalType,
InferType,
ParenthesizedType,
ThisType,
TypeOperator,
IndexedAccessType,
MappedType,
LiteralType,
NamedTupleMember,
TemplateLiteralType,
TemplateLiteralTypeSpan,
ImportType,
ObjectBindingPattern,
ArrayBindingPattern,
BindingElement,
ArrayLiteralExpression,
ObjectLiteralExpression,
PropertyAccessExpression,
ElementAccessExpression,
CallExpression,
NewExpression,
TaggedTemplateExpression,
TypeAssertionExpression,
ParenthesizedExpression,
FunctionExpression,
ArrowFunction,
DeleteExpression,
TypeOfExpression,
VoidExpression,
AwaitExpression,
PrefixUnaryExpression,
PostfixUnaryExpression,
BinaryExpression,
ConditionalExpression,
TemplateExpression,
YieldExpression,
SpreadElement,
ClassExpression,
OmittedExpression,
ExpressionWithTypeArguments,
AsExpression,
NonNullExpression,
MetaProperty,
SyntheticExpression,
TemplateSpan,
SemicolonClassElement,
Block,
EmptyStatement,
VariableStatement,
ExpressionStatement,
IfStatement,
DoStatement,
WhileStatement,
ForStatement,
ForInStatement,
ForOfStatement,
ContinueStatement,
BreakStatement,
ReturnStatement,
WithStatement,
SwitchStatement,
LabeledStatement,
ThrowStatement,
TryStatement,
DebuggerStatement,
VariableDeclaration,
VariableDeclarationList,
FunctionDeclaration,
ClassDeclaration,
InterfaceDeclaration,
TypeAliasDeclaration,
EnumDeclaration,
ModuleDeclaration,
ModuleBlock,
CaseBlock,
NamespaceExportDeclaration,
ImportEqualsDeclaration,
ImportDeclaration,
ImportClause,
NamespaceImport,
NamedImports,
ImportSpecifier,
ExportAssignment,
ExportDeclaration,
NamedExports,
NamespaceExport,
ExportSpecifier,
MissingDeclaration,
ExternalModuleReference,
JsxElement,
JsxSelfClosingElement,
JsxOpeningElement,
JsxClosingElement,
JsxFragment,
JsxOpeningFragment,
JsxClosingFragment,
JsxAttribute,
JsxAttributes,
JsxSpreadAttribute,
JsxExpression,
CaseClause,
DefaultClause,
HeritageClause,
CatchClause,
PropertyAssignment,
ShorthandPropertyAssignment,
SpreadAssignment,
EnumMember,
UnparsedPrologue,
UnparsedPrepend,
UnparsedText,
UnparsedInternalText,
UnparsedSyntheticReference,
SourceFile,
Bundle,
UnparsedSource,
InputFiles,
JSDocTypeExpression,
JSDocNameReference,
JSDocMemberName,
JSDocAllType,
JSDocUnknownType,
JSDocNullableType,
JSDocNonNullableType,
JSDocOptionalType,
JSDocFunctionType,
JSDocVariadicType,
JSDocNamepathType,
JSDocComment,
JSDocText,
JSDocTypeLiteral,
JSDocSignature,
JSDocLink,
JSDocLinkCode,
JSDocLinkPlain,
JSDocTag,
JSDocAugmentsTag,
JSDocImplementsTag,
JSDocAuthorTag,
JSDocDeprecatedTag,
JSDocClassTag,
JSDocPublicTag,
JSDocPrivateTag,
JSDocProtectedTag,
JSDocReadonlyTag,
JSDocOverrideTag,
JSDocCallbackTag,
JSDocEnumTag,
JSDocParameterTag,
JSDocReturnTag,
JSDocThisTag,
JSDocTypeTag,
JSDocTemplateTag,
JSDocTypedefTag,
JSDocSeeTag,
JSDocPropertyTag,
SyntaxList,
NotEmittedStatement,
PartiallyEmittedExpression,
CommaListExpression,
MergeDeclarationMarker,
EndOfDeclarationMarker,
SyntheticReferenceExpression,
Count,
FirstAssignment,
LastAssignment,
FirstCompoundAssignment,
LastCompoundAssignment,
FirstReservedWord,
LastReservedWord,
FirstKeyword,
LastKeyword,
FirstFutureReservedWord,
LastFutureReservedWord,
FirstTypeNode,
LastTypeNode,
FirstPunctuation,
LastPunctuation,
FirstToken,
LastToken,
FirstTriviaToken,
LastTriviaToken,
FirstLiteralToken,
LastLiteralToken,
FirstTemplateToken,
LastTemplateToken,
FirstBinaryOperator,
LastBinaryOperator,
FirstStatement,
LastStatement,
FirstNode,
FirstJSDocNode,
LastJSDocNode,
FirstJSDocTagNode,
LastJSDocTagNode
        }
    
typealias TriviaSyntaxKind = /* SyntaxKind.SingleLineCommentTrivia | SyntaxKind.MultiLineCommentTrivia | SyntaxKind.NewLineTrivia | SyntaxKind.WhitespaceTrivia | SyntaxKind.ShebangTrivia | SyntaxKind.ConflictMarkerTrivia */
typealias LiteralSyntaxKind = /* SyntaxKind.NumericLiteral | SyntaxKind.BigIntLiteral | SyntaxKind.StringLiteral | SyntaxKind.JsxText | SyntaxKind.JsxTextAllWhiteSpaces | SyntaxKind.RegularExpressionLiteral | SyntaxKind.NoSubstitutionTemplateLiteral */
typealias PseudoLiteralSyntaxKind = /* SyntaxKind.TemplateHead | SyntaxKind.TemplateMiddle | SyntaxKind.TemplateTail */
typealias PunctuationSyntaxKind = /* SyntaxKind.OpenBraceToken | SyntaxKind.CloseBraceToken | SyntaxKind.OpenParenToken | SyntaxKind.CloseParenToken | SyntaxKind.OpenBracketToken | SyntaxKind.CloseBracketToken | SyntaxKind.DotToken | SyntaxKind.DotDotDotToken | SyntaxKind.SemicolonToken | SyntaxKind.CommaToken | SyntaxKind.QuestionDotToken | SyntaxKind.LessThanToken | SyntaxKind.LessThanSlashToken | SyntaxKind.GreaterThanToken | SyntaxKind.LessThanEqualsToken | SyntaxKind.GreaterThanEqualsToken | SyntaxKind.EqualsEqualsToken | SyntaxKind.ExclamationEqualsToken | SyntaxKind.EqualsEqualsEqualsToken | SyntaxKind.ExclamationEqualsEqualsToken | SyntaxKind.EqualsGreaterThanToken | SyntaxKind.PlusToken | SyntaxKind.MinusToken | SyntaxKind.AsteriskToken | SyntaxKind.AsteriskAsteriskToken | SyntaxKind.SlashToken | SyntaxKind.PercentToken | SyntaxKind.PlusPlusToken | SyntaxKind.MinusMinusToken | SyntaxKind.LessThanLessThanToken | SyntaxKind.GreaterThanGreaterThanToken | SyntaxKind.GreaterThanGreaterThanGreaterThanToken | SyntaxKind.AmpersandToken | SyntaxKind.BarToken | SyntaxKind.CaretToken | SyntaxKind.ExclamationToken | SyntaxKind.TildeToken | SyntaxKind.AmpersandAmpersandToken | SyntaxKind.BarBarToken | SyntaxKind.QuestionQuestionToken | SyntaxKind.QuestionToken | SyntaxKind.ColonToken | SyntaxKind.AtToken | SyntaxKind.BacktickToken | SyntaxKind.HashToken | SyntaxKind.EqualsToken | SyntaxKind.PlusEqualsToken | SyntaxKind.MinusEqualsToken | SyntaxKind.AsteriskEqualsToken | SyntaxKind.AsteriskAsteriskEqualsToken | SyntaxKind.SlashEqualsToken | SyntaxKind.PercentEqualsToken | SyntaxKind.LessThanLessThanEqualsToken | SyntaxKind.GreaterThanGreaterThanEqualsToken | SyntaxKind.GreaterThanGreaterThanGreaterThanEqualsToken | SyntaxKind.AmpersandEqualsToken | SyntaxKind.BarEqualsToken | SyntaxKind.CaretEqualsToken */
typealias KeywordSyntaxKind = /* SyntaxKind.AbstractKeyword | SyntaxKind.AnyKeyword | SyntaxKind.AsKeyword | SyntaxKind.AssertsKeyword | SyntaxKind.AsyncKeyword | SyntaxKind.AwaitKeyword | SyntaxKind.BigIntKeyword | SyntaxKind.BooleanKeyword | SyntaxKind.BreakKeyword | SyntaxKind.CaseKeyword | SyntaxKind.CatchKeyword | SyntaxKind.ClassKeyword | SyntaxKind.ConstKeyword | SyntaxKind.ConstructorKeyword | SyntaxKind.ContinueKeyword | SyntaxKind.DebuggerKeyword | SyntaxKind.DeclareKeyword | SyntaxKind.DefaultKeyword | SyntaxKind.DeleteKeyword | SyntaxKind.DoKeyword | SyntaxKind.ElseKeyword | SyntaxKind.EnumKeyword | SyntaxKind.ExportKeyword | SyntaxKind.ExtendsKeyword | SyntaxKind.FalseKeyword | SyntaxKind.FinallyKeyword | SyntaxKind.ForKeyword | SyntaxKind.FromKeyword | SyntaxKind.FunctionKeyword | SyntaxKind.GetKeyword | SyntaxKind.GlobalKeyword | SyntaxKind.IfKeyword | SyntaxKind.ImplementsKeyword | SyntaxKind.ImportKeyword | SyntaxKind.InferKeyword | SyntaxKind.InKeyword | SyntaxKind.InstanceOfKeyword | SyntaxKind.InterfaceKeyword | SyntaxKind.IntrinsicKeyword | SyntaxKind.IsKeyword | SyntaxKind.KeyOfKeyword | SyntaxKind.LetKeyword | SyntaxKind.ModuleKeyword | SyntaxKind.NamespaceKeyword | SyntaxKind.NeverKeyword | SyntaxKind.NewKeyword | SyntaxKind.NullKeyword | SyntaxKind.NumberKeyword | SyntaxKind.ObjectKeyword | SyntaxKind.OfKeyword | SyntaxKind.PackageKeyword | SyntaxKind.PrivateKeyword | SyntaxKind.ProtectedKeyword | SyntaxKind.PublicKeyword | SyntaxKind.ReadonlyKeyword | SyntaxKind.OverrideKeyword | SyntaxKind.RequireKeyword | SyntaxKind.ReturnKeyword | SyntaxKind.SetKeyword | SyntaxKind.StaticKeyword | SyntaxKind.StringKeyword | SyntaxKind.SuperKeyword | SyntaxKind.SwitchKeyword | SyntaxKind.SymbolKeyword | SyntaxKind.ThisKeyword | SyntaxKind.ThrowKeyword | SyntaxKind.TrueKeyword | SyntaxKind.TryKeyword | SyntaxKind.TypeKeyword | SyntaxKind.TypeOfKeyword | SyntaxKind.UndefinedKeyword | SyntaxKind.UniqueKeyword | SyntaxKind.UnknownKeyword | SyntaxKind.VarKeyword | SyntaxKind.VoidKeyword | SyntaxKind.WhileKeyword | SyntaxKind.WithKeyword | SyntaxKind.YieldKeyword */
typealias ModifierSyntaxKind = /* SyntaxKind.AbstractKeyword | SyntaxKind.AsyncKeyword | SyntaxKind.ConstKeyword | SyntaxKind.DeclareKeyword | SyntaxKind.DefaultKeyword | SyntaxKind.ExportKeyword | SyntaxKind.PrivateKeyword | SyntaxKind.ProtectedKeyword | SyntaxKind.PublicKeyword | SyntaxKind.ReadonlyKeyword | SyntaxKind.OverrideKeyword | SyntaxKind.StaticKeyword */
typealias KeywordTypeSyntaxKind = /* SyntaxKind.AnyKeyword | SyntaxKind.BigIntKeyword | SyntaxKind.BooleanKeyword | SyntaxKind.IntrinsicKeyword | SyntaxKind.NeverKeyword | SyntaxKind.NumberKeyword | SyntaxKind.ObjectKeyword | SyntaxKind.StringKeyword | SyntaxKind.SymbolKeyword | SyntaxKind.UndefinedKeyword | SyntaxKind.UnknownKeyword | SyntaxKind.VoidKeyword */
typealias TokenSyntaxKind = /* SyntaxKind.Unknown | SyntaxKind.EndOfFileToken | TriviaSyntaxKind | LiteralSyntaxKind | PseudoLiteralSyntaxKind | PunctuationSyntaxKind | SyntaxKind.Identifier | KeywordSyntaxKind */
typealias JsxTokenSyntaxKind = /* SyntaxKind.LessThanSlashToken | SyntaxKind.EndOfFileToken | SyntaxKind.ConflictMarkerTrivia | SyntaxKind.JsxText | SyntaxKind.JsxTextAllWhiteSpaces | SyntaxKind.OpenBraceToken | SyntaxKind.LessThanToken */
typealias JSDocSyntaxKind = /* SyntaxKind.EndOfFileToken | SyntaxKind.WhitespaceTrivia | SyntaxKind.AtToken | SyntaxKind.NewLineTrivia | SyntaxKind.AsteriskToken | SyntaxKind.OpenBraceToken | SyntaxKind.CloseBraceToken | SyntaxKind.LessThanToken | SyntaxKind.GreaterThanToken | SyntaxKind.OpenBracketToken | SyntaxKind.CloseBracketToken | SyntaxKind.EqualsToken | SyntaxKind.CommaToken | SyntaxKind.DotToken | SyntaxKind.Identifier | SyntaxKind.BacktickToken | SyntaxKind.HashToken | SyntaxKind.Unknown | KeywordSyntaxKind */

        external enum class NodeFlags {
            None,
Let,
Const,
NestedNamespace,
Synthesized,
Namespace,
OptionalChain,
ExportContext,
ContainsThis,
HasImplicitReturn,
HasExplicitReturn,
GlobalAugmentation,
HasAsyncFunctions,
DisallowInContext,
YieldContext,
DecoratorContext,
AwaitContext,
ThisNodeHasError,
JavaScriptFile,
ThisNodeOrAnySubNodesHasError,
HasAggregatedChildData,
JSDoc,
JsonFile,
BlockScoped,
ReachabilityCheckFlags,
ReachabilityAndEmitFlags,
ContextFlags,
TypeExcludesFlags
        }
    

        external enum class ModifierFlags {
            None,
Export,
Ambient,
Public,
Private,
Protected,
Static,
Readonly,
Abstract,
Async,
Default,
Const,
HasComputedJSDocModifiers,
Deprecated,
Override,
HasComputedFlags,
AccessibilityModifier,
ParameterPropertyModifier,
NonPublicAccessibilityModifier,
TypeScriptModifier,
ExportDefault,
All
        }
    

        external enum class JsxFlags {
            None,
IntrinsicNamedElement,
IntrinsicIndexedElement,
IntrinsicElement
        }
    

        external interface Node   : ReadonlyTextRange {
            val kind: SyntaxKind
val flags: NodeFlags
val decorators: NodeArray<Decorator>?
val modifiers: ModifiersArray?
val parent: Node
fun  getSourceFile(): SourceFile
fun  getChildCount(sourceFile: SourceFile = definedExternally): Double
fun  getChildAt(index: Double, sourceFile: SourceFile = definedExternally): Node
fun  getChildren(sourceFile: SourceFile = definedExternally): Array<Node>
fun  getStart(sourceFile: SourceFile = definedExternally, includeJsDocComment: Boolean = definedExternally): Double
fun  getFullStart(): Double
fun  getEnd(): Double
fun  getWidth(sourceFile: SourceFileLike = definedExternally): Double
fun  getFullWidth(): Double
fun  getLeadingTriviaWidth(sourceFile: SourceFile = definedExternally): Double
fun  getFullText(sourceFile: SourceFile = definedExternally): String
fun  getText(sourceFile: SourceFile = definedExternally): String
fun  getFirstToken(sourceFile: SourceFile = definedExternally): Node?
fun  getLastToken(sourceFile: SourceFile = definedExternally): Node?
fun <T  > forEachChild(cbNode: (node: Node) -> T?, cbNodeArray: (nodes: NodeArray<Node>) -> T? = definedExternally): T?
        }
    

        external interface JSDocContainer   {
            
        }
    
typealias HasJSDoc = /* ParameterDeclaration | CallSignatureDeclaration | ClassStaticBlockDeclaration | ConstructSignatureDeclaration | MethodSignature | PropertySignature | ArrowFunction | ParenthesizedExpression | SpreadAssignment | ShorthandPropertyAssignment | PropertyAssignment | FunctionExpression | EmptyStatement | DebuggerStatement | Block | VariableStatement | ExpressionStatement | IfStatement | DoStatement | WhileStatement | ForStatement | ForInStatement | ForOfStatement | BreakStatement | ContinueStatement | ReturnStatement | WithStatement | SwitchStatement | LabeledStatement | ThrowStatement | TryStatement | FunctionDeclaration | ConstructorDeclaration | MethodDeclaration | VariableDeclaration | PropertyDeclaration | AccessorDeclaration | ClassLikeDeclaration | InterfaceDeclaration | TypeAliasDeclaration | EnumMember | EnumDeclaration | ModuleDeclaration | ImportEqualsDeclaration | ImportDeclaration | NamespaceExportDeclaration | ExportAssignment | IndexSignatureDeclaration | FunctionTypeNode | ConstructorTypeNode | JSDocFunctionType | ExportDeclaration | NamedTupleMember | EndOfFileToken */
typealias HasType = /* SignatureDeclaration | VariableDeclaration | ParameterDeclaration | PropertySignature | PropertyDeclaration | TypePredicateNode | ParenthesizedTypeNode | TypeOperatorNode | MappedTypeNode | AssertionExpression | TypeAliasDeclaration | JSDocTypeExpression | JSDocNonNullableType | JSDocNullableType | JSDocOptionalType | JSDocVariadicType */
typealias HasTypeArguments = /* CallExpression | NewExpression | TaggedTemplateExpression | JsxOpeningElement | JsxSelfClosingElement */
typealias HasInitializer = /* HasExpressionInitializer | ForStatement | ForInStatement | ForOfStatement | JsxAttribute */
typealias HasExpressionInitializer = /* VariableDeclaration | ParameterDeclaration | BindingElement | PropertySignature | PropertyDeclaration | PropertyAssignment | EnumMember */

        external interface NodeArray <T : Node >  : ReadonlyArray<T>, ReadonlyTextRange {
            val hasTrailingComma: Boolean
        }
    

        external interface Token <TKind : SyntaxKind >  : Node {
            val kind: TKind
        }
    
typealias EndOfFileToken = /* Token<SyntaxKind.EndOfFileToken> & JSDocContainer */

        external interface PunctuationToken <TKind : PunctuationSyntaxKind >  : Token<TKind> {
            
        }
    
typealias DotToken = PunctuationToken<SyntaxKind.DotToken>
typealias DotDotDotToken = PunctuationToken<SyntaxKind.DotDotDotToken>
typealias QuestionToken = PunctuationToken<SyntaxKind.QuestionToken>
typealias ExclamationToken = PunctuationToken<SyntaxKind.ExclamationToken>
typealias ColonToken = PunctuationToken<SyntaxKind.ColonToken>
typealias EqualsToken = PunctuationToken<SyntaxKind.EqualsToken>
typealias AsteriskToken = PunctuationToken<SyntaxKind.AsteriskToken>
typealias EqualsGreaterThanToken = PunctuationToken<SyntaxKind.EqualsGreaterThanToken>
typealias PlusToken = PunctuationToken<SyntaxKind.PlusToken>
typealias MinusToken = PunctuationToken<SyntaxKind.MinusToken>
typealias QuestionDotToken = PunctuationToken<SyntaxKind.QuestionDotToken>

        external interface KeywordToken <TKind : KeywordSyntaxKind >  : Token<TKind> {
            
        }
    
typealias AssertsKeyword = KeywordToken<SyntaxKind.AssertsKeyword>
typealias AwaitKeyword = KeywordToken<SyntaxKind.AwaitKeyword>
typealias AwaitKeywordToken = AwaitKeyword
typealias AssertsToken = AssertsKeyword

        external interface ModifierToken <TKind : ModifierSyntaxKind >  : KeywordToken<TKind> {
            
        }
    
typealias AbstractKeyword = ModifierToken<SyntaxKind.AbstractKeyword>
typealias AsyncKeyword = ModifierToken<SyntaxKind.AsyncKeyword>
typealias ConstKeyword = ModifierToken<SyntaxKind.ConstKeyword>
typealias DeclareKeyword = ModifierToken<SyntaxKind.DeclareKeyword>
typealias DefaultKeyword = ModifierToken<SyntaxKind.DefaultKeyword>
typealias ExportKeyword = ModifierToken<SyntaxKind.ExportKeyword>
typealias PrivateKeyword = ModifierToken<SyntaxKind.PrivateKeyword>
typealias ProtectedKeyword = ModifierToken<SyntaxKind.ProtectedKeyword>
typealias PublicKeyword = ModifierToken<SyntaxKind.PublicKeyword>
typealias ReadonlyKeyword = ModifierToken<SyntaxKind.ReadonlyKeyword>
typealias OverrideKeyword = ModifierToken<SyntaxKind.OverrideKeyword>
typealias StaticKeyword = ModifierToken<SyntaxKind.StaticKeyword>
typealias ReadonlyToken = ReadonlyKeyword
typealias Modifier = /* AbstractKeyword | AsyncKeyword | ConstKeyword | DeclareKeyword | DefaultKeyword | ExportKeyword | PrivateKeyword | ProtectedKeyword | PublicKeyword | OverrideKeyword | ReadonlyKeyword | StaticKeyword */
typealias AccessibilityModifier = /* PublicKeyword | PrivateKeyword | ProtectedKeyword */
typealias ParameterPropertyModifier = /* AccessibilityModifier | ReadonlyKeyword */
typealias ClassMemberModifier = /* AccessibilityModifier | ReadonlyKeyword | StaticKeyword */
typealias ModifiersArray = NodeArray<Modifier>

        external enum class GeneratedIdentifierFlags {
            None,
ReservedInNestedScopes,
Optimistic,
FileLevel,
AllowNameSubstitution
        }
    

        external interface Identifier   : PrimaryExpression, Declaration {
            val kind: SyntaxKind.Identifier
val escapedText: __String
val originalKeywordKind: SyntaxKind?
var isInJSDocNamespace: Boolean?
val text: String
        }
    

        external interface TransientIdentifier   : Identifier {
            var resolvedSymbol: Symbol
        }
    

        external interface QualifiedName   : Node {
            val kind: SyntaxKind.QualifiedName
val left: EntityName
val right: Identifier
        }
    
typealias EntityName = /* Identifier | QualifiedName */
typealias PropertyName = /* Identifier | StringLiteral | NumericLiteral | ComputedPropertyName | PrivateIdentifier */
typealias MemberName = /* Identifier | PrivateIdentifier */
typealias DeclarationName = /* Identifier | PrivateIdentifier | StringLiteralLike | NumericLiteral | ComputedPropertyName | ElementAccessExpression | BindingPattern | EntityNameExpression */

        external interface Declaration   : Node {
            var _declarationBrand: Any?
        }
    

        external interface NamedDeclaration   : Declaration {
            val name: DeclarationName?
        }
    

        external interface DeclarationStatement   : NamedDeclaration, Statement {
            val name: /* Identifier | StringLiteral | NumericLiteral */?
        }
    

        external interface ComputedPropertyName   : Node {
            val kind: SyntaxKind.ComputedPropertyName
val parent: Declaration
val expression: Expression
        }
    

        external interface PrivateIdentifier   : Node {
            val kind: SyntaxKind.PrivateIdentifier
val escapedText: __String
val text: String
        }
    

        external interface Decorator   : Node {
            val kind: SyntaxKind.Decorator
val parent: NamedDeclaration
val expression: LeftHandSideExpression
        }
    

        external interface TypeParameterDeclaration   : NamedDeclaration {
            val kind: SyntaxKind.TypeParameter
val parent: /* DeclarationWithTypeParameterChildren | InferTypeNode */
val name: Identifier
val constraint: TypeNode?
val default: TypeNode?
var expression: Expression?
        }
    

        external interface SignatureDeclarationBase   : NamedDeclaration, JSDocContainer {
            val kind: /* SignatureDeclaration["kind"] */
val name: PropertyName?
val typeParameters: NodeArray<TypeParameterDeclaration>?
val parameters: NodeArray<ParameterDeclaration>
val type: TypeNode?
        }
    
typealias SignatureDeclaration = /* CallSignatureDeclaration | ConstructSignatureDeclaration | MethodSignature | IndexSignatureDeclaration | FunctionTypeNode | ConstructorTypeNode | JSDocFunctionType | FunctionDeclaration | MethodDeclaration | ConstructorDeclaration | AccessorDeclaration | FunctionExpression | ArrowFunction */

        external interface CallSignatureDeclaration   : SignatureDeclarationBase, TypeElement {
            val kind: SyntaxKind.CallSignature
        }
    

        external interface ConstructSignatureDeclaration   : SignatureDeclarationBase, TypeElement {
            val kind: SyntaxKind.ConstructSignature
        }
    
typealias BindingName = /* Identifier | BindingPattern */

        external interface VariableDeclaration   : NamedDeclaration, JSDocContainer {
            val kind: SyntaxKind.VariableDeclaration
val parent: /* VariableDeclarationList | CatchClause */
val name: BindingName
val exclamationToken: ExclamationToken?
val type: TypeNode?
val initializer: Expression?
        }
    

        external interface VariableDeclarationList   : Node {
            val kind: SyntaxKind.VariableDeclarationList
val parent: /* VariableStatement | ForStatement | ForOfStatement | ForInStatement */
val declarations: NodeArray<VariableDeclaration>
        }
    

        external interface ParameterDeclaration   : NamedDeclaration, JSDocContainer {
            val kind: SyntaxKind.Parameter
val parent: SignatureDeclaration
val dotDotDotToken: DotDotDotToken?
val name: BindingName
val questionToken: QuestionToken?
val type: TypeNode?
val initializer: Expression?
        }
    

        external interface BindingElement   : NamedDeclaration {
            val kind: SyntaxKind.BindingElement
val parent: BindingPattern
val propertyName: PropertyName?
val dotDotDotToken: DotDotDotToken?
val name: BindingName
val initializer: Expression?
        }
    

        external interface PropertySignature   : TypeElement, JSDocContainer {
            val kind: SyntaxKind.PropertySignature
val name: PropertyName
val questionToken: QuestionToken?
val type: TypeNode?
var initializer: Expression?
        }
    

        external interface PropertyDeclaration   : ClassElement, JSDocContainer {
            val kind: SyntaxKind.PropertyDeclaration
val parent: ClassLikeDeclaration
val name: PropertyName
val questionToken: QuestionToken?
val exclamationToken: ExclamationToken?
val type: TypeNode?
val initializer: Expression?
        }
    

        external interface ObjectLiteralElement   : NamedDeclaration {
            var _objectLiteralBrand: Any?
val name: PropertyName?
        }
    
typealias ObjectLiteralElementLike = /* PropertyAssignment | ShorthandPropertyAssignment | SpreadAssignment | MethodDeclaration | AccessorDeclaration */

        external interface PropertyAssignment   : ObjectLiteralElement, JSDocContainer {
            val kind: SyntaxKind.PropertyAssignment
val parent: ObjectLiteralExpression
val name: PropertyName
val questionToken: QuestionToken?
val exclamationToken: ExclamationToken?
val initializer: Expression
        }
    

        external interface ShorthandPropertyAssignment   : ObjectLiteralElement, JSDocContainer {
            val kind: SyntaxKind.ShorthandPropertyAssignment
val parent: ObjectLiteralExpression
val name: Identifier
val questionToken: QuestionToken?
val exclamationToken: ExclamationToken?
val equalsToken: EqualsToken?
val objectAssignmentInitializer: Expression?
        }
    

        external interface SpreadAssignment   : ObjectLiteralElement, JSDocContainer {
            val kind: SyntaxKind.SpreadAssignment
val parent: ObjectLiteralExpression
val expression: Expression
        }
    
typealias VariableLikeDeclaration = /* VariableDeclaration | ParameterDeclaration | BindingElement | PropertyDeclaration | PropertyAssignment | PropertySignature | JsxAttribute | ShorthandPropertyAssignment | EnumMember | JSDocPropertyTag | JSDocParameterTag */

        external interface PropertyLikeDeclaration   : NamedDeclaration {
            val name: PropertyName
        }
    

        external interface ObjectBindingPattern   : Node {
            val kind: SyntaxKind.ObjectBindingPattern
val parent: /* VariableDeclaration | ParameterDeclaration | BindingElement */
val elements: NodeArray<BindingElement>
        }
    

        external interface ArrayBindingPattern   : Node {
            val kind: SyntaxKind.ArrayBindingPattern
val parent: /* VariableDeclaration | ParameterDeclaration | BindingElement */
val elements: NodeArray<ArrayBindingElement>
        }
    
typealias BindingPattern = /* ObjectBindingPattern | ArrayBindingPattern */
typealias ArrayBindingElement = /* BindingElement | OmittedExpression */

        external interface FunctionLikeDeclarationBase   : SignatureDeclarationBase {
            var _functionLikeDeclarationBrand: Any?
val asteriskToken: AsteriskToken?
val questionToken: QuestionToken?
val exclamationToken: ExclamationToken?
val body: /* Block | Expression */?
        }
    
typealias FunctionLikeDeclaration = /* FunctionDeclaration | MethodDeclaration | GetAccessorDeclaration | SetAccessorDeclaration | ConstructorDeclaration | FunctionExpression | ArrowFunction */
typealias FunctionLike = SignatureDeclaration

        external interface FunctionDeclaration   : FunctionLikeDeclarationBase, DeclarationStatement {
            val kind: SyntaxKind.FunctionDeclaration
val name: Identifier?
val body: FunctionBody?
        }
    

        external interface MethodSignature   : SignatureDeclarationBase, TypeElement {
            val kind: SyntaxKind.MethodSignature
val parent: ObjectTypeDeclaration
val name: PropertyName
        }
    

        external interface MethodDeclaration   : FunctionLikeDeclarationBase, ClassElement, ObjectLiteralElement, JSDocContainer {
            val kind: SyntaxKind.MethodDeclaration
val parent: /* ClassLikeDeclaration | ObjectLiteralExpression */
val name: PropertyName
val body: FunctionBody?
        }
    

        external interface ConstructorDeclaration   : FunctionLikeDeclarationBase, ClassElement, JSDocContainer {
            val kind: SyntaxKind.Constructor
val parent: ClassLikeDeclaration
val body: FunctionBody?
        }
    

        external interface SemicolonClassElement   : ClassElement {
            val kind: SyntaxKind.SemicolonClassElement
val parent: ClassLikeDeclaration
        }
    

        external interface GetAccessorDeclaration   : FunctionLikeDeclarationBase, ClassElement, TypeElement, ObjectLiteralElement, JSDocContainer {
            val kind: SyntaxKind.GetAccessor
val parent: /* ClassLikeDeclaration | ObjectLiteralExpression | TypeLiteralNode | InterfaceDeclaration */
val name: PropertyName
val body: FunctionBody?
        }
    

        external interface SetAccessorDeclaration   : FunctionLikeDeclarationBase, ClassElement, TypeElement, ObjectLiteralElement, JSDocContainer {
            val kind: SyntaxKind.SetAccessor
val parent: /* ClassLikeDeclaration | ObjectLiteralExpression | TypeLiteralNode | InterfaceDeclaration */
val name: PropertyName
val body: FunctionBody?
        }
    
typealias AccessorDeclaration = /* GetAccessorDeclaration | SetAccessorDeclaration */

        external interface IndexSignatureDeclaration   : SignatureDeclarationBase, ClassElement, TypeElement {
            val kind: SyntaxKind.IndexSignature
val parent: ObjectTypeDeclaration
val type: TypeNode
        }
    

        external interface ClassStaticBlockDeclaration   : ClassElement, JSDocContainer {
            val kind: SyntaxKind.ClassStaticBlockDeclaration
val parent: /* ClassDeclaration | ClassExpression */
val body: Block
        }
    

        external interface TypeNode   : Node {
            var _typeNodeBrand: Any?
        }
    

        external interface KeywordTypeNode <TKind : KeywordTypeSyntaxKind /* default is KeywordTypeSyntaxKind */>  : KeywordToken<TKind>, TypeNode {
            val kind: TKind
        }
    

        external interface ImportTypeNode   : NodeWithTypeArguments {
            val kind: SyntaxKind.ImportType
val isTypeOf: Boolean
val argument: TypeNode
val qualifier: EntityName?
        }
    

        external interface ThisTypeNode   : TypeNode {
            val kind: SyntaxKind.ThisType
        }
    
typealias FunctionOrConstructorTypeNode = /* FunctionTypeNode | ConstructorTypeNode */

        external interface FunctionOrConstructorTypeNodeBase   : TypeNode, SignatureDeclarationBase {
            val kind: /* SyntaxKind.FunctionType | SyntaxKind.ConstructorType */
val type: TypeNode
        }
    

        external interface FunctionTypeNode   : FunctionOrConstructorTypeNodeBase {
            val kind: SyntaxKind.FunctionType
        }
    

        external interface ConstructorTypeNode   : FunctionOrConstructorTypeNodeBase {
            val kind: SyntaxKind.ConstructorType
        }
    

        external interface NodeWithTypeArguments   : TypeNode {
            val typeArguments: NodeArray<TypeNode>?
        }
    
typealias TypeReferenceType = /* TypeReferenceNode | ExpressionWithTypeArguments */

        external interface TypeReferenceNode   : NodeWithTypeArguments {
            val kind: SyntaxKind.TypeReference
val typeName: EntityName
        }
    

        external interface TypePredicateNode   : TypeNode {
            val kind: SyntaxKind.TypePredicate
val parent: /* SignatureDeclaration | JSDocTypeExpression */
val assertsModifier: AssertsToken?
val parameterName: /* Identifier | ThisTypeNode */
val type: TypeNode?
        }
    

        external interface TypeQueryNode   : TypeNode {
            val kind: SyntaxKind.TypeQuery
val exprName: EntityName
        }
    

        external interface TypeLiteralNode   : TypeNode, Declaration {
            val kind: SyntaxKind.TypeLiteral
val members: NodeArray<TypeElement>
        }
    

        external interface ArrayTypeNode   : TypeNode {
            val kind: SyntaxKind.ArrayType
val elementType: TypeNode
        }
    

        external interface TupleTypeNode   : TypeNode {
            val kind: SyntaxKind.TupleType
val elements: NodeArray</* TypeNode | NamedTupleMember */>
        }
    

        external interface NamedTupleMember   : TypeNode, JSDocContainer, Declaration {
            val kind: SyntaxKind.NamedTupleMember
val dotDotDotToken: Token<SyntaxKind.DotDotDotToken>?
val name: Identifier
val questionToken: Token<SyntaxKind.QuestionToken>?
val type: TypeNode
        }
    

        external interface OptionalTypeNode   : TypeNode {
            val kind: SyntaxKind.OptionalType
val type: TypeNode
        }
    

        external interface RestTypeNode   : TypeNode {
            val kind: SyntaxKind.RestType
val type: TypeNode
        }
    
typealias UnionOrIntersectionTypeNode = /* UnionTypeNode | IntersectionTypeNode */

        external interface UnionTypeNode   : TypeNode {
            val kind: SyntaxKind.UnionType
val types: NodeArray<TypeNode>
        }
    

        external interface IntersectionTypeNode   : TypeNode {
            val kind: SyntaxKind.IntersectionType
val types: NodeArray<TypeNode>
        }
    

        external interface ConditionalTypeNode   : TypeNode {
            val kind: SyntaxKind.ConditionalType
val checkType: TypeNode
val extendsType: TypeNode
val trueType: TypeNode
val falseType: TypeNode
        }
    

        external interface InferTypeNode   : TypeNode {
            val kind: SyntaxKind.InferType
val typeParameter: TypeParameterDeclaration
        }
    

        external interface ParenthesizedTypeNode   : TypeNode {
            val kind: SyntaxKind.ParenthesizedType
val type: TypeNode
        }
    

        external interface TypeOperatorNode   : TypeNode {
            val kind: SyntaxKind.TypeOperator
val operator: /* SyntaxKind.KeyOfKeyword | SyntaxKind.UniqueKeyword | SyntaxKind.ReadonlyKeyword */
val type: TypeNode
        }
    

        external interface IndexedAccessTypeNode   : TypeNode {
            val kind: SyntaxKind.IndexedAccessType
val objectType: TypeNode
val indexType: TypeNode
        }
    

        external interface MappedTypeNode   : TypeNode, Declaration {
            val kind: SyntaxKind.MappedType
val readonlyToken: /* ReadonlyToken | PlusToken | MinusToken */?
val typeParameter: TypeParameterDeclaration
val nameType: TypeNode?
val questionToken: /* QuestionToken | PlusToken | MinusToken */?
val type: TypeNode?
        }
    

        external interface LiteralTypeNode   : TypeNode {
            val kind: SyntaxKind.LiteralType
val literal: /* NullLiteral | BooleanLiteral | LiteralExpression | PrefixUnaryExpression */
        }
    

        external interface StringLiteral   : LiteralExpression, Declaration {
            val kind: SyntaxKind.StringLiteral
        }
    
typealias StringLiteralLike = /* StringLiteral | NoSubstitutionTemplateLiteral */
typealias PropertyNameLiteral = /* Identifier | StringLiteralLike | NumericLiteral */

        external interface TemplateLiteralTypeNode   : TypeNode {
            var kind: SyntaxKind.TemplateLiteralType
val head: TemplateHead
val templateSpans: NodeArray<TemplateLiteralTypeSpan>
        }
    

        external interface TemplateLiteralTypeSpan   : TypeNode {
            val kind: SyntaxKind.TemplateLiteralTypeSpan
val parent: TemplateLiteralTypeNode
val type: TypeNode
val literal: /* TemplateMiddle | TemplateTail */
        }
    

        external interface Expression   : Node {
            var _expressionBrand: Any?
        }
    

        external interface OmittedExpression   : Expression {
            val kind: SyntaxKind.OmittedExpression
        }
    

        external interface PartiallyEmittedExpression   : LeftHandSideExpression {
            val kind: SyntaxKind.PartiallyEmittedExpression
val expression: Expression
        }
    

        external interface UnaryExpression   : Expression {
            var _unaryExpressionBrand: Any?
        }
    
typealias IncrementExpression = UpdateExpression

        external interface UpdateExpression   : UnaryExpression {
            var _updateExpressionBrand: Any?
        }
    
typealias PrefixUnaryOperator = /* SyntaxKind.PlusPlusToken | SyntaxKind.MinusMinusToken | SyntaxKind.PlusToken | SyntaxKind.MinusToken | SyntaxKind.TildeToken | SyntaxKind.ExclamationToken */

        external interface PrefixUnaryExpression   : UpdateExpression {
            val kind: SyntaxKind.PrefixUnaryExpression
val operator: PrefixUnaryOperator
val operand: UnaryExpression
        }
    
typealias PostfixUnaryOperator = /* SyntaxKind.PlusPlusToken | SyntaxKind.MinusMinusToken */

        external interface PostfixUnaryExpression   : UpdateExpression {
            val kind: SyntaxKind.PostfixUnaryExpression
val operand: LeftHandSideExpression
val operator: PostfixUnaryOperator
        }
    

        external interface LeftHandSideExpression   : UpdateExpression {
            var _leftHandSideExpressionBrand: Any?
        }
    

        external interface MemberExpression   : LeftHandSideExpression {
            var _memberExpressionBrand: Any?
        }
    

        external interface PrimaryExpression   : MemberExpression {
            var _primaryExpressionBrand: Any?
        }
    

        external interface NullLiteral   : PrimaryExpression {
            val kind: SyntaxKind.NullKeyword
        }
    

        external interface TrueLiteral   : PrimaryExpression {
            val kind: SyntaxKind.TrueKeyword
        }
    

        external interface FalseLiteral   : PrimaryExpression {
            val kind: SyntaxKind.FalseKeyword
        }
    
typealias BooleanLiteral = /* TrueLiteral | FalseLiteral */

        external interface ThisExpression   : PrimaryExpression {
            val kind: SyntaxKind.ThisKeyword
        }
    

        external interface SuperExpression   : PrimaryExpression {
            val kind: SyntaxKind.SuperKeyword
        }
    

        external interface ImportExpression   : PrimaryExpression {
            val kind: SyntaxKind.ImportKeyword
        }
    

        external interface DeleteExpression   : UnaryExpression {
            val kind: SyntaxKind.DeleteExpression
val expression: UnaryExpression
        }
    

        external interface TypeOfExpression   : UnaryExpression {
            val kind: SyntaxKind.TypeOfExpression
val expression: UnaryExpression
        }
    

        external interface VoidExpression   : UnaryExpression {
            val kind: SyntaxKind.VoidExpression
val expression: UnaryExpression
        }
    

        external interface AwaitExpression   : UnaryExpression {
            val kind: SyntaxKind.AwaitExpression
val expression: UnaryExpression
        }
    

        external interface YieldExpression   : Expression {
            val kind: SyntaxKind.YieldExpression
val asteriskToken: AsteriskToken?
val expression: Expression?
        }
    

        external interface SyntheticExpression   : Expression {
            val kind: SyntaxKind.SyntheticExpression
val isSpread: Boolean
val type: Type
val tupleNameSource: /* ParameterDeclaration | NamedTupleMember */?
        }
    
typealias ExponentiationOperator = SyntaxKind.AsteriskAsteriskToken
typealias MultiplicativeOperator = /* SyntaxKind.AsteriskToken | SyntaxKind.SlashToken | SyntaxKind.PercentToken */
typealias MultiplicativeOperatorOrHigher = /* ExponentiationOperator | MultiplicativeOperator */
typealias AdditiveOperator = /* SyntaxKind.PlusToken | SyntaxKind.MinusToken */
typealias AdditiveOperatorOrHigher = /* MultiplicativeOperatorOrHigher | AdditiveOperator */
typealias ShiftOperator = /* SyntaxKind.LessThanLessThanToken | SyntaxKind.GreaterThanGreaterThanToken | SyntaxKind.GreaterThanGreaterThanGreaterThanToken */
typealias ShiftOperatorOrHigher = /* AdditiveOperatorOrHigher | ShiftOperator */
typealias RelationalOperator = /* SyntaxKind.LessThanToken | SyntaxKind.LessThanEqualsToken | SyntaxKind.GreaterThanToken | SyntaxKind.GreaterThanEqualsToken | SyntaxKind.InstanceOfKeyword | SyntaxKind.InKeyword */
typealias RelationalOperatorOrHigher = /* ShiftOperatorOrHigher | RelationalOperator */
typealias EqualityOperator = /* SyntaxKind.EqualsEqualsToken | SyntaxKind.EqualsEqualsEqualsToken | SyntaxKind.ExclamationEqualsEqualsToken | SyntaxKind.ExclamationEqualsToken */
typealias EqualityOperatorOrHigher = /* RelationalOperatorOrHigher | EqualityOperator */
typealias BitwiseOperator = /* SyntaxKind.AmpersandToken | SyntaxKind.BarToken | SyntaxKind.CaretToken */
typealias BitwiseOperatorOrHigher = /* EqualityOperatorOrHigher | BitwiseOperator */
typealias LogicalOperator = /* SyntaxKind.AmpersandAmpersandToken | SyntaxKind.BarBarToken */
typealias LogicalOperatorOrHigher = /* BitwiseOperatorOrHigher | LogicalOperator */
typealias CompoundAssignmentOperator = /* SyntaxKind.PlusEqualsToken | SyntaxKind.MinusEqualsToken | SyntaxKind.AsteriskAsteriskEqualsToken | SyntaxKind.AsteriskEqualsToken | SyntaxKind.SlashEqualsToken | SyntaxKind.PercentEqualsToken | SyntaxKind.AmpersandEqualsToken | SyntaxKind.BarEqualsToken | SyntaxKind.CaretEqualsToken | SyntaxKind.LessThanLessThanEqualsToken | SyntaxKind.GreaterThanGreaterThanGreaterThanEqualsToken | SyntaxKind.GreaterThanGreaterThanEqualsToken | SyntaxKind.BarBarEqualsToken | SyntaxKind.AmpersandAmpersandEqualsToken | SyntaxKind.QuestionQuestionEqualsToken */
typealias AssignmentOperator = /* SyntaxKind.EqualsToken | CompoundAssignmentOperator */
typealias AssignmentOperatorOrHigher = /* SyntaxKind.QuestionQuestionToken | LogicalOperatorOrHigher | AssignmentOperator */
typealias BinaryOperator = /* AssignmentOperatorOrHigher | SyntaxKind.CommaToken */
typealias LogicalOrCoalescingAssignmentOperator = /* SyntaxKind.AmpersandAmpersandEqualsToken | SyntaxKind.BarBarEqualsToken | SyntaxKind.QuestionQuestionEqualsToken */
typealias BinaryOperatorToken = Token<BinaryOperator>

        external interface BinaryExpression   : Expression, Declaration {
            val kind: SyntaxKind.BinaryExpression
val left: Expression
val operatorToken: BinaryOperatorToken
val right: Expression
        }
    
typealias AssignmentOperatorToken = Token<AssignmentOperator>

        external interface AssignmentExpression <TOperator : AssignmentOperatorToken >  : BinaryExpression {
            val left: LeftHandSideExpression
val operatorToken: TOperator
        }
    

        external interface ObjectDestructuringAssignment   : AssignmentExpression<EqualsToken> {
            val left: ObjectLiteralExpression
        }
    

        external interface ArrayDestructuringAssignment   : AssignmentExpression<EqualsToken> {
            val left: ArrayLiteralExpression
        }
    
typealias DestructuringAssignment = /* ObjectDestructuringAssignment | ArrayDestructuringAssignment */
typealias BindingOrAssignmentElement = /* VariableDeclaration | ParameterDeclaration | ObjectBindingOrAssignmentElement | ArrayBindingOrAssignmentElement */
typealias ObjectBindingOrAssignmentElement = /* BindingElement | PropertyAssignment | ShorthandPropertyAssignment | SpreadAssignment */
typealias ArrayBindingOrAssignmentElement = /* BindingElement | OmittedExpression | SpreadElement | ArrayLiteralExpression | ObjectLiteralExpression | AssignmentExpression<EqualsToken> | Identifier | PropertyAccessExpression | ElementAccessExpression */
typealias BindingOrAssignmentElementRestIndicator = /* DotDotDotToken | SpreadElement | SpreadAssignment */
typealias BindingOrAssignmentElementTarget = /* BindingOrAssignmentPattern | Identifier | PropertyAccessExpression | ElementAccessExpression | OmittedExpression */
typealias ObjectBindingOrAssignmentPattern = /* ObjectBindingPattern | ObjectLiteralExpression */
typealias ArrayBindingOrAssignmentPattern = /* ArrayBindingPattern | ArrayLiteralExpression */
typealias AssignmentPattern = /* ObjectLiteralExpression | ArrayLiteralExpression */
typealias BindingOrAssignmentPattern = /* ObjectBindingOrAssignmentPattern | ArrayBindingOrAssignmentPattern */

        external interface ConditionalExpression   : Expression {
            val kind: SyntaxKind.ConditionalExpression
val condition: Expression
val questionToken: QuestionToken
val whenTrue: Expression
val colonToken: ColonToken
val whenFalse: Expression
        }
    
typealias FunctionBody = Block
typealias ConciseBody = /* FunctionBody | Expression */

        external interface FunctionExpression   : PrimaryExpression, FunctionLikeDeclarationBase, JSDocContainer {
            val kind: SyntaxKind.FunctionExpression
val name: Identifier?
val body: FunctionBody
        }
    

        external interface ArrowFunction   : Expression, FunctionLikeDeclarationBase, JSDocContainer {
            val kind: SyntaxKind.ArrowFunction
val equalsGreaterThanToken: EqualsGreaterThanToken
val body: ConciseBody
val name: Nothing
        }
    

        external interface LiteralLikeNode   : Node {
            var text: String
var isUnterminated: Boolean?
var hasExtendedUnicodeEscape: Boolean?
        }
    

        external interface TemplateLiteralLikeNode   : LiteralLikeNode {
            var rawText: String?
        }
    

        external interface LiteralExpression   : LiteralLikeNode, PrimaryExpression {
            var _literalExpressionBrand: Any?
        }
    

        external interface RegularExpressionLiteral   : LiteralExpression {
            val kind: SyntaxKind.RegularExpressionLiteral
        }
    

        external interface NoSubstitutionTemplateLiteral   : LiteralExpression, TemplateLiteralLikeNode, Declaration {
            val kind: SyntaxKind.NoSubstitutionTemplateLiteral
        }
    

        external enum class TokenFlags {
            None,
Scientific,
Octal,
HexSpecifier,
BinarySpecifier,
OctalSpecifier
        }
    

        external interface NumericLiteral   : LiteralExpression, Declaration {
            val kind: SyntaxKind.NumericLiteral
        }
    

        external interface BigIntLiteral   : LiteralExpression {
            val kind: SyntaxKind.BigIntLiteral
        }
    
typealias LiteralToken = /* NumericLiteral | BigIntLiteral | StringLiteral | JsxText | RegularExpressionLiteral | NoSubstitutionTemplateLiteral */

        external interface TemplateHead   : TemplateLiteralLikeNode {
            val kind: SyntaxKind.TemplateHead
val parent: /* TemplateExpression | TemplateLiteralTypeNode */
        }
    

        external interface TemplateMiddle   : TemplateLiteralLikeNode {
            val kind: SyntaxKind.TemplateMiddle
val parent: /* TemplateSpan | TemplateLiteralTypeSpan */
        }
    

        external interface TemplateTail   : TemplateLiteralLikeNode {
            val kind: SyntaxKind.TemplateTail
val parent: /* TemplateSpan | TemplateLiteralTypeSpan */
        }
    
typealias PseudoLiteralToken = /* TemplateHead | TemplateMiddle | TemplateTail */
typealias TemplateLiteralToken = /* NoSubstitutionTemplateLiteral | PseudoLiteralToken */

        external interface TemplateExpression   : PrimaryExpression {
            val kind: SyntaxKind.TemplateExpression
val head: TemplateHead
val templateSpans: NodeArray<TemplateSpan>
        }
    
typealias TemplateLiteral = /* TemplateExpression | NoSubstitutionTemplateLiteral */

        external interface TemplateSpan   : Node {
            val kind: SyntaxKind.TemplateSpan
val parent: TemplateExpression
val expression: Expression
val literal: /* TemplateMiddle | TemplateTail */
        }
    

        external interface ParenthesizedExpression   : PrimaryExpression, JSDocContainer {
            val kind: SyntaxKind.ParenthesizedExpression
val expression: Expression
        }
    

        external interface ArrayLiteralExpression   : PrimaryExpression {
            val kind: SyntaxKind.ArrayLiteralExpression
val elements: NodeArray<Expression>
        }
    

        external interface SpreadElement   : Expression {
            val kind: SyntaxKind.SpreadElement
val parent: /* ArrayLiteralExpression | CallExpression | NewExpression */
val expression: Expression
        }
    

        external interface ObjectLiteralExpressionBase <T : ObjectLiteralElement >  : PrimaryExpression, Declaration {
            val properties: NodeArray<T>
        }
    

        external interface ObjectLiteralExpression   : ObjectLiteralExpressionBase<ObjectLiteralElementLike> {
            val kind: SyntaxKind.ObjectLiteralExpression
        }
    
typealias EntityNameExpression = /* Identifier | PropertyAccessEntityNameExpression */
typealias EntityNameOrEntityNameExpression = /* EntityName | EntityNameExpression */
typealias AccessExpression = /* PropertyAccessExpression | ElementAccessExpression */

        external interface PropertyAccessExpression   : MemberExpression, NamedDeclaration {
            val kind: SyntaxKind.PropertyAccessExpression
val expression: LeftHandSideExpression
val questionDotToken: QuestionDotToken?
val name: MemberName
        }
    

        external interface PropertyAccessChain   : PropertyAccessExpression {
            var _optionalChainBrand: Any?
val name: MemberName
        }
    

        external interface SuperPropertyAccessExpression   : PropertyAccessExpression {
            val expression: SuperExpression
        }
    

        external interface PropertyAccessEntityNameExpression   : PropertyAccessExpression {
            var _propertyAccessExpressionLikeQualifiedNameBrand: Any??
val expression: EntityNameExpression
val name: Identifier
        }
    

        external interface ElementAccessExpression   : MemberExpression {
            val kind: SyntaxKind.ElementAccessExpression
val expression: LeftHandSideExpression
val questionDotToken: QuestionDotToken?
val argumentExpression: Expression
        }
    

        external interface ElementAccessChain   : ElementAccessExpression {
            var _optionalChainBrand: Any?
        }
    

        external interface SuperElementAccessExpression   : ElementAccessExpression {
            val expression: SuperExpression
        }
    
typealias SuperProperty = /* SuperPropertyAccessExpression | SuperElementAccessExpression */

        external interface CallExpression   : LeftHandSideExpression, Declaration {
            val kind: SyntaxKind.CallExpression
val expression: LeftHandSideExpression
val questionDotToken: QuestionDotToken?
val typeArguments: NodeArray<TypeNode>?
val arguments: NodeArray<Expression>
        }
    

        external interface CallChain   : CallExpression {
            var _optionalChainBrand: Any?
        }
    
typealias OptionalChain = /* PropertyAccessChain | ElementAccessChain | CallChain | NonNullChain */

        external interface SuperCall   : CallExpression {
            val expression: SuperExpression
        }
    

        external interface ImportCall   : CallExpression {
            val expression: ImportExpression
        }
    

        external interface ExpressionWithTypeArguments   : NodeWithTypeArguments {
            val kind: SyntaxKind.ExpressionWithTypeArguments
val parent: /* HeritageClause | JSDocAugmentsTag | JSDocImplementsTag */
val expression: LeftHandSideExpression
        }
    

        external interface NewExpression   : PrimaryExpression, Declaration {
            val kind: SyntaxKind.NewExpression
val expression: LeftHandSideExpression
val typeArguments: NodeArray<TypeNode>?
val arguments: NodeArray<Expression>?
        }
    

        external interface TaggedTemplateExpression   : MemberExpression {
            val kind: SyntaxKind.TaggedTemplateExpression
val tag: LeftHandSideExpression
val typeArguments: NodeArray<TypeNode>?
val template: TemplateLiteral
        }
    
typealias CallLikeExpression = /* CallExpression | NewExpression | TaggedTemplateExpression | Decorator | JsxOpeningLikeElement */

        external interface AsExpression   : Expression {
            val kind: SyntaxKind.AsExpression
val expression: Expression
val type: TypeNode
        }
    

        external interface TypeAssertion   : UnaryExpression {
            val kind: SyntaxKind.TypeAssertionExpression
val type: TypeNode
val expression: UnaryExpression
        }
    
typealias AssertionExpression = /* TypeAssertion | AsExpression */

        external interface NonNullExpression   : LeftHandSideExpression {
            val kind: SyntaxKind.NonNullExpression
val expression: Expression
        }
    

        external interface NonNullChain   : NonNullExpression {
            var _optionalChainBrand: Any?
        }
    

        external interface MetaProperty   : PrimaryExpression {
            val kind: SyntaxKind.MetaProperty
val keywordToken: /* SyntaxKind.NewKeyword | SyntaxKind.ImportKeyword */
val name: Identifier
        }
    

        external interface JsxElement   : PrimaryExpression {
            val kind: SyntaxKind.JsxElement
val openingElement: JsxOpeningElement
val children: NodeArray<JsxChild>
val closingElement: JsxClosingElement
        }
    
typealias JsxOpeningLikeElement = /* JsxSelfClosingElement | JsxOpeningElement */
typealias JsxAttributeLike = /* JsxAttribute | JsxSpreadAttribute */
typealias JsxTagNameExpression = /* Identifier | ThisExpression | JsxTagNamePropertyAccess */

        external interface JsxTagNamePropertyAccess   : PropertyAccessExpression {
            val expression: JsxTagNameExpression
        }
    

        external interface JsxAttributes   : ObjectLiteralExpressionBase<JsxAttributeLike> {
            val kind: SyntaxKind.JsxAttributes
val parent: JsxOpeningLikeElement
        }
    

        external interface JsxOpeningElement   : Expression {
            val kind: SyntaxKind.JsxOpeningElement
val parent: JsxElement
val tagName: JsxTagNameExpression
val typeArguments: NodeArray<TypeNode>?
val attributes: JsxAttributes
        }
    

        external interface JsxSelfClosingElement   : PrimaryExpression {
            val kind: SyntaxKind.JsxSelfClosingElement
val tagName: JsxTagNameExpression
val typeArguments: NodeArray<TypeNode>?
val attributes: JsxAttributes
        }
    

        external interface JsxFragment   : PrimaryExpression {
            val kind: SyntaxKind.JsxFragment
val openingFragment: JsxOpeningFragment
val children: NodeArray<JsxChild>
val closingFragment: JsxClosingFragment
        }
    

        external interface JsxOpeningFragment   : Expression {
            val kind: SyntaxKind.JsxOpeningFragment
val parent: JsxFragment
        }
    

        external interface JsxClosingFragment   : Expression {
            val kind: SyntaxKind.JsxClosingFragment
val parent: JsxFragment
        }
    

        external interface JsxAttribute   : ObjectLiteralElement {
            val kind: SyntaxKind.JsxAttribute
val parent: JsxAttributes
val name: Identifier
val initializer: /* StringLiteral | JsxExpression */?
        }
    

        external interface JsxSpreadAttribute   : ObjectLiteralElement {
            val kind: SyntaxKind.JsxSpreadAttribute
val parent: JsxAttributes
val expression: Expression
        }
    

        external interface JsxClosingElement   : Node {
            val kind: SyntaxKind.JsxClosingElement
val parent: JsxElement
val tagName: JsxTagNameExpression
        }
    

        external interface JsxExpression   : Expression {
            val kind: SyntaxKind.JsxExpression
val parent: /* JsxElement | JsxAttributeLike */
val dotDotDotToken: Token<SyntaxKind.DotDotDotToken>?
val expression: Expression?
        }
    

        external interface JsxText   : LiteralLikeNode {
            val kind: SyntaxKind.JsxText
val parent: JsxElement
val containsOnlyTriviaWhiteSpaces: Boolean
        }
    
typealias JsxChild = /* JsxText | JsxExpression | JsxElement | JsxSelfClosingElement | JsxFragment */

        external interface Statement   : Node, JSDocContainer {
            var _statementBrand: Any?
        }
    

        external interface NotEmittedStatement   : Statement {
            val kind: SyntaxKind.NotEmittedStatement
        }
    

        external interface CommaListExpression   : Expression {
            val kind: SyntaxKind.CommaListExpression
val elements: NodeArray<Expression>
        }
    

        external interface EmptyStatement   : Statement {
            val kind: SyntaxKind.EmptyStatement
        }
    

        external interface DebuggerStatement   : Statement {
            val kind: SyntaxKind.DebuggerStatement
        }
    

        external interface MissingDeclaration   : DeclarationStatement {
            val kind: SyntaxKind.MissingDeclaration
val name: Identifier?
        }
    
typealias BlockLike = /* SourceFile | Block | ModuleBlock | CaseOrDefaultClause */

        external interface Block   : Statement {
            val kind: SyntaxKind.Block
val statements: NodeArray<Statement>
        }
    

        external interface VariableStatement   : Statement {
            val kind: SyntaxKind.VariableStatement
val declarationList: VariableDeclarationList
        }
    

        external interface ExpressionStatement   : Statement {
            val kind: SyntaxKind.ExpressionStatement
val expression: Expression
        }
    

        external interface IfStatement   : Statement {
            val kind: SyntaxKind.IfStatement
val expression: Expression
val thenStatement: Statement
val elseStatement: Statement?
        }
    

        external interface IterationStatement   : Statement {
            val statement: Statement
        }
    

        external interface DoStatement   : IterationStatement {
            val kind: SyntaxKind.DoStatement
val expression: Expression
        }
    

        external interface WhileStatement   : IterationStatement {
            val kind: SyntaxKind.WhileStatement
val expression: Expression
        }
    
typealias ForInitializer = /* VariableDeclarationList | Expression */

        external interface ForStatement   : IterationStatement {
            val kind: SyntaxKind.ForStatement
val initializer: ForInitializer?
val condition: Expression?
val incrementor: Expression?
        }
    
typealias ForInOrOfStatement = /* ForInStatement | ForOfStatement */

        external interface ForInStatement   : IterationStatement {
            val kind: SyntaxKind.ForInStatement
val initializer: ForInitializer
val expression: Expression
        }
    

        external interface ForOfStatement   : IterationStatement {
            val kind: SyntaxKind.ForOfStatement
val awaitModifier: AwaitKeywordToken?
val initializer: ForInitializer
val expression: Expression
        }
    

        external interface BreakStatement   : Statement {
            val kind: SyntaxKind.BreakStatement
val label: Identifier?
        }
    

        external interface ContinueStatement   : Statement {
            val kind: SyntaxKind.ContinueStatement
val label: Identifier?
        }
    
typealias BreakOrContinueStatement = /* BreakStatement | ContinueStatement */

        external interface ReturnStatement   : Statement {
            val kind: SyntaxKind.ReturnStatement
val expression: Expression?
        }
    

        external interface WithStatement   : Statement {
            val kind: SyntaxKind.WithStatement
val expression: Expression
val statement: Statement
        }
    

        external interface SwitchStatement   : Statement {
            val kind: SyntaxKind.SwitchStatement
val expression: Expression
val caseBlock: CaseBlock
var possiblyExhaustive: Boolean?
        }
    

        external interface CaseBlock   : Node {
            val kind: SyntaxKind.CaseBlock
val parent: SwitchStatement
val clauses: NodeArray<CaseOrDefaultClause>
        }
    

        external interface CaseClause   : Node {
            val kind: SyntaxKind.CaseClause
val parent: CaseBlock
val expression: Expression
val statements: NodeArray<Statement>
        }
    

        external interface DefaultClause   : Node {
            val kind: SyntaxKind.DefaultClause
val parent: CaseBlock
val statements: NodeArray<Statement>
        }
    
typealias CaseOrDefaultClause = /* CaseClause | DefaultClause */

        external interface LabeledStatement   : Statement {
            val kind: SyntaxKind.LabeledStatement
val label: Identifier
val statement: Statement
        }
    

        external interface ThrowStatement   : Statement {
            val kind: SyntaxKind.ThrowStatement
val expression: Expression
        }
    

        external interface TryStatement   : Statement {
            val kind: SyntaxKind.TryStatement
val tryBlock: Block
val catchClause: CatchClause?
val finallyBlock: Block?
        }
    

        external interface CatchClause   : Node {
            val kind: SyntaxKind.CatchClause
val parent: TryStatement
val variableDeclaration: VariableDeclaration?
val block: Block
        }
    
typealias ObjectTypeDeclaration = /* ClassLikeDeclaration | InterfaceDeclaration | TypeLiteralNode */
typealias DeclarationWithTypeParameters = /* DeclarationWithTypeParameterChildren | JSDocTypedefTag | JSDocCallbackTag | JSDocSignature */
typealias DeclarationWithTypeParameterChildren = /* SignatureDeclaration | ClassLikeDeclaration | InterfaceDeclaration | TypeAliasDeclaration | JSDocTemplateTag */

        external interface ClassLikeDeclarationBase   : NamedDeclaration, JSDocContainer {
            val kind: /* SyntaxKind.ClassDeclaration | SyntaxKind.ClassExpression */
val name: Identifier?
val typeParameters: NodeArray<TypeParameterDeclaration>?
val heritageClauses: NodeArray<HeritageClause>?
val members: NodeArray<ClassElement>
        }
    

        external interface ClassDeclaration   : ClassLikeDeclarationBase, DeclarationStatement {
            val kind: SyntaxKind.ClassDeclaration
val name: Identifier?
        }
    

        external interface ClassExpression   : ClassLikeDeclarationBase, PrimaryExpression {
            val kind: SyntaxKind.ClassExpression
        }
    
typealias ClassLikeDeclaration = /* ClassDeclaration | ClassExpression */

        external interface ClassElement   : NamedDeclaration {
            var _classElementBrand: Any?
val name: PropertyName?
        }
    

        external interface TypeElement   : NamedDeclaration {
            var _typeElementBrand: Any?
val name: PropertyName?
val questionToken: QuestionToken?
        }
    

        external interface InterfaceDeclaration   : DeclarationStatement, JSDocContainer {
            val kind: SyntaxKind.InterfaceDeclaration
val name: Identifier
val typeParameters: NodeArray<TypeParameterDeclaration>?
val heritageClauses: NodeArray<HeritageClause>?
val members: NodeArray<TypeElement>
        }
    

        external interface HeritageClause   : Node {
            val kind: SyntaxKind.HeritageClause
val parent: /* InterfaceDeclaration | ClassLikeDeclaration */
val token: /* SyntaxKind.ExtendsKeyword | SyntaxKind.ImplementsKeyword */
val types: NodeArray<ExpressionWithTypeArguments>
        }
    

        external interface TypeAliasDeclaration   : DeclarationStatement, JSDocContainer {
            val kind: SyntaxKind.TypeAliasDeclaration
val name: Identifier
val typeParameters: NodeArray<TypeParameterDeclaration>?
val type: TypeNode
        }
    

        external interface EnumMember   : NamedDeclaration, JSDocContainer {
            val kind: SyntaxKind.EnumMember
val parent: EnumDeclaration
val name: PropertyName
val initializer: Expression?
        }
    

        external interface EnumDeclaration   : DeclarationStatement, JSDocContainer {
            val kind: SyntaxKind.EnumDeclaration
val name: Identifier
val members: NodeArray<EnumMember>
        }
    
typealias ModuleName = /* Identifier | StringLiteral */
typealias ModuleBody = /* NamespaceBody | JSDocNamespaceBody */

        external interface ModuleDeclaration   : DeclarationStatement, JSDocContainer {
            val kind: SyntaxKind.ModuleDeclaration
val parent: /* ModuleBody | SourceFile */
val name: ModuleName
val body: /* ModuleBody | JSDocNamespaceDeclaration */?
        }
    
typealias NamespaceBody = /* ModuleBlock | NamespaceDeclaration */

        external interface NamespaceDeclaration   : ModuleDeclaration {
            val name: Identifier
val body: NamespaceBody
        }
    
typealias JSDocNamespaceBody = /* Identifier | JSDocNamespaceDeclaration */

        external interface JSDocNamespaceDeclaration   : ModuleDeclaration {
            val name: Identifier
val body: JSDocNamespaceBody?
        }
    

        external interface ModuleBlock   : Node, Statement {
            val kind: SyntaxKind.ModuleBlock
val parent: ModuleDeclaration
val statements: NodeArray<Statement>
        }
    
typealias ModuleReference = /* EntityName | ExternalModuleReference */

        external interface ImportEqualsDeclaration   : DeclarationStatement, JSDocContainer {
            val kind: SyntaxKind.ImportEqualsDeclaration
val parent: /* SourceFile | ModuleBlock */
val name: Identifier
val isTypeOnly: Boolean
val moduleReference: ModuleReference
        }
    

        external interface ExternalModuleReference   : Node {
            val kind: SyntaxKind.ExternalModuleReference
val parent: ImportEqualsDeclaration
val expression: Expression
        }
    

        external interface ImportDeclaration   : Statement {
            val kind: SyntaxKind.ImportDeclaration
val parent: /* SourceFile | ModuleBlock */
val importClause: ImportClause?
val moduleSpecifier: Expression
        }
    
typealias NamedImportBindings = /* NamespaceImport | NamedImports */
typealias NamedExportBindings = /* NamespaceExport | NamedExports */

        external interface ImportClause   : NamedDeclaration {
            val kind: SyntaxKind.ImportClause
val parent: ImportDeclaration
val isTypeOnly: Boolean
val name: Identifier?
val namedBindings: NamedImportBindings?
        }
    

        external interface NamespaceImport   : NamedDeclaration {
            val kind: SyntaxKind.NamespaceImport
val parent: ImportClause
val name: Identifier
        }
    

        external interface NamespaceExport   : NamedDeclaration {
            val kind: SyntaxKind.NamespaceExport
val parent: ExportDeclaration
val name: Identifier
        }
    

        external interface NamespaceExportDeclaration   : DeclarationStatement, JSDocContainer {
            val kind: SyntaxKind.NamespaceExportDeclaration
val name: Identifier
        }
    

        external interface ExportDeclaration   : DeclarationStatement, JSDocContainer {
            val kind: SyntaxKind.ExportDeclaration
val parent: /* SourceFile | ModuleBlock */
val isTypeOnly: Boolean
val exportClause: NamedExportBindings?
val moduleSpecifier: Expression?
        }
    

        external interface NamedImports   : Node {
            val kind: SyntaxKind.NamedImports
val parent: ImportClause
val elements: NodeArray<ImportSpecifier>
        }
    

        external interface NamedExports   : Node {
            val kind: SyntaxKind.NamedExports
val parent: ExportDeclaration
val elements: NodeArray<ExportSpecifier>
        }
    
typealias NamedImportsOrExports = /* NamedImports | NamedExports */

        external interface ImportSpecifier   : NamedDeclaration {
            val kind: SyntaxKind.ImportSpecifier
val parent: NamedImports
val propertyName: Identifier?
val name: Identifier
        }
    

        external interface ExportSpecifier   : NamedDeclaration {
            val kind: SyntaxKind.ExportSpecifier
val parent: NamedExports
val propertyName: Identifier?
val name: Identifier
        }
    
typealias ImportOrExportSpecifier = /* ImportSpecifier | ExportSpecifier */
typealias TypeOnlyCompatibleAliasDeclaration = /* ImportClause | ImportEqualsDeclaration | NamespaceImport | ImportOrExportSpecifier */

        external interface ExportAssignment   : DeclarationStatement, JSDocContainer {
            val kind: SyntaxKind.ExportAssignment
val parent: SourceFile
val isExportEquals: Boolean?
val expression: Expression
        }
    

        external interface FileReference   : TextRange {
            var fileName: String
        }
    

        external interface CheckJsDirective   : TextRange {
            var enabled: Boolean
        }
    
typealias CommentKind = /* SyntaxKind.SingleLineCommentTrivia | SyntaxKind.MultiLineCommentTrivia */

        external interface CommentRange   : TextRange {
            var hasTrailingNewLine: Boolean?
var kind: CommentKind
        }
    

        external interface SynthesizedComment   : CommentRange {
            var text: String
var pos: 40[object Object]
var end: 40[object Object]
var hasLeadingNewline: Boolean?
        }
    

        external interface JSDocTypeExpression   : TypeNode {
            val kind: SyntaxKind.JSDocTypeExpression
val type: TypeNode
        }
    

        external interface JSDocNameReference   : Node {
            val kind: SyntaxKind.JSDocNameReference
val name: /* EntityName | JSDocMemberName */
        }
    

        external interface JSDocMemberName   : Node {
            val kind: SyntaxKind.JSDocMemberName
val left: /* EntityName | JSDocMemberName */
val right: Identifier
        }
    

        external interface JSDocType   : TypeNode {
            var _jsDocTypeBrand: Any?
        }
    

        external interface JSDocAllType   : JSDocType {
            val kind: SyntaxKind.JSDocAllType
        }
    

        external interface JSDocUnknownType   : JSDocType {
            val kind: SyntaxKind.JSDocUnknownType
        }
    

        external interface JSDocNonNullableType   : JSDocType {
            val kind: SyntaxKind.JSDocNonNullableType
val type: TypeNode
        }
    

        external interface JSDocNullableType   : JSDocType {
            val kind: SyntaxKind.JSDocNullableType
val type: TypeNode
        }
    

        external interface JSDocOptionalType   : JSDocType {
            val kind: SyntaxKind.JSDocOptionalType
val type: TypeNode
        }
    

        external interface JSDocFunctionType   : JSDocType, SignatureDeclarationBase {
            val kind: SyntaxKind.JSDocFunctionType
        }
    

        external interface JSDocVariadicType   : JSDocType {
            val kind: SyntaxKind.JSDocVariadicType
val type: TypeNode
        }
    

        external interface JSDocNamepathType   : JSDocType {
            val kind: SyntaxKind.JSDocNamepathType
val type: TypeNode
        }
    
typealias JSDocTypeReferencingNode = /* JSDocVariadicType | JSDocOptionalType | JSDocNullableType | JSDocNonNullableType */

        external interface JSDoc   : Node {
            val kind: SyntaxKind.JSDocComment
val parent: HasJSDoc
val tags: NodeArray<JSDocTag>?
val comment: /* string | NodeArray<JSDocComment> */?
        }
    

        external interface JSDocTag   : Node {
            val parent: /* JSDoc | JSDocTypeLiteral */
val tagName: Identifier
val comment: /* string | NodeArray<JSDocComment> */?
        }
    

        external interface JSDocLink   : Node {
            val kind: SyntaxKind.JSDocLink
val name: /* EntityName | JSDocMemberName */?
var text: String
        }
    

        external interface JSDocLinkCode   : Node {
            val kind: SyntaxKind.JSDocLinkCode
val name: /* EntityName | JSDocMemberName */?
var text: String
        }
    

        external interface JSDocLinkPlain   : Node {
            val kind: SyntaxKind.JSDocLinkPlain
val name: /* EntityName | JSDocMemberName */?
var text: String
        }
    
typealias JSDocComment = /* JSDocText | JSDocLink | JSDocLinkCode | JSDocLinkPlain */

        external interface JSDocText   : Node {
            val kind: SyntaxKind.JSDocText
var text: String
        }
    

        external interface JSDocUnknownTag   : JSDocTag {
            val kind: SyntaxKind.JSDocTag
        }
    

        external interface JSDocAugmentsTag   : JSDocTag {
            val kind: SyntaxKind.JSDocAugmentsTag
val class: /* ExpressionWithTypeArguments & {
            readonly expression: Identifier | PropertyAccessEntityNameExpression;
        } */
        }
    

        external interface JSDocImplementsTag   : JSDocTag {
            val kind: SyntaxKind.JSDocImplementsTag
val class: /* ExpressionWithTypeArguments & {
            readonly expression: Identifier | PropertyAccessEntityNameExpression;
        } */
        }
    

        external interface JSDocAuthorTag   : JSDocTag {
            val kind: SyntaxKind.JSDocAuthorTag
        }
    

        external interface JSDocDeprecatedTag   : JSDocTag {
            var kind: SyntaxKind.JSDocDeprecatedTag
        }
    

        external interface JSDocClassTag   : JSDocTag {
            val kind: SyntaxKind.JSDocClassTag
        }
    

        external interface JSDocPublicTag   : JSDocTag {
            val kind: SyntaxKind.JSDocPublicTag
        }
    

        external interface JSDocPrivateTag   : JSDocTag {
            val kind: SyntaxKind.JSDocPrivateTag
        }
    

        external interface JSDocProtectedTag   : JSDocTag {
            val kind: SyntaxKind.JSDocProtectedTag
        }
    

        external interface JSDocReadonlyTag   : JSDocTag {
            val kind: SyntaxKind.JSDocReadonlyTag
        }
    

        external interface JSDocOverrideTag   : JSDocTag {
            val kind: SyntaxKind.JSDocOverrideTag
        }
    

        external interface JSDocEnumTag   : JSDocTag, Declaration {
            val kind: SyntaxKind.JSDocEnumTag
val parent: JSDoc
val typeExpression: JSDocTypeExpression
        }
    

        external interface JSDocThisTag   : JSDocTag {
            val kind: SyntaxKind.JSDocThisTag
val typeExpression: JSDocTypeExpression
        }
    

        external interface JSDocTemplateTag   : JSDocTag {
            val kind: SyntaxKind.JSDocTemplateTag
val constraint: JSDocTypeExpression?
val typeParameters: NodeArray<TypeParameterDeclaration>
        }
    

        external interface JSDocSeeTag   : JSDocTag {
            val kind: SyntaxKind.JSDocSeeTag
val name: JSDocNameReference?
        }
    

        external interface JSDocReturnTag   : JSDocTag {
            val kind: SyntaxKind.JSDocReturnTag
val typeExpression: JSDocTypeExpression?
        }
    

        external interface JSDocTypeTag   : JSDocTag {
            val kind: SyntaxKind.JSDocTypeTag
val typeExpression: JSDocTypeExpression
        }
    

        external interface JSDocTypedefTag   : JSDocTag, NamedDeclaration {
            val kind: SyntaxKind.JSDocTypedefTag
val parent: JSDoc
val fullName: /* JSDocNamespaceDeclaration | Identifier */?
val name: Identifier?
val typeExpression: /* JSDocTypeExpression | JSDocTypeLiteral */?
        }
    

        external interface JSDocCallbackTag   : JSDocTag, NamedDeclaration {
            val kind: SyntaxKind.JSDocCallbackTag
val parent: JSDoc
val fullName: /* JSDocNamespaceDeclaration | Identifier */?
val name: Identifier?
val typeExpression: JSDocSignature
        }
    

        external interface JSDocSignature   : JSDocType, Declaration {
            val kind: SyntaxKind.JSDocSignature
val typeParameters: Array<out JSDocTemplateTag>?
val parameters: Array<out JSDocParameterTag>
val type: JSDocReturnTag?
        }
    

        external interface JSDocPropertyLikeTag   : JSDocTag, Declaration {
            val parent: JSDoc
val name: EntityName
val typeExpression: JSDocTypeExpression?
val isNameFirst: Boolean
val isBracketed: Boolean
        }
    

        external interface JSDocPropertyTag   : JSDocPropertyLikeTag {
            val kind: SyntaxKind.JSDocPropertyTag
        }
    

        external interface JSDocParameterTag   : JSDocPropertyLikeTag {
            val kind: SyntaxKind.JSDocParameterTag
        }
    

        external interface JSDocTypeLiteral   : JSDocType {
            val kind: SyntaxKind.JSDocTypeLiteral
val jsDocPropertyTags: Array<out JSDocPropertyLikeTag>?
val isArrayType: Boolean
        }
    

        external enum class FlowFlags {
            Unreachable,
Start,
BranchLabel,
LoopLabel,
Assignment,
TrueCondition,
FalseCondition,
SwitchClause,
ArrayMutation,
Call,
ReduceLabel,
Referenced,
Shared,
Label,
Condition
        }
    
typealias FlowNode = /* FlowStart | FlowLabel | FlowAssignment | FlowCall | FlowCondition | FlowSwitchClause | FlowArrayMutation | FlowCall | FlowReduceLabel */

        external interface FlowNodeBase   {
            var flags: FlowFlags
var id: Double?
        }
    

        external interface FlowStart   : FlowNodeBase {
            var node: /* FunctionExpression | ArrowFunction | MethodDeclaration */?
        }
    

        external interface FlowLabel   : FlowNodeBase {
            var antecedents: Array<FlowNode>?
        }
    

        external interface FlowAssignment   : FlowNodeBase {
            var node: /* Expression | VariableDeclaration | BindingElement */
var antecedent: FlowNode
        }
    

        external interface FlowCall   : FlowNodeBase {
            var node: CallExpression
var antecedent: FlowNode
        }
    

        external interface FlowCondition   : FlowNodeBase {
            var node: Expression
var antecedent: FlowNode
        }
    

        external interface FlowSwitchClause   : FlowNodeBase {
            var switchStatement: SwitchStatement
var clauseStart: Double
var clauseEnd: Double
var antecedent: FlowNode
        }
    

        external interface FlowArrayMutation   : FlowNodeBase {
            var node: /* CallExpression | BinaryExpression */
var antecedent: FlowNode
        }
    

        external interface FlowReduceLabel   : FlowNodeBase {
            var target: FlowLabel
var antecedents: Array<FlowNode>
var antecedent: FlowNode
        }
    
typealias FlowType = /* Type | IncompleteType */

        external interface IncompleteType   {
            var flags: TypeFlags
var type: Type
        }
    

        external interface AmdDependency   {
            var path: String
var name: String?
        }
    

        external interface SourceFile   : Declaration {
            val kind: SyntaxKind.SourceFile
val statements: NodeArray<Statement>
val endOfFileToken: Token<SyntaxKind.EndOfFileToken>
var fileName: String
var text: String
var amdDependencies: Array<out AmdDependency>
var moduleName: String?
var referencedFiles: Array<out FileReference>
var typeReferenceDirectives: Array<out FileReference>
var libReferenceDirectives: Array<out FileReference>
var languageVariant: LanguageVariant
var isDeclarationFile: Boolean
var hasNoDefaultLib: Boolean
var languageVersion: ScriptTarget
fun  getLineAndCharacterOfPosition(pos: Double): LineAndCharacter
fun  getLineEndOfPosition(pos: Double): Double
fun  getLineStarts(): Array<out Double>
fun  getPositionOfLineAndCharacter(line: Double, character: Double): Double
fun  update(newText: String, textChangeRange: TextChangeRange): SourceFile
        }
    

        external interface Bundle   : Node {
            val kind: SyntaxKind.Bundle
val prepends: Array<out (/* InputFiles | UnparsedSource */)>
val sourceFiles: Array<out SourceFile>
        }
    

        external interface InputFiles   : Node {
            val kind: SyntaxKind.InputFiles
var javascriptPath: String?
var javascriptText: String
var javascriptMapPath: String?
var javascriptMapText: String?
var declarationPath: String?
var declarationText: String
var declarationMapPath: String?
var declarationMapText: String?
        }
    

        external interface UnparsedSource   : Node {
            val kind: SyntaxKind.UnparsedSource
var fileName: String
var text: String
val prologues: Array<out UnparsedPrologue>
var helpers: Array<out UnscopedEmitHelper>?
var referencedFiles: Array<out FileReference>
var typeReferenceDirectives: Array<out String>?
var libReferenceDirectives: Array<out FileReference>
var hasNoDefaultLib: Boolean?
var sourceMapPath: String?
var sourceMapText: String?
val syntheticReferences: Array<out UnparsedSyntheticReference>?
val texts: Array<out UnparsedSourceText>
        }
    
typealias UnparsedSourceText = /* UnparsedPrepend | UnparsedTextLike */
typealias UnparsedNode = /* UnparsedPrologue | UnparsedSourceText | UnparsedSyntheticReference */

        external interface UnparsedSection   : Node {
            val kind: SyntaxKind
val parent: UnparsedSource
val data: String?
        }
    

        external interface UnparsedPrologue   : UnparsedSection {
            val kind: SyntaxKind.UnparsedPrologue
val parent: UnparsedSource
val data: String
        }
    

        external interface UnparsedPrepend   : UnparsedSection {
            val kind: SyntaxKind.UnparsedPrepend
val parent: UnparsedSource
val data: String
val texts: Array<out UnparsedTextLike>
        }
    

        external interface UnparsedTextLike   : UnparsedSection {
            val kind: /* SyntaxKind.UnparsedText | SyntaxKind.UnparsedInternalText */
val parent: UnparsedSource
        }
    

        external interface UnparsedSyntheticReference   : UnparsedSection {
            val kind: SyntaxKind.UnparsedSyntheticReference
val parent: UnparsedSource
        }
    

        external interface JsonSourceFile   : SourceFile {
            val statements: NodeArray<JsonObjectExpressionStatement>
        }
    

        external interface TsConfigSourceFile   : JsonSourceFile {
            var extendedSourceFiles: Array<String>?
        }
    

        external interface JsonMinusNumericLiteral   : PrefixUnaryExpression {
            val kind: SyntaxKind.PrefixUnaryExpression
val operator: SyntaxKind.MinusToken
val operand: NumericLiteral
        }
    
typealias JsonObjectExpression = /* ObjectLiteralExpression | ArrayLiteralExpression | JsonMinusNumericLiteral | NumericLiteral | StringLiteral | BooleanLiteral | NullLiteral */

        external interface JsonObjectExpressionStatement   : ExpressionStatement {
            val expression: JsonObjectExpression
        }
    

        external interface ScriptReferenceHost   {
            fun  getCompilerOptions(): CompilerOptions
fun  getSourceFile(fileName: String): SourceFile?
fun  getSourceFileByPath(path: Path): SourceFile?
fun  getCurrentDirectory(): String
        }
    

        external interface ParseConfigHost   {
            var useCaseSensitiveFileNames: Boolean
fun  readDirectory(rootDir: String, extensions: Array<out String>, excludes: Array<out String>?, includes: Array<out String>, depth: Double = definedExternally): Array<out String>
fun  fileExists(path: String): Boolean
fun  readFile(path: String): String?
fun  trace(s: String): Unit
        }
    
typealias ResolvedConfigFileName = /* string & {
        _isResolvedConfigFileName: never;
    } */
typealias WriteFileCallback = (fileName: String, data: String, writeByteOrderMark: Boolean, onError: (message: String) -> Unit = definedExternally, sourceFiles: Array<out SourceFile> = definedExternally) -> Unit
/* export class OperationCanceledException {
    } */

        external interface CancellationToken   {
            fun  isCancellationRequested(): Boolean
fun  throwIfCancellationRequested(): Unit
        }
    

        external interface Program   : ScriptReferenceHost {
            fun  getCurrentDirectory(): String
fun  getRootFileNames(): Array<out String>
fun  getSourceFiles(): Array<out SourceFile>
fun  emit(targetSourceFile: SourceFile = definedExternally, writeFile: WriteFileCallback = definedExternally, cancellationToken: CancellationToken = definedExternally, emitOnlyDtsFiles: Boolean = definedExternally, customTransformers: CustomTransformers = definedExternally): EmitResult
fun  getOptionsDiagnostics(cancellationToken: CancellationToken = definedExternally): Array<out Diagnostic>
fun  getGlobalDiagnostics(cancellationToken: CancellationToken = definedExternally): Array<out Diagnostic>
fun  getSyntacticDiagnostics(sourceFile: SourceFile = definedExternally, cancellationToken: CancellationToken = definedExternally): Array<out DiagnosticWithLocation>
fun  getSemanticDiagnostics(sourceFile: SourceFile = definedExternally, cancellationToken: CancellationToken = definedExternally): Array<out Diagnostic>
fun  getDeclarationDiagnostics(sourceFile: SourceFile = definedExternally, cancellationToken: CancellationToken = definedExternally): Array<out DiagnosticWithLocation>
fun  getConfigFileParsingDiagnostics(): Array<out Diagnostic>
fun  getTypeChecker(): TypeChecker
fun  getNodeCount(): Double
fun  getIdentifierCount(): Double
fun  getSymbolCount(): Double
fun  getTypeCount(): Double
fun  getInstantiationCount(): Double
fun  getRelationCacheSizes(): /* {
            assignable: number;
            identity: number;
            subtype: number;
            strictSubtype: number;
        } */
fun  isSourceFileFromExternalLibrary(file: SourceFile): Boolean
fun  isSourceFileDefaultLibrary(file: SourceFile): Boolean
fun  getProjectReferences(): Array<out ProjectReference>?
fun  getResolvedProjectReferences(): Array<out (ResolvedProjectReference?)>?
        }
    

        external interface ResolvedProjectReference   {
            var commandLine: ParsedCommandLine
var sourceFile: SourceFile
var references: Array<out (ResolvedProjectReference?)>?
        }
    
typealias CustomTransformerFactory = (context: TransformationContext) -> CustomTransformer

        external interface CustomTransformer   {
            fun  transformSourceFile(node: SourceFile): SourceFile
fun  transformBundle(node: Bundle): Bundle
        }
    

        external interface CustomTransformers   {
            var before: Array<(/* TransformerFactory<SourceFile> | CustomTransformerFactory */)>?
var after: Array<(/* TransformerFactory<SourceFile> | CustomTransformerFactory */)>?
var afterDeclarations: Array<(/* TransformerFactory<Bundle | SourceFile> | CustomTransformerFactory */)>?
        }
    

        external interface SourceMapSpan   {
            var emittedLine: Double
var emittedColumn: Double
var sourceLine: Double
var sourceColumn: Double
var nameIndex: Double?
var sourceIndex: Double
        }
    

        external enum class ExitStatus {
            Success,
DiagnosticsPresent_OutputsSkipped,
DiagnosticsPresent_OutputsGenerated,
InvalidProject_OutputsSkipped,
ProjectReferenceCycle_OutputsSkipped,
ProjectReferenceCycle_OutputsSkupped
        }
    

        external interface EmitResult   {
            var emitSkipped: Boolean
var diagnostics: Array<out Diagnostic>
var emittedFiles: Array<String>?
        }
    

        external interface TypeChecker   {
            fun  getTypeOfSymbolAtLocation(symbol: Symbol, node: Node): Type
fun  getDeclaredTypeOfSymbol(symbol: Symbol): Type
fun  getPropertiesOfType(type: Type): Array<Symbol>
fun  getPropertyOfType(type: Type, propertyName: String): Symbol?
fun  getPrivateIdentifierPropertyOfType(leftType: Type, name: String, location: Node): Symbol?
fun  getIndexInfoOfType(type: Type, kind: IndexKind): IndexInfo?
fun  getIndexInfosOfType(type: Type): Array<out IndexInfo>
fun  getSignaturesOfType(type: Type, kind: SignatureKind): Array<out Signature>
fun  getIndexTypeOfType(type: Type, kind: IndexKind): Type?
fun  getBaseTypes(type: InterfaceType): Array<BaseType>
fun  getBaseTypeOfLiteralType(type: Type): Type
fun  getWidenedType(type: Type): Type
fun  getReturnTypeOfSignature(signature: Signature): Type
fun  getNullableType(type: Type, flags: TypeFlags): Type
fun  getNonNullableType(type: Type): Type
fun  getTypeArguments(type: TypeReference): Array<out Type>
fun  typeToTypeNode(type: Type, enclosingDeclaration: Node?, flags: NodeBuilderFlags?): TypeNode?
fun  signatureToSignatureDeclaration(signature: Signature, kind: SyntaxKind, enclosingDeclaration: Node?, flags: NodeBuilderFlags?): /* SignatureDeclaration & {
            typeArguments?: NodeArray<TypeNode>;
        } */?
fun  indexInfoToIndexSignatureDeclaration(indexInfo: IndexInfo, enclosingDeclaration: Node?, flags: NodeBuilderFlags?): IndexSignatureDeclaration?
fun  symbolToEntityName(symbol: Symbol, meaning: SymbolFlags, enclosingDeclaration: Node?, flags: NodeBuilderFlags?): EntityName?
fun  symbolToExpression(symbol: Symbol, meaning: SymbolFlags, enclosingDeclaration: Node?, flags: NodeBuilderFlags?): Expression?
fun  symbolToTypeParameterDeclarations(symbol: Symbol, enclosingDeclaration: Node?, flags: NodeBuilderFlags?): NodeArray<TypeParameterDeclaration>?
fun  symbolToParameterDeclaration(symbol: Symbol, enclosingDeclaration: Node?, flags: NodeBuilderFlags?): ParameterDeclaration?
fun  typeParameterToDeclaration(parameter: TypeParameter, enclosingDeclaration: Node?, flags: NodeBuilderFlags?): TypeParameterDeclaration?
fun  getSymbolsInScope(location: Node, meaning: SymbolFlags): Array<Symbol>
fun  getSymbolAtLocation(node: Node): Symbol?
fun  getSymbolsOfParameterPropertyDeclaration(parameter: ParameterDeclaration, parameterName: String): Array<Symbol>
fun  getShorthandAssignmentValueSymbol(location: Node?): Symbol?
fun  getExportSpecifierLocalTargetSymbol(location: /* ExportSpecifier | Identifier */): Symbol?
fun  getExportSymbolOfSymbol(symbol: Symbol): Symbol
fun  getPropertySymbolOfDestructuringAssignment(location: Identifier): Symbol?
fun  getTypeOfAssignmentPattern(pattern: AssignmentPattern): Type
fun  getTypeAtLocation(node: Node): Type
fun  getTypeFromTypeNode(node: TypeNode): Type
fun  signatureToString(signature: Signature, enclosingDeclaration: Node = definedExternally, flags: TypeFormatFlags = definedExternally, kind: SignatureKind = definedExternally): String
fun  typeToString(type: Type, enclosingDeclaration: Node = definedExternally, flags: TypeFormatFlags = definedExternally): String
fun  symbolToString(symbol: Symbol, enclosingDeclaration: Node = definedExternally, meaning: SymbolFlags = definedExternally, flags: SymbolFormatFlags = definedExternally): String
fun  typePredicateToString(predicate: TypePredicate, enclosingDeclaration: Node = definedExternally, flags: TypeFormatFlags = definedExternally): String
fun  getFullyQualifiedName(symbol: Symbol): String
fun  getAugmentedPropertiesOfType(type: Type): Array<Symbol>
fun  getRootSymbols(symbol: Symbol): Array<out Symbol>
fun  getSymbolOfExpando(node: Node, allowDeclaration: Boolean): Symbol?
fun  getContextualType(node: Expression): Type?
fun  getResolvedSignature(node: CallLikeExpression, candidatesOutArray: Array<Signature> = definedExternally, argumentCount: Double = definedExternally): Signature?
fun  getSignatureFromDeclaration(declaration: SignatureDeclaration): Signature?
fun  isImplementationOfOverload(node: SignatureDeclaration): Boolean?
fun  isUndefinedSymbol(symbol: Symbol): Boolean
fun  isArgumentsSymbol(symbol: Symbol): Boolean
fun  isUnknownSymbol(symbol: Symbol): Boolean
fun  getConstantValue(node: /* EnumMember | PropertyAccessExpression | ElementAccessExpression */): /* string | number | undefined */
fun  isValidPropertyAccess(node: /* PropertyAccessExpression | QualifiedName | ImportTypeNode */, propertyName: String): Boolean
fun  getAliasedSymbol(symbol: Symbol): Symbol
fun  getImmediateAliasedSymbol(symbol: Symbol): Symbol?
fun  getExportsOfModule(moduleSymbol: Symbol): Array<Symbol>
fun  getJsxIntrinsicTagNamesAt(location: Node): Array<Symbol>
fun  isOptionalParameter(node: ParameterDeclaration): Boolean
fun  getAmbientModules(): Array<Symbol>
fun  tryGetMemberInModuleExports(memberName: String, moduleSymbol: Symbol): Symbol?
fun  getApparentType(type: Type): Type
fun  getBaseConstraintOfType(type: Type): Type?
fun  getDefaultFromTypeParameter(type: Type): Type?
fun <T  > runWithCancellationToken(token: CancellationToken, cb: (checker: TypeChecker) -> T): T
        }
    

        external enum class NodeBuilderFlags {
            None,
NoTruncation,
WriteArrayAsGenericType,
GenerateNamesForShadowedTypeParams,
UseStructuralFallback,
ForbidIndexedAccessSymbolReferences,
WriteTypeArgumentsOfSignature,
UseFullyQualifiedType,
UseOnlyExternalAliasing,
SuppressAnyReturnType,
WriteTypeParametersInQualifiedName,
MultilineObjectLiterals,
WriteClassExpressionAsTypeLiteral,
UseTypeOfFunction,
OmitParameterModifiers,
UseAliasDefinedOutsideCurrentScope,
UseSingleQuotesForStringLiteralType,
NoTypeReduction,
NoUndefinedOptionalParameterType,
AllowThisInObjectLiteral,
AllowQualifiedNameInPlaceOfIdentifier,
AllowQualifedNameInPlaceOfIdentifier,
AllowAnonymousIdentifier,
AllowEmptyUnionOrIntersection,
AllowEmptyTuple,
AllowUniqueESSymbolType,
AllowEmptyIndexInfoType,
AllowNodeModulesRelativePaths,
IgnoreErrors,
InObjectTypeLiteral,
InTypeAlias,
InInitialEntityName
        }
    

        external enum class TypeFormatFlags {
            None,
NoTruncation,
WriteArrayAsGenericType,
UseStructuralFallback,
WriteTypeArgumentsOfSignature,
UseFullyQualifiedType,
SuppressAnyReturnType,
MultilineObjectLiterals,
WriteClassExpressionAsTypeLiteral,
UseTypeOfFunction,
OmitParameterModifiers,
UseAliasDefinedOutsideCurrentScope,
UseSingleQuotesForStringLiteralType,
NoTypeReduction,
AllowUniqueESSymbolType,
AddUndefined,
WriteArrowStyleSignature,
InArrayType,
InElementType,
InFirstTypeArgument,
InTypeAlias,
WriteOwnNameForAnyLike,
NodeBuilderFlagsMask
        }
    

        external enum class SymbolFormatFlags {
            None,
WriteTypeParametersOrArguments,
UseOnlyExternalAliasing,
AllowAnyNodeKind,
UseAliasDefinedOutsideCurrentScope
        }
    

        external enum class TypePredicateKind {
            This,
Identifier,
AssertsThis,
AssertsIdentifier
        }
    

        external interface TypePredicateBase   {
            var kind: TypePredicateKind
var type: Type?
        }
    

        external interface ThisTypePredicate   : TypePredicateBase {
            var kind: TypePredicateKind.This
var parameterName: null
var parameterIndex: null
var type: Type
        }
    

        external interface IdentifierTypePredicate   : TypePredicateBase {
            var kind: TypePredicateKind.Identifier
var parameterName: String
var parameterIndex: Double
var type: Type
        }
    

        external interface AssertsThisTypePredicate   : TypePredicateBase {
            var kind: TypePredicateKind.AssertsThis
var parameterName: null
var parameterIndex: null
var type: Type?
        }
    

        external interface AssertsIdentifierTypePredicate   : TypePredicateBase {
            var kind: TypePredicateKind.AssertsIdentifier
var parameterName: String
var parameterIndex: Double
var type: Type?
        }
    
typealias TypePredicate = /* ThisTypePredicate | IdentifierTypePredicate | AssertsThisTypePredicate | AssertsIdentifierTypePredicate */

        external enum class SymbolFlags {
            None,
FunctionScopedVariable,
BlockScopedVariable,
Property,
EnumMember,
Function,
Class,
Interface,
ConstEnum,
RegularEnum,
ValueModule,
NamespaceModule,
TypeLiteral,
ObjectLiteral,
Method,
Constructor,
GetAccessor,
SetAccessor,
Signature,
TypeParameter,
TypeAlias,
ExportValue,
Alias,
Prototype,
ExportStar,
Optional,
Transient,
Assignment,
ModuleExports,
Enum,
Variable,
Value,
Type,
Namespace,
Module,
Accessor,
FunctionScopedVariableExcludes,
BlockScopedVariableExcludes,
ParameterExcludes,
PropertyExcludes,
EnumMemberExcludes,
FunctionExcludes,
ClassExcludes,
InterfaceExcludes,
RegularEnumExcludes,
ConstEnumExcludes,
ValueModuleExcludes,
NamespaceModuleExcludes,
MethodExcludes,
GetAccessorExcludes,
SetAccessorExcludes,
TypeParameterExcludes,
TypeAliasExcludes,
AliasExcludes,
ModuleMember,
ExportHasLocal,
BlockScoped,
PropertyOrAccessor,
ClassMember
        }
    

        external interface Symbol   {
            var flags: SymbolFlags
var escapedName: __String
var declarations: Array<Declaration>?
var valueDeclaration: Declaration?
var members: SymbolTable?
var exports: SymbolTable?
var globalExports: SymbolTable?
val name: String
fun  getFlags(): SymbolFlags
fun  getEscapedName(): __String
fun  getName(): String
fun  getDeclarations(): Array<Declaration>?
fun  getDocumentationComment(typeChecker: TypeChecker?): Array<SymbolDisplayPart>
fun  getJsDocTags(checker: TypeChecker = definedExternally): Array<JSDocTagInfo>
        }
    

        external enum class InternalSymbolName {
            Call,
Constructor,
New,
Index,
ExportStar,
Global,
Missing,
Type,
Object,
JSXAttributes,
Class,
Function,
Computed,
Resolving,
ExportEquals,
Default,
This
        }
    
typealias __String = /* (string & {
        __escapedIdentifier: void;
    }) | (void & {
        __escapedIdentifier: void;
    }) | InternalSymbolName */

        external interface ReadonlyUnderscoreEscapedMap <T  >  : ReadonlyESMap<__String, T> {
            
        }
    

        external interface UnderscoreEscapedMap <T  >  : ESMap<__String, T>, ReadonlyUnderscoreEscapedMap<T> {
            
        }
    
typealias SymbolTable = UnderscoreEscapedMap<Symbol>

        external enum class TypeFlags {
            Any,
Unknown,
String,
Number,
Boolean,
Enum,
BigInt,
StringLiteral,
NumberLiteral,
BooleanLiteral,
EnumLiteral,
BigIntLiteral,
ESSymbol,
UniqueESSymbol,
Void,
Undefined,
Null,
Never,
TypeParameter,
Object,
Union,
Intersection,
Index,
IndexedAccess,
Conditional,
Substitution,
NonPrimitive,
TemplateLiteral,
StringMapping,
Literal,
Unit,
StringOrNumberLiteral,
PossiblyFalsy,
StringLike,
NumberLike,
BigIntLike,
BooleanLike,
EnumLike,
ESSymbolLike,
VoidLike,
UnionOrIntersection,
StructuredType,
TypeVariable,
InstantiableNonPrimitive,
InstantiablePrimitive,
Instantiable,
StructuredOrInstantiable,
Narrowable
        }
    
typealias DestructuringPattern = /* BindingPattern | ObjectLiteralExpression | ArrayLiteralExpression */

        external interface Type   {
            var flags: TypeFlags
var symbol: Symbol
var pattern: DestructuringPattern?
var aliasSymbol: Symbol?
var aliasTypeArguments: Array<out Type>?
fun  getFlags(): TypeFlags
fun  getSymbol(): Symbol?
fun  getProperties(): Array<Symbol>
fun  getProperty(propertyName: String): Symbol?
fun  getApparentProperties(): Array<Symbol>
fun  getCallSignatures(): Array<out Signature>
fun  getConstructSignatures(): Array<out Signature>
fun  getStringIndexType(): Type?
fun  getNumberIndexType(): Type?
fun  getBaseTypes(): Array<BaseType>?
fun  getNonNullableType(): Type
fun  getConstraint(): Type?
fun  getDefault(): Type?
fun  isUnion(): Boolean
fun  isIntersection(): Boolean
fun  isUnionOrIntersection(): Boolean
fun  isLiteral(): Boolean
fun  isStringLiteral(): Boolean
fun  isNumberLiteral(): Boolean
fun  isTypeParameter(): Boolean
fun  isClassOrInterface(): Boolean
fun  isClass(): Boolean
        }
    

        external interface LiteralType   : Type {
            var value: /* string | number | PseudoBigInt */
var freshType: LiteralType
var regularType: LiteralType
        }
    

        external interface UniqueESSymbolType   : Type {
            var symbol: Symbol
var escapedName: __String
        }
    

        external interface StringLiteralType   : LiteralType {
            var value: String
        }
    

        external interface NumberLiteralType   : LiteralType {
            var value: Double
        }
    

        external interface BigIntLiteralType   : LiteralType {
            var value: PseudoBigInt
        }
    

        external interface EnumType   : Type {
            
        }
    

        external enum class ObjectFlags {
            Class,
Interface,
Reference,
Tuple,
Anonymous,
Mapped,
Instantiated,
ObjectLiteral,
EvolvingArray,
ObjectLiteralPatternWithComputedProperties,
ReverseMapped,
JsxAttributes,
MarkerType,
JSLiteral,
FreshLiteral,
ArrayLiteral,
ClassOrInterface,
ContainsSpread,
ObjectRestType
        }
    

        external interface ObjectType   : Type {
            var objectFlags: ObjectFlags
        }
    

        external interface InterfaceType   : ObjectType {
            var typeParameters: Array<TypeParameter>?
var outerTypeParameters: Array<TypeParameter>?
var localTypeParameters: Array<TypeParameter>?
var thisType: TypeParameter?
        }
    
typealias BaseType = /* ObjectType | IntersectionType | TypeVariable */

        external interface InterfaceTypeWithDeclaredMembers   : InterfaceType {
            var declaredProperties: Array<Symbol>
var declaredCallSignatures: Array<Signature>
var declaredConstructSignatures: Array<Signature>
var declaredIndexInfos: Array<IndexInfo>
        }
    

        external interface TypeReference   : ObjectType {
            var target: GenericType
var node: /* TypeReferenceNode | ArrayTypeNode | TupleTypeNode */?
var typeArguments: Array<out Type>?
        }
    

        external interface DeferredTypeReference   : TypeReference {
            
        }
    

        external interface GenericType   : InterfaceType, TypeReference {
            
        }
    

        external enum class ElementFlags {
            Required,
Optional,
Rest,
Variadic,
Fixed,
Variable,
NonRequired,
NonRest
        }
    

        external interface TupleType   : GenericType {
            var elementFlags: Array<out ElementFlags>
var minLength: Double
var fixedLength: Double
var hasRestElement: Boolean
var combinedFlags: ElementFlags
var readonly: Boolean
var labeledElementDeclarations: Array<out (/* NamedTupleMember | ParameterDeclaration */)>?
        }
    

        external interface TupleTypeReference   : TypeReference {
            var target: TupleType
        }
    

        external interface UnionOrIntersectionType   : Type {
            var types: Array<Type>
        }
    

        external interface UnionType   : UnionOrIntersectionType {
            
        }
    

        external interface IntersectionType   : UnionOrIntersectionType {
            
        }
    
typealias StructuredType = /* ObjectType | UnionType | IntersectionType */

        external interface EvolvingArrayType   : ObjectType {
            var elementType: Type
var finalArrayType: Type?
        }
    

        external interface InstantiableType   : Type {
            
        }
    

        external interface TypeParameter   : InstantiableType {
            
        }
    

        external interface IndexedAccessType   : InstantiableType {
            var objectType: Type
var indexType: Type
var constraint: Type?
var simplifiedForReading: Type?
var simplifiedForWriting: Type?
        }
    
typealias TypeVariable = /* TypeParameter | IndexedAccessType */

        external interface IndexType   : InstantiableType {
            var type: /* InstantiableType | UnionOrIntersectionType */
        }
    

        external interface ConditionalRoot   {
            var node: ConditionalTypeNode
var checkType: Type
var extendsType: Type
var isDistributive: Boolean
var inferTypeParameters: Array<TypeParameter>?
var outerTypeParameters: Array<TypeParameter>?
var instantiations: Map<Type>?
var aliasSymbol: Symbol?
var aliasTypeArguments: Array<Type>?
        }
    

        external interface ConditionalType   : InstantiableType {
            var root: ConditionalRoot
var checkType: Type
var extendsType: Type
var resolvedTrueType: Type
var resolvedFalseType: Type
        }
    

        external interface TemplateLiteralType   : InstantiableType {
            var texts: Array<out String>
var types: Array<out Type>
        }
    

        external interface StringMappingType   : InstantiableType {
            var symbol: Symbol
var type: Type
        }
    

        external interface SubstitutionType   : InstantiableType {
            var objectFlags: ObjectFlags
var baseType: Type
var substitute: Type
        }
    

        external enum class SignatureKind {
            Call,
Construct
        }
    

        external interface Signature   {
            var declaration: /* SignatureDeclaration | JSDocSignature */?
var typeParameters: Array<out TypeParameter>?
var parameters: Array<out Symbol>
fun  getDeclaration(): SignatureDeclaration
fun  getTypeParameters(): Array<TypeParameter>?
fun  getParameters(): Array<Symbol>
fun  getReturnType(): Type
fun  getDocumentationComment(typeChecker: TypeChecker?): Array<SymbolDisplayPart>
fun  getJsDocTags(): Array<JSDocTagInfo>
        }
    

        external enum class IndexKind {
            String,
Number
        }
    

        external interface IndexInfo   {
            var keyType: Type
var type: Type
var isReadonly: Boolean
var declaration: IndexSignatureDeclaration?
        }
    

        external enum class InferencePriority {
            NakedTypeVariable,
SpeculativeTuple,
SubstituteSource,
HomomorphicMappedType,
PartialHomomorphicMappedType,
MappedTypeConstraint,
ContravariantConditional,
ReturnType,
LiteralKeyof,
NoConstraints,
AlwaysStrict,
MaxValue,
PriorityImpliesCombination,
Circularity
        }
    
typealias JsFileExtensionInfo = FileExtensionInfo

        external interface FileExtensionInfo   {
            var extension: String
var isMixedContent: Boolean
var scriptKind: ScriptKind?
        }
    

        external interface DiagnosticMessage   {
            var key: String
var category: DiagnosticCategory
var code: Double
var message: String
var reportsUnnecessary: /* {} */?
var reportsDeprecated: /* {} */?
        }
    

        external interface DiagnosticMessageChain   {
            var messageText: String
var category: DiagnosticCategory
var code: Double
var next: Array<DiagnosticMessageChain>?
        }
    

        external interface Diagnostic   : DiagnosticRelatedInformation {
            var reportsUnnecessary: /* {} */?
var reportsDeprecated: /* {} */?
var source: String?
var relatedInformation: Array<DiagnosticRelatedInformation>?
        }
    

        external interface DiagnosticRelatedInformation   {
            var category: DiagnosticCategory
var code: Double
var file: SourceFile?
var start: Double?
var length: Double?
var messageText: /* string | DiagnosticMessageChain */
        }
    

        external interface DiagnosticWithLocation   : Diagnostic {
            var file: SourceFile
var start: Double
var length: Double
        }
    

        external enum class DiagnosticCategory {
            Warning,
Error,
Suggestion,
Message
        }
    

        external enum class ModuleResolutionKind {
            Classic,
NodeJs
        }
    

        external interface PluginImport   {
            var name: String
        }
    

        external interface ProjectReference   {
            var path: String
var originalPath: String?
var prepend: Boolean?
var circular: Boolean?
        }
    

        external enum class WatchFileKind {
            FixedPollingInterval,
PriorityPollingInterval,
DynamicPriorityPolling,
FixedChunkSizePolling,
UseFsEvents,
UseFsEventsOnParentDirectory
        }
    

        external enum class WatchDirectoryKind {
            UseFsEvents,
FixedPollingInterval,
DynamicPriorityPolling,
FixedChunkSizePolling
        }
    

        external enum class PollingWatchKind {
            FixedInterval,
PriorityInterval,
DynamicPriority,
FixedChunkSize
        }
    
typealias CompilerOptionsValue = /* string | number | boolean | (string | number)[] | string[] | MapLike<string[]> | PluginImport[] | ProjectReference[] | null | undefined */

        external interface CompilerOptions   {
            var allowJs: Boolean?
var allowSyntheticDefaultImports: Boolean?
var allowUmdGlobalAccess: Boolean?
var allowUnreachableCode: Boolean?
var allowUnusedLabels: Boolean?
var alwaysStrict: Boolean?
var baseUrl: String?
var charset: String?
var checkJs: Boolean?
var declaration: Boolean?
var declarationMap: Boolean?
var emitDeclarationOnly: Boolean?
var declarationDir: String?
var disableSizeLimit: Boolean?
var disableSourceOfProjectReferenceRedirect: Boolean?
var disableSolutionSearching: Boolean?
var disableReferencedProjectLoad: Boolean?
var downlevelIteration: Boolean?
var emitBOM: Boolean?
var emitDecoratorMetadata: Boolean?
var exactOptionalPropertyTypes: Boolean?
var experimentalDecorators: Boolean?
var forceConsistentCasingInFileNames: Boolean?
var importHelpers: Boolean?
var importsNotUsedAsValues: ImportsNotUsedAsValues?
var inlineSourceMap: Boolean?
var inlineSources: Boolean?
var isolatedModules: Boolean?
var jsx: JsxEmit?
var keyofStringsOnly: Boolean?
var lib: Array<String>?
var locale: String?
var mapRoot: String?
var maxNodeModuleJsDepth: Double?
var module: ModuleKind?
var moduleResolution: ModuleResolutionKind?
var newLine: NewLineKind?
var noEmit: Boolean?
var noEmitHelpers: Boolean?
var noEmitOnError: Boolean?
var noErrorTruncation: Boolean?
var noFallthroughCasesInSwitch: Boolean?
var noImplicitAny: Boolean?
var noImplicitReturns: Boolean?
var noImplicitThis: Boolean?
var noStrictGenericChecks: Boolean?
var noUnusedLocals: Boolean?
var noUnusedParameters: Boolean?
var noImplicitUseStrict: Boolean?
var noPropertyAccessFromIndexSignature: Boolean?
var assumeChangesOnlyAffectDirectDependencies: Boolean?
var noLib: Boolean?
var noResolve: Boolean?
var noUncheckedIndexedAccess: Boolean?
var out: String?
var outDir: String?
var outFile: String?
var paths: MapLike<Array<String>>?
var preserveConstEnums: Boolean?
var noImplicitOverride: Boolean?
var preserveSymlinks: Boolean?
var project: String?
var reactNamespace: String?
var jsxFactory: String?
var jsxFragmentFactory: String?
var jsxImportSource: String?
var composite: Boolean?
var incremental: Boolean?
var tsBuildInfoFile: String?
var removeComments: Boolean?
var rootDir: String?
var rootDirs: Array<String>?
var skipLibCheck: Boolean?
var skipDefaultLibCheck: Boolean?
var sourceMap: Boolean?
var sourceRoot: String?
var strict: Boolean?
var strictFunctionTypes: Boolean?
var strictBindCallApply: Boolean?
var strictNullChecks: Boolean?
var strictPropertyInitialization: Boolean?
var stripInternal: Boolean?
var suppressExcessPropertyErrors: Boolean?
var suppressImplicitAnyIndexErrors: Boolean?
var target: ScriptTarget?
var traceResolution: Boolean?
var useUnknownInCatchVariables: Boolean?
var resolveJsonModule: Boolean?
var types: Array<String>?
var typeRoots: Array<String>?
var esModuleInterop: Boolean?
var useDefineForClassFields: Boolean?
/* [option: string]: CompilerOptionsValue | TsConfigSourceFile | undefined; */
        }
    

        external interface WatchOptions   {
            var watchFile: WatchFileKind?
var watchDirectory: WatchDirectoryKind?
var fallbackPolling: PollingWatchKind?
var synchronousWatchDirectory: Boolean?
var excludeDirectories: Array<String>?
var excludeFiles: Array<String>?
/* [option: string]: CompilerOptionsValue | undefined; */
        }
    

        external interface TypeAcquisition   {
            var enableAutoDiscovery: Boolean?
var enable: Boolean?
var include: Array<String>?
var exclude: Array<String>?
var disableFilenameBasedTypeAcquisition: Boolean?
/* [option: string]: CompilerOptionsValue | undefined; */
        }
    

        external enum class ModuleKind {
            None,
CommonJS,
AMD,
UMD,
System,
ES2015,
ES2020,
ESNext
        }
    

        external enum class JsxEmit {
            None,
Preserve,
React,
ReactNative,
ReactJSX,
ReactJSXDev
        }
    

        external enum class ImportsNotUsedAsValues {
            Remove,
Preserve,
Error
        }
    

        external enum class NewLineKind {
            CarriageReturnLineFeed,
LineFeed
        }
    

        external interface LineAndCharacter   {
            var line: Double
var character: Double
        }
    

        external enum class ScriptKind {
            Unknown,
JS,
JSX,
TS,
TSX,
External,
JSON,
Deferred
        }
    

        external enum class ScriptTarget {
            ES3,
ES5,
ES2015,
ES2016,
ES2017,
ES2018,
ES2019,
ES2020,
ES2021,
ESNext,
JSON,
Latest
        }
    

        external enum class LanguageVariant {
            Standard,
JSX
        }
    

        external interface ParsedCommandLine   {
            var options: CompilerOptions
var typeAcquisition: TypeAcquisition?
var fileNames: Array<String>
var projectReferences: Array<out ProjectReference>?
var watchOptions: WatchOptions?
var raw: Any??
var errors: Array<Diagnostic>
var wildcardDirectories: MapLike<WatchDirectoryFlags>?
var compileOnSave: Boolean?
        }
    

        external enum class WatchDirectoryFlags {
            None,
Recursive
        }
    

        external interface CreateProgramOptions   {
            var rootNames: Array<out String>
var options: CompilerOptions
var projectReferences: Array<out ProjectReference>?
var host: CompilerHost?
var oldProgram: Program?
var configFileParsingDiagnostics: Array<out Diagnostic>?
        }
    

        external interface ModuleResolutionHost   {
            fun  fileExists(fileName: String): Boolean
fun  readFile(fileName: String): String?
fun  trace(s: String): Unit
fun  directoryExists(directoryName: String): Boolean
fun  realpath(path: String): String
fun  getCurrentDirectory(): String
fun  getDirectories(path: String): Array<String>
        }
    

        external interface ResolvedModule   {
            var resolvedFileName: String
var isExternalLibraryImport: Boolean?
        }
    

        external interface ResolvedModuleFull   : ResolvedModule {
            var extension: Extension
var packageId: PackageId?
        }
    

        external interface PackageId   {
            var name: String
var subModuleName: String
var version: String
        }
    

        external enum class Extension {
            Ts,
Tsx,
Dts,
Js,
Jsx,
Json,
TsBuildInfo
        }
    

        external interface ResolvedModuleWithFailedLookupLocations   {
            val resolvedModule: ResolvedModuleFull?
        }
    

        external interface ResolvedTypeReferenceDirective   {
            var primary: Boolean
var resolvedFileName: String?
var packageId: PackageId?
var isExternalLibraryImport: Boolean?
        }
    

        external interface ResolvedTypeReferenceDirectiveWithFailedLookupLocations   {
            val resolvedTypeReferenceDirective: ResolvedTypeReferenceDirective?
val failedLookupLocations: Array<String>
        }
    

        external interface CompilerHost   : ModuleResolutionHost {
            fun  getSourceFile(fileName: String, languageVersion: ScriptTarget, onError: (message: String) -> Unit = definedExternally, shouldCreateNewSourceFile: Boolean = definedExternally): SourceFile?
fun  getSourceFileByPath(fileName: String, path: Path, languageVersion: ScriptTarget, onError: (message: String) -> Unit = definedExternally, shouldCreateNewSourceFile: Boolean = definedExternally): SourceFile?
fun  getCancellationToken(): CancellationToken
fun  getDefaultLibFileName(options: CompilerOptions): String
fun  getDefaultLibLocation(): String
var writeFile: WriteFileCallback
fun  getCurrentDirectory(): String
fun  getCanonicalFileName(fileName: String): String
fun  useCaseSensitiveFileNames(): Boolean
fun  getNewLine(): String
fun  readDirectory(rootDir: String, extensions: Array<out String>, excludes: Array<out String>?, includes: Array<out String>, depth: Double = definedExternally): Array<String>
fun  resolveModuleNames(moduleNames: Array<String>, containingFile: String, reusedNames: Array<String>?, redirectedReference: ResolvedProjectReference?, options: CompilerOptions): Array<(ResolvedModule?)>
fun  resolveTypeReferenceDirectives(typeReferenceDirectiveNames: Array<String>, containingFile: String, redirectedReference: ResolvedProjectReference?, options: CompilerOptions): Array<(ResolvedTypeReferenceDirective?)>
fun  getEnvironmentVariable(name: String): String?
fun  createHash(data: String): String
fun  getParsedCommandLine(fileName: String): ParsedCommandLine?
        }
    

        external interface SourceMapRange   : TextRange {
            var source: SourceMapSource?
        }
    

        external interface SourceMapSource   {
            var fileName: String
var text: String
var skipTrivia: (pos: Double) -> Double?
fun  getLineAndCharacterOfPosition(pos: Double): LineAndCharacter
        }
    

        external enum class EmitFlags {
            None,
SingleLine,
AdviseOnEmitNode,
NoSubstitution,
CapturesThis,
NoLeadingSourceMap,
NoTrailingSourceMap,
NoSourceMap,
NoNestedSourceMaps,
NoTokenLeadingSourceMaps,
NoTokenTrailingSourceMaps,
NoTokenSourceMaps,
NoLeadingComments,
NoTrailingComments,
NoComments,
NoNestedComments,
HelperName,
ExportName,
LocalName,
InternalName,
Indented,
NoIndentation,
AsyncFunctionBody,
ReuseTempVariableScope,
CustomPrologue,
NoHoisting,
HasEndOfDeclarationMarker,
Iterator,
NoAsciiEscaping
        }
    

        external interface EmitHelperBase   {
            val name: String
val scoped: Boolean
val text: /* string | ((node: EmitHelperUniqueNameCallback) => string) */
val priority: Double?
val dependencies: Array<EmitHelper>?
        }
    

        external interface ScopedEmitHelper   : EmitHelperBase {
            val scoped: Boolean
        }
    

        external interface UnscopedEmitHelper   : EmitHelperBase {
            val scoped: Boolean
val text: String
        }
    
typealias EmitHelper = /* ScopedEmitHelper | UnscopedEmitHelper */
typealias EmitHelperUniqueNameCallback = (name: String) -> String

        external enum class EmitHint {
            SourceFile,
Expression,
IdentifierName,
MappedTypeParameter,
Unspecified,
EmbeddedStatement,
JsxAttributeValue
        }
    

        external enum class OuterExpressionKinds {
            Parentheses,
TypeAssertions,
NonNullAssertions,
PartiallyEmittedExpressions,
Assertions,
All
        }
    
typealias TypeOfTag = /* "undefined" | "number" | "bigint" | "boolean" | "string" | "symbol" | "object" | "function" */

        external interface NodeFactory   {
            fun <T : Node > createNodeArray(elements: Array<out T> = definedExternally, hasTrailingComma: Boolean = definedExternally): NodeArray<T>
fun  createNumericLiteral(value: /* string | number */, numericLiteralFlags: TokenFlags = definedExternally): NumericLiteral
fun  createBigIntLiteral(value: /* string | PseudoBigInt */): BigIntLiteral
fun  createStringLiteral(text: String, isSingleQuote: Boolean = definedExternally): StringLiteral
fun  createStringLiteralFromNode(sourceNode: PropertyNameLiteral, isSingleQuote: Boolean = definedExternally): StringLiteral
fun  createRegularExpressionLiteral(text: String): RegularExpressionLiteral
fun  createIdentifier(text: String): Identifier
fun  createTempVariable(recordTempVariable: ((node: Identifier) -> Unit)?, reservedInNestedScopes: Boolean = definedExternally): Identifier
fun  createLoopVariable(reservedInNestedScopes: Boolean = definedExternally): Identifier
fun  createUniqueName(text: String, flags: GeneratedIdentifierFlags = definedExternally): Identifier
fun  getGeneratedNameForNode(node: Node?, flags: GeneratedIdentifierFlags = definedExternally): Identifier
fun  createPrivateIdentifier(text: String): PrivateIdentifier
fun  createToken(token: SyntaxKind.SuperKeyword): SuperExpression
fun  createToken(token: SyntaxKind.ThisKeyword): ThisExpression
fun  createToken(token: SyntaxKind.NullKeyword): NullLiteral
fun  createToken(token: SyntaxKind.TrueKeyword): TrueLiteral
fun  createToken(token: SyntaxKind.FalseKeyword): FalseLiteral
fun <TKind : PunctuationSyntaxKind > createToken(token: TKind): PunctuationToken<TKind>
fun <TKind : KeywordTypeSyntaxKind > createToken(token: TKind): KeywordTypeNode<TKind>
fun <TKind : ModifierSyntaxKind > createToken(token: TKind): ModifierToken<TKind>
fun <TKind : KeywordSyntaxKind > createToken(token: TKind): KeywordToken<TKind>
fun <TKind : /* SyntaxKind.Unknown | SyntaxKind.EndOfFileToken */ > createToken(token: TKind): Token<TKind>
fun  createSuper(): SuperExpression
fun  createThis(): ThisExpression
fun  createNull(): NullLiteral
fun  createTrue(): TrueLiteral
fun  createFalse(): FalseLiteral
fun <T : ModifierSyntaxKind > createModifier(kind: T): ModifierToken<T>
fun  createModifiersFromModifierFlags(flags: ModifierFlags): Array<Modifier>
fun  createQualifiedName(left: EntityName, right: /* string | Identifier */): QualifiedName
fun  updateQualifiedName(node: QualifiedName, left: EntityName, right: Identifier): QualifiedName
fun  createComputedPropertyName(expression: Expression): ComputedPropertyName
fun  updateComputedPropertyName(node: ComputedPropertyName, expression: Expression): ComputedPropertyName
fun  createTypeParameterDeclaration(name: /* string | Identifier */, constraint: TypeNode = definedExternally, defaultType: TypeNode = definedExternally): TypeParameterDeclaration
fun  updateTypeParameterDeclaration(node: TypeParameterDeclaration, name: Identifier, constraint: TypeNode?, defaultType: TypeNode?): TypeParameterDeclaration
fun  createParameterDeclaration(decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, dotDotDotToken: DotDotDotToken?, name: /* string | BindingName */, questionToken: QuestionToken = definedExternally, type: TypeNode = definedExternally, initializer: Expression = definedExternally): ParameterDeclaration
fun  updateParameterDeclaration(node: ParameterDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, dotDotDotToken: DotDotDotToken?, name: /* string | BindingName */, questionToken: QuestionToken?, type: TypeNode?, initializer: Expression?): ParameterDeclaration
fun  createDecorator(expression: Expression): Decorator
fun  updateDecorator(node: Decorator, expression: Expression): Decorator
fun  createPropertySignature(modifiers: Array<out Modifier>?, name: /* PropertyName | string */, questionToken: QuestionToken?, type: TypeNode?): PropertySignature
fun  updatePropertySignature(node: PropertySignature, modifiers: Array<out Modifier>?, name: PropertyName, questionToken: QuestionToken?, type: TypeNode?): PropertySignature
fun  createPropertyDeclaration(decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: /* string | PropertyName */, questionOrExclamationToken: /* QuestionToken | ExclamationToken | undefined */, type: TypeNode?, initializer: Expression?): PropertyDeclaration
fun  updatePropertyDeclaration(node: PropertyDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: /* string | PropertyName */, questionOrExclamationToken: /* QuestionToken | ExclamationToken | undefined */, type: TypeNode?, initializer: Expression?): PropertyDeclaration
fun  createMethodSignature(modifiers: Array<out Modifier>?, name: /* string | PropertyName */, questionToken: QuestionToken?, typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode?): MethodSignature
fun  updateMethodSignature(node: MethodSignature, modifiers: Array<out Modifier>?, name: PropertyName, questionToken: QuestionToken?, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode?): MethodSignature
fun  createMethodDeclaration(decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, asteriskToken: AsteriskToken?, name: /* string | PropertyName */, questionToken: QuestionToken?, typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode?, body: Block?): MethodDeclaration
fun  updateMethodDeclaration(node: MethodDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, asteriskToken: AsteriskToken?, name: PropertyName, questionToken: QuestionToken?, typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode?, body: Block?): MethodDeclaration
fun  createConstructorDeclaration(decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, parameters: Array<out ParameterDeclaration>, body: Block?): ConstructorDeclaration
fun  updateConstructorDeclaration(node: ConstructorDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, parameters: Array<out ParameterDeclaration>, body: Block?): ConstructorDeclaration
fun  createGetAccessorDeclaration(decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: /* string | PropertyName */, parameters: Array<out ParameterDeclaration>, type: TypeNode?, body: Block?): GetAccessorDeclaration
fun  updateGetAccessorDeclaration(node: GetAccessorDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: PropertyName, parameters: Array<out ParameterDeclaration>, type: TypeNode?, body: Block?): GetAccessorDeclaration
fun  createSetAccessorDeclaration(decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: /* string | PropertyName */, parameters: Array<out ParameterDeclaration>, body: Block?): SetAccessorDeclaration
fun  updateSetAccessorDeclaration(node: SetAccessorDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: PropertyName, parameters: Array<out ParameterDeclaration>, body: Block?): SetAccessorDeclaration
fun  createCallSignature(typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode?): CallSignatureDeclaration
fun  updateCallSignature(node: CallSignatureDeclaration, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode?): CallSignatureDeclaration
fun  createConstructSignature(typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode?): ConstructSignatureDeclaration
fun  updateConstructSignature(node: ConstructSignatureDeclaration, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode?): ConstructSignatureDeclaration
fun  createIndexSignature(decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, parameters: Array<out ParameterDeclaration>, type: TypeNode): IndexSignatureDeclaration
fun  updateIndexSignature(node: IndexSignatureDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, parameters: Array<out ParameterDeclaration>, type: TypeNode): IndexSignatureDeclaration
fun  createTemplateLiteralTypeSpan(type: TypeNode, literal: /* TemplateMiddle | TemplateTail */): TemplateLiteralTypeSpan
fun  updateTemplateLiteralTypeSpan(node: TemplateLiteralTypeSpan, type: TypeNode, literal: /* TemplateMiddle | TemplateTail */): TemplateLiteralTypeSpan
fun  createClassStaticBlockDeclaration(decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, body: Block): ClassStaticBlockDeclaration
fun  updateClassStaticBlockDeclaration(node: ClassStaticBlockDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, body: Block): ClassStaticBlockDeclaration
fun <TKind : KeywordTypeSyntaxKind > createKeywordTypeNode(kind: TKind): KeywordTypeNode<TKind>
fun  createTypePredicateNode(assertsModifier: AssertsKeyword?, parameterName: /* Identifier | ThisTypeNode | string */, type: TypeNode?): TypePredicateNode
fun  updateTypePredicateNode(node: TypePredicateNode, assertsModifier: AssertsKeyword?, parameterName: /* Identifier | ThisTypeNode */, type: TypeNode?): TypePredicateNode
fun  createTypeReferenceNode(typeName: /* string | EntityName */, typeArguments: Array<out TypeNode> = definedExternally): TypeReferenceNode
fun  updateTypeReferenceNode(node: TypeReferenceNode, typeName: EntityName, typeArguments: NodeArray<TypeNode>?): TypeReferenceNode
fun  createFunctionTypeNode(typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode): FunctionTypeNode
fun  updateFunctionTypeNode(node: FunctionTypeNode, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode): FunctionTypeNode
fun  createConstructorTypeNode(modifiers: Array<out Modifier>?, typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode): ConstructorTypeNode
fun  createConstructorTypeNode(typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode): ConstructorTypeNode
fun  updateConstructorTypeNode(node: ConstructorTypeNode, modifiers: Array<out Modifier>?, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode): ConstructorTypeNode
fun  updateConstructorTypeNode(node: ConstructorTypeNode, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode): ConstructorTypeNode
fun  createTypeQueryNode(exprName: EntityName): TypeQueryNode
fun  updateTypeQueryNode(node: TypeQueryNode, exprName: EntityName): TypeQueryNode
fun  createTypeLiteralNode(members: Array<out TypeElement>?): TypeLiteralNode
fun  updateTypeLiteralNode(node: TypeLiteralNode, members: NodeArray<TypeElement>): TypeLiteralNode
fun  createArrayTypeNode(elementType: TypeNode): ArrayTypeNode
fun  updateArrayTypeNode(node: ArrayTypeNode, elementType: TypeNode): ArrayTypeNode
fun  createTupleTypeNode(elements: Array<out (/* TypeNode | NamedTupleMember */)>): TupleTypeNode
fun  updateTupleTypeNode(node: TupleTypeNode, elements: Array<out (/* TypeNode | NamedTupleMember */)>): TupleTypeNode
fun  createNamedTupleMember(dotDotDotToken: DotDotDotToken?, name: Identifier, questionToken: QuestionToken?, type: TypeNode): NamedTupleMember
fun  updateNamedTupleMember(node: NamedTupleMember, dotDotDotToken: DotDotDotToken?, name: Identifier, questionToken: QuestionToken?, type: TypeNode): NamedTupleMember
fun  createOptionalTypeNode(type: TypeNode): OptionalTypeNode
fun  updateOptionalTypeNode(node: OptionalTypeNode, type: TypeNode): OptionalTypeNode
fun  createRestTypeNode(type: TypeNode): RestTypeNode
fun  updateRestTypeNode(node: RestTypeNode, type: TypeNode): RestTypeNode
fun  createUnionTypeNode(types: Array<out TypeNode>): UnionTypeNode
fun  updateUnionTypeNode(node: UnionTypeNode, types: NodeArray<TypeNode>): UnionTypeNode
fun  createIntersectionTypeNode(types: Array<out TypeNode>): IntersectionTypeNode
fun  updateIntersectionTypeNode(node: IntersectionTypeNode, types: NodeArray<TypeNode>): IntersectionTypeNode
fun  createConditionalTypeNode(checkType: TypeNode, extendsType: TypeNode, trueType: TypeNode, falseType: TypeNode): ConditionalTypeNode
fun  updateConditionalTypeNode(node: ConditionalTypeNode, checkType: TypeNode, extendsType: TypeNode, trueType: TypeNode, falseType: TypeNode): ConditionalTypeNode
fun  createInferTypeNode(typeParameter: TypeParameterDeclaration): InferTypeNode
fun  updateInferTypeNode(node: InferTypeNode, typeParameter: TypeParameterDeclaration): InferTypeNode
fun  createImportTypeNode(argument: TypeNode, qualifier: EntityName = definedExternally, typeArguments: Array<out TypeNode> = definedExternally, isTypeOf: Boolean = definedExternally): ImportTypeNode
fun  updateImportTypeNode(node: ImportTypeNode, argument: TypeNode, qualifier: EntityName?, typeArguments: Array<out TypeNode>?, isTypeOf: Boolean = definedExternally): ImportTypeNode
fun  createParenthesizedType(type: TypeNode): ParenthesizedTypeNode
fun  updateParenthesizedType(node: ParenthesizedTypeNode, type: TypeNode): ParenthesizedTypeNode
fun  createThisTypeNode(): ThisTypeNode
fun  createTypeOperatorNode(operator: /* SyntaxKind.KeyOfKeyword | SyntaxKind.UniqueKeyword | SyntaxKind.ReadonlyKeyword */, type: TypeNode): TypeOperatorNode
fun  updateTypeOperatorNode(node: TypeOperatorNode, type: TypeNode): TypeOperatorNode
fun  createIndexedAccessTypeNode(objectType: TypeNode, indexType: TypeNode): IndexedAccessTypeNode
fun  updateIndexedAccessTypeNode(node: IndexedAccessTypeNode, objectType: TypeNode, indexType: TypeNode): IndexedAccessTypeNode
fun  createMappedTypeNode(readonlyToken: /* ReadonlyKeyword | PlusToken | MinusToken | undefined */, typeParameter: TypeParameterDeclaration, nameType: TypeNode?, questionToken: /* QuestionToken | PlusToken | MinusToken | undefined */, type: TypeNode?): MappedTypeNode
fun  updateMappedTypeNode(node: MappedTypeNode, readonlyToken: /* ReadonlyKeyword | PlusToken | MinusToken | undefined */, typeParameter: TypeParameterDeclaration, nameType: TypeNode?, questionToken: /* QuestionToken | PlusToken | MinusToken | undefined */, type: TypeNode?): MappedTypeNode
fun  createLiteralTypeNode(literal: /* LiteralTypeNode["literal"] */): LiteralTypeNode
fun  updateLiteralTypeNode(node: LiteralTypeNode, literal: /* LiteralTypeNode["literal"] */): LiteralTypeNode
fun  createTemplateLiteralType(head: TemplateHead, templateSpans: Array<out TemplateLiteralTypeSpan>): TemplateLiteralTypeNode
fun  updateTemplateLiteralType(node: TemplateLiteralTypeNode, head: TemplateHead, templateSpans: Array<out TemplateLiteralTypeSpan>): TemplateLiteralTypeNode
fun  createObjectBindingPattern(elements: Array<out BindingElement>): ObjectBindingPattern
fun  updateObjectBindingPattern(node: ObjectBindingPattern, elements: Array<out BindingElement>): ObjectBindingPattern
fun  createArrayBindingPattern(elements: Array<out ArrayBindingElement>): ArrayBindingPattern
fun  updateArrayBindingPattern(node: ArrayBindingPattern, elements: Array<out ArrayBindingElement>): ArrayBindingPattern
fun  createBindingElement(dotDotDotToken: DotDotDotToken?, propertyName: /* string | PropertyName | undefined */, name: /* string | BindingName */, initializer: Expression = definedExternally): BindingElement
fun  updateBindingElement(node: BindingElement, dotDotDotToken: DotDotDotToken?, propertyName: PropertyName?, name: BindingName, initializer: Expression?): BindingElement
fun  createArrayLiteralExpression(elements: Array<out Expression> = definedExternally, multiLine: Boolean = definedExternally): ArrayLiteralExpression
fun  updateArrayLiteralExpression(node: ArrayLiteralExpression, elements: Array<out Expression>): ArrayLiteralExpression
fun  createObjectLiteralExpression(properties: Array<out ObjectLiteralElementLike> = definedExternally, multiLine: Boolean = definedExternally): ObjectLiteralExpression
fun  updateObjectLiteralExpression(node: ObjectLiteralExpression, properties: Array<out ObjectLiteralElementLike>): ObjectLiteralExpression
fun  createPropertyAccessExpression(expression: Expression, name: /* string | MemberName */): PropertyAccessExpression
fun  updatePropertyAccessExpression(node: PropertyAccessExpression, expression: Expression, name: MemberName): PropertyAccessExpression
fun  createPropertyAccessChain(expression: Expression, questionDotToken: QuestionDotToken?, name: /* string | MemberName */): PropertyAccessChain
fun  updatePropertyAccessChain(node: PropertyAccessChain, expression: Expression, questionDotToken: QuestionDotToken?, name: MemberName): PropertyAccessChain
fun  createElementAccessExpression(expression: Expression, index: /* number | Expression */): ElementAccessExpression
fun  updateElementAccessExpression(node: ElementAccessExpression, expression: Expression, argumentExpression: Expression): ElementAccessExpression
fun  createElementAccessChain(expression: Expression, questionDotToken: QuestionDotToken?, index: /* number | Expression */): ElementAccessChain
fun  updateElementAccessChain(node: ElementAccessChain, expression: Expression, questionDotToken: QuestionDotToken?, argumentExpression: Expression): ElementAccessChain
fun  createCallExpression(expression: Expression, typeArguments: Array<out TypeNode>?, argumentsArray: Array<out Expression>?): CallExpression
fun  updateCallExpression(node: CallExpression, expression: Expression, typeArguments: Array<out TypeNode>?, argumentsArray: Array<out Expression>): CallExpression
fun  createCallChain(expression: Expression, questionDotToken: QuestionDotToken?, typeArguments: Array<out TypeNode>?, argumentsArray: Array<out Expression>?): CallChain
fun  updateCallChain(node: CallChain, expression: Expression, questionDotToken: QuestionDotToken?, typeArguments: Array<out TypeNode>?, argumentsArray: Array<out Expression>): CallChain
fun  createNewExpression(expression: Expression, typeArguments: Array<out TypeNode>?, argumentsArray: Array<out Expression>?): NewExpression
fun  updateNewExpression(node: NewExpression, expression: Expression, typeArguments: Array<out TypeNode>?, argumentsArray: Array<out Expression>?): NewExpression
fun  createTaggedTemplateExpression(tag: Expression, typeArguments: Array<out TypeNode>?, template: TemplateLiteral): TaggedTemplateExpression
fun  updateTaggedTemplateExpression(node: TaggedTemplateExpression, tag: Expression, typeArguments: Array<out TypeNode>?, template: TemplateLiteral): TaggedTemplateExpression
fun  createTypeAssertion(type: TypeNode, expression: Expression): TypeAssertion
fun  updateTypeAssertion(node: TypeAssertion, type: TypeNode, expression: Expression): TypeAssertion
fun  createParenthesizedExpression(expression: Expression): ParenthesizedExpression
fun  updateParenthesizedExpression(node: ParenthesizedExpression, expression: Expression): ParenthesizedExpression
fun  createFunctionExpression(modifiers: Array<out Modifier>?, asteriskToken: AsteriskToken?, name: /* string | Identifier | undefined */, typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>?, type: TypeNode?, body: Block): FunctionExpression
fun  updateFunctionExpression(node: FunctionExpression, modifiers: Array<out Modifier>?, asteriskToken: AsteriskToken?, name: Identifier?, typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode?, body: Block): FunctionExpression
fun  createArrowFunction(modifiers: Array<out Modifier>?, typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode?, equalsGreaterThanToken: EqualsGreaterThanToken?, body: ConciseBody): ArrowFunction
fun  updateArrowFunction(node: ArrowFunction, modifiers: Array<out Modifier>?, typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode?, equalsGreaterThanToken: EqualsGreaterThanToken, body: ConciseBody): ArrowFunction
fun  createDeleteExpression(expression: Expression): DeleteExpression
fun  updateDeleteExpression(node: DeleteExpression, expression: Expression): DeleteExpression
fun  createTypeOfExpression(expression: Expression): TypeOfExpression
fun  updateTypeOfExpression(node: TypeOfExpression, expression: Expression): TypeOfExpression
fun  createVoidExpression(expression: Expression): VoidExpression
fun  updateVoidExpression(node: VoidExpression, expression: Expression): VoidExpression
fun  createAwaitExpression(expression: Expression): AwaitExpression
fun  updateAwaitExpression(node: AwaitExpression, expression: Expression): AwaitExpression
fun  createPrefixUnaryExpression(operator: PrefixUnaryOperator, operand: Expression): PrefixUnaryExpression
fun  updatePrefixUnaryExpression(node: PrefixUnaryExpression, operand: Expression): PrefixUnaryExpression
fun  createPostfixUnaryExpression(operand: Expression, operator: PostfixUnaryOperator): PostfixUnaryExpression
fun  updatePostfixUnaryExpression(node: PostfixUnaryExpression, operand: Expression): PostfixUnaryExpression
fun  createBinaryExpression(left: Expression, operator: /* BinaryOperator | BinaryOperatorToken */, right: Expression): BinaryExpression
fun  updateBinaryExpression(node: BinaryExpression, left: Expression, operator: /* BinaryOperator | BinaryOperatorToken */, right: Expression): BinaryExpression
fun  createConditionalExpression(condition: Expression, questionToken: QuestionToken?, whenTrue: Expression, colonToken: ColonToken?, whenFalse: Expression): ConditionalExpression
fun  updateConditionalExpression(node: ConditionalExpression, condition: Expression, questionToken: QuestionToken, whenTrue: Expression, colonToken: ColonToken, whenFalse: Expression): ConditionalExpression
fun  createTemplateExpression(head: TemplateHead, templateSpans: Array<out TemplateSpan>): TemplateExpression
fun  updateTemplateExpression(node: TemplateExpression, head: TemplateHead, templateSpans: Array<out TemplateSpan>): TemplateExpression
fun  createTemplateHead(text: String, rawText: String = definedExternally, templateFlags: TokenFlags = definedExternally): TemplateHead
fun  createTemplateHead(text: String?, rawText: String, templateFlags: TokenFlags = definedExternally): TemplateHead
fun  createTemplateMiddle(text: String, rawText: String = definedExternally, templateFlags: TokenFlags = definedExternally): TemplateMiddle
fun  createTemplateMiddle(text: String?, rawText: String, templateFlags: TokenFlags = definedExternally): TemplateMiddle
fun  createTemplateTail(text: String, rawText: String = definedExternally, templateFlags: TokenFlags = definedExternally): TemplateTail
fun  createTemplateTail(text: String?, rawText: String, templateFlags: TokenFlags = definedExternally): TemplateTail
fun  createNoSubstitutionTemplateLiteral(text: String, rawText: String = definedExternally): NoSubstitutionTemplateLiteral
fun  createNoSubstitutionTemplateLiteral(text: String?, rawText: String): NoSubstitutionTemplateLiteral
fun  createYieldExpression(asteriskToken: AsteriskToken, expression: Expression): YieldExpression
fun  createYieldExpression(asteriskToken: null, expression: Expression?): YieldExpression
fun  updateYieldExpression(node: YieldExpression, asteriskToken: AsteriskToken?, expression: Expression?): YieldExpression
fun  createSpreadElement(expression: Expression): SpreadElement
fun  updateSpreadElement(node: SpreadElement, expression: Expression): SpreadElement
fun  createClassExpression(decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: /* string | Identifier | undefined */, typeParameters: Array<out TypeParameterDeclaration>?, heritageClauses: Array<out HeritageClause>?, members: Array<out ClassElement>): ClassExpression
fun  updateClassExpression(node: ClassExpression, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: Identifier?, typeParameters: Array<out TypeParameterDeclaration>?, heritageClauses: Array<out HeritageClause>?, members: Array<out ClassElement>): ClassExpression
fun  createOmittedExpression(): OmittedExpression
fun  createExpressionWithTypeArguments(expression: Expression, typeArguments: Array<out TypeNode>?): ExpressionWithTypeArguments
fun  updateExpressionWithTypeArguments(node: ExpressionWithTypeArguments, expression: Expression, typeArguments: Array<out TypeNode>?): ExpressionWithTypeArguments
fun  createAsExpression(expression: Expression, type: TypeNode): AsExpression
fun  updateAsExpression(node: AsExpression, expression: Expression, type: TypeNode): AsExpression
fun  createNonNullExpression(expression: Expression): NonNullExpression
fun  updateNonNullExpression(node: NonNullExpression, expression: Expression): NonNullExpression
fun  createNonNullChain(expression: Expression): NonNullChain
fun  updateNonNullChain(node: NonNullChain, expression: Expression): NonNullChain
fun  createMetaProperty(keywordToken: /* MetaProperty["keywordToken"] */, name: Identifier): MetaProperty
fun  updateMetaProperty(node: MetaProperty, name: Identifier): MetaProperty
fun  createTemplateSpan(expression: Expression, literal: /* TemplateMiddle | TemplateTail */): TemplateSpan
fun  updateTemplateSpan(node: TemplateSpan, expression: Expression, literal: /* TemplateMiddle | TemplateTail */): TemplateSpan
fun  createSemicolonClassElement(): SemicolonClassElement
fun  createBlock(statements: Array<out Statement>, multiLine: Boolean = definedExternally): Block
fun  updateBlock(node: Block, statements: Array<out Statement>): Block
fun  createVariableStatement(modifiers: Array<out Modifier>?, declarationList: /* VariableDeclarationList | readonly VariableDeclaration[] */): VariableStatement
fun  updateVariableStatement(node: VariableStatement, modifiers: Array<out Modifier>?, declarationList: VariableDeclarationList): VariableStatement
fun  createEmptyStatement(): EmptyStatement
fun  createExpressionStatement(expression: Expression): ExpressionStatement
fun  updateExpressionStatement(node: ExpressionStatement, expression: Expression): ExpressionStatement
fun  createIfStatement(expression: Expression, thenStatement: Statement, elseStatement: Statement = definedExternally): IfStatement
fun  updateIfStatement(node: IfStatement, expression: Expression, thenStatement: Statement, elseStatement: Statement?): IfStatement
fun  createDoStatement(statement: Statement, expression: Expression): DoStatement
fun  updateDoStatement(node: DoStatement, statement: Statement, expression: Expression): DoStatement
fun  createWhileStatement(expression: Expression, statement: Statement): WhileStatement
fun  updateWhileStatement(node: WhileStatement, expression: Expression, statement: Statement): WhileStatement
fun  createForStatement(initializer: ForInitializer?, condition: Expression?, incrementor: Expression?, statement: Statement): ForStatement
fun  updateForStatement(node: ForStatement, initializer: ForInitializer?, condition: Expression?, incrementor: Expression?, statement: Statement): ForStatement
fun  createForInStatement(initializer: ForInitializer, expression: Expression, statement: Statement): ForInStatement
fun  updateForInStatement(node: ForInStatement, initializer: ForInitializer, expression: Expression, statement: Statement): ForInStatement
fun  createForOfStatement(awaitModifier: AwaitKeyword?, initializer: ForInitializer, expression: Expression, statement: Statement): ForOfStatement
fun  updateForOfStatement(node: ForOfStatement, awaitModifier: AwaitKeyword?, initializer: ForInitializer, expression: Expression, statement: Statement): ForOfStatement
fun  createContinueStatement(label: /* string | Identifier */ = definedExternally): ContinueStatement
fun  updateContinueStatement(node: ContinueStatement, label: Identifier?): ContinueStatement
fun  createBreakStatement(label: /* string | Identifier */ = definedExternally): BreakStatement
fun  updateBreakStatement(node: BreakStatement, label: Identifier?): BreakStatement
fun  createReturnStatement(expression: Expression = definedExternally): ReturnStatement
fun  updateReturnStatement(node: ReturnStatement, expression: Expression?): ReturnStatement
fun  createWithStatement(expression: Expression, statement: Statement): WithStatement
fun  updateWithStatement(node: WithStatement, expression: Expression, statement: Statement): WithStatement
fun  createSwitchStatement(expression: Expression, caseBlock: CaseBlock): SwitchStatement
fun  updateSwitchStatement(node: SwitchStatement, expression: Expression, caseBlock: CaseBlock): SwitchStatement
fun  createLabeledStatement(label: /* string | Identifier */, statement: Statement): LabeledStatement
fun  updateLabeledStatement(node: LabeledStatement, label: Identifier, statement: Statement): LabeledStatement
fun  createThrowStatement(expression: Expression): ThrowStatement
fun  updateThrowStatement(node: ThrowStatement, expression: Expression): ThrowStatement
fun  createTryStatement(tryBlock: Block, catchClause: CatchClause?, finallyBlock: Block?): TryStatement
fun  updateTryStatement(node: TryStatement, tryBlock: Block, catchClause: CatchClause?, finallyBlock: Block?): TryStatement
fun  createDebuggerStatement(): DebuggerStatement
fun  createVariableDeclaration(name: /* string | BindingName */, exclamationToken: ExclamationToken = definedExternally, type: TypeNode = definedExternally, initializer: Expression = definedExternally): VariableDeclaration
fun  updateVariableDeclaration(node: VariableDeclaration, name: BindingName, exclamationToken: ExclamationToken?, type: TypeNode?, initializer: Expression?): VariableDeclaration
fun  createVariableDeclarationList(declarations: Array<out VariableDeclaration>, flags: NodeFlags = definedExternally): VariableDeclarationList
fun  updateVariableDeclarationList(node: VariableDeclarationList, declarations: Array<out VariableDeclaration>): VariableDeclarationList
fun  createFunctionDeclaration(decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, asteriskToken: AsteriskToken?, name: /* string | Identifier | undefined */, typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode?, body: Block?): FunctionDeclaration
fun  updateFunctionDeclaration(node: FunctionDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, asteriskToken: AsteriskToken?, name: Identifier?, typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode?, body: Block?): FunctionDeclaration
fun  createClassDeclaration(decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: /* string | Identifier | undefined */, typeParameters: Array<out TypeParameterDeclaration>?, heritageClauses: Array<out HeritageClause>?, members: Array<out ClassElement>): ClassDeclaration
fun  updateClassDeclaration(node: ClassDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: Identifier?, typeParameters: Array<out TypeParameterDeclaration>?, heritageClauses: Array<out HeritageClause>?, members: Array<out ClassElement>): ClassDeclaration
fun  createInterfaceDeclaration(decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: /* string | Identifier */, typeParameters: Array<out TypeParameterDeclaration>?, heritageClauses: Array<out HeritageClause>?, members: Array<out TypeElement>): InterfaceDeclaration
fun  updateInterfaceDeclaration(node: InterfaceDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: Identifier, typeParameters: Array<out TypeParameterDeclaration>?, heritageClauses: Array<out HeritageClause>?, members: Array<out TypeElement>): InterfaceDeclaration
fun  createTypeAliasDeclaration(decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: /* string | Identifier */, typeParameters: Array<out TypeParameterDeclaration>?, type: TypeNode): TypeAliasDeclaration
fun  updateTypeAliasDeclaration(node: TypeAliasDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: Identifier, typeParameters: Array<out TypeParameterDeclaration>?, type: TypeNode): TypeAliasDeclaration
fun  createEnumDeclaration(decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: /* string | Identifier */, members: Array<out EnumMember>): EnumDeclaration
fun  updateEnumDeclaration(node: EnumDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: Identifier, members: Array<out EnumMember>): EnumDeclaration
fun  createModuleDeclaration(decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: ModuleName, body: ModuleBody?, flags: NodeFlags = definedExternally): ModuleDeclaration
fun  updateModuleDeclaration(node: ModuleDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: ModuleName, body: ModuleBody?): ModuleDeclaration
fun  createModuleBlock(statements: Array<out Statement>): ModuleBlock
fun  updateModuleBlock(node: ModuleBlock, statements: Array<out Statement>): ModuleBlock
fun  createCaseBlock(clauses: Array<out CaseOrDefaultClause>): CaseBlock
fun  updateCaseBlock(node: CaseBlock, clauses: Array<out CaseOrDefaultClause>): CaseBlock
fun  createNamespaceExportDeclaration(name: /* string | Identifier */): NamespaceExportDeclaration
fun  updateNamespaceExportDeclaration(node: NamespaceExportDeclaration, name: Identifier): NamespaceExportDeclaration
fun  createImportEqualsDeclaration(decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, isTypeOnly: Boolean, name: /* string | Identifier */, moduleReference: ModuleReference): ImportEqualsDeclaration
fun  updateImportEqualsDeclaration(node: ImportEqualsDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, isTypeOnly: Boolean, name: Identifier, moduleReference: ModuleReference): ImportEqualsDeclaration
fun  createImportDeclaration(decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, importClause: ImportClause?, moduleSpecifier: Expression): ImportDeclaration
fun  updateImportDeclaration(node: ImportDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, importClause: ImportClause?, moduleSpecifier: Expression): ImportDeclaration
fun  createImportClause(isTypeOnly: Boolean, name: Identifier?, namedBindings: NamedImportBindings?): ImportClause
fun  updateImportClause(node: ImportClause, isTypeOnly: Boolean, name: Identifier?, namedBindings: NamedImportBindings?): ImportClause
fun  createNamespaceImport(name: Identifier): NamespaceImport
fun  updateNamespaceImport(node: NamespaceImport, name: Identifier): NamespaceImport
fun  createNamespaceExport(name: Identifier): NamespaceExport
fun  updateNamespaceExport(node: NamespaceExport, name: Identifier): NamespaceExport
fun  createNamedImports(elements: Array<out ImportSpecifier>): NamedImports
fun  updateNamedImports(node: NamedImports, elements: Array<out ImportSpecifier>): NamedImports
fun  createImportSpecifier(propertyName: Identifier?, name: Identifier): ImportSpecifier
fun  updateImportSpecifier(node: ImportSpecifier, propertyName: Identifier?, name: Identifier): ImportSpecifier
fun  createExportAssignment(decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, isExportEquals: Boolean?, expression: Expression): ExportAssignment
fun  updateExportAssignment(node: ExportAssignment, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, expression: Expression): ExportAssignment
fun  createExportDeclaration(decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, isTypeOnly: Boolean, exportClause: NamedExportBindings?, moduleSpecifier: Expression = definedExternally): ExportDeclaration
fun  updateExportDeclaration(node: ExportDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, isTypeOnly: Boolean, exportClause: NamedExportBindings?, moduleSpecifier: Expression?): ExportDeclaration
fun  createNamedExports(elements: Array<out ExportSpecifier>): NamedExports
fun  updateNamedExports(node: NamedExports, elements: Array<out ExportSpecifier>): NamedExports
fun  createExportSpecifier(propertyName: /* string | Identifier | undefined */, name: /* string | Identifier */): ExportSpecifier
fun  updateExportSpecifier(node: ExportSpecifier, propertyName: Identifier?, name: Identifier): ExportSpecifier
fun  createExternalModuleReference(expression: Expression): ExternalModuleReference
fun  updateExternalModuleReference(node: ExternalModuleReference, expression: Expression): ExternalModuleReference
fun  createJSDocAllType(): JSDocAllType
fun  createJSDocUnknownType(): JSDocUnknownType
fun  createJSDocNonNullableType(type: TypeNode): JSDocNonNullableType
fun  updateJSDocNonNullableType(node: JSDocNonNullableType, type: TypeNode): JSDocNonNullableType
fun  createJSDocNullableType(type: TypeNode): JSDocNullableType
fun  updateJSDocNullableType(node: JSDocNullableType, type: TypeNode): JSDocNullableType
fun  createJSDocOptionalType(type: TypeNode): JSDocOptionalType
fun  updateJSDocOptionalType(node: JSDocOptionalType, type: TypeNode): JSDocOptionalType
fun  createJSDocFunctionType(parameters: Array<out ParameterDeclaration>, type: TypeNode?): JSDocFunctionType
fun  updateJSDocFunctionType(node: JSDocFunctionType, parameters: Array<out ParameterDeclaration>, type: TypeNode?): JSDocFunctionType
fun  createJSDocVariadicType(type: TypeNode): JSDocVariadicType
fun  updateJSDocVariadicType(node: JSDocVariadicType, type: TypeNode): JSDocVariadicType
fun  createJSDocNamepathType(type: TypeNode): JSDocNamepathType
fun  updateJSDocNamepathType(node: JSDocNamepathType, type: TypeNode): JSDocNamepathType
fun  createJSDocTypeExpression(type: TypeNode): JSDocTypeExpression
fun  updateJSDocTypeExpression(node: JSDocTypeExpression, type: TypeNode): JSDocTypeExpression
fun  createJSDocNameReference(name: /* EntityName | JSDocMemberName */): JSDocNameReference
fun  updateJSDocNameReference(node: JSDocNameReference, name: /* EntityName | JSDocMemberName */): JSDocNameReference
fun  createJSDocMemberName(left: /* EntityName | JSDocMemberName */, right: Identifier): JSDocMemberName
fun  updateJSDocMemberName(node: JSDocMemberName, left: /* EntityName | JSDocMemberName */, right: Identifier): JSDocMemberName
fun  createJSDocLink(name: /* EntityName | JSDocMemberName | undefined */, text: String): JSDocLink
fun  updateJSDocLink(node: JSDocLink, name: /* EntityName | JSDocMemberName | undefined */, text: String): JSDocLink
fun  createJSDocLinkCode(name: /* EntityName | JSDocMemberName | undefined */, text: String): JSDocLinkCode
fun  updateJSDocLinkCode(node: JSDocLinkCode, name: /* EntityName | JSDocMemberName | undefined */, text: String): JSDocLinkCode
fun  createJSDocLinkPlain(name: /* EntityName | JSDocMemberName | undefined */, text: String): JSDocLinkPlain
fun  updateJSDocLinkPlain(node: JSDocLinkPlain, name: /* EntityName | JSDocMemberName | undefined */, text: String): JSDocLinkPlain
fun  createJSDocTypeLiteral(jsDocPropertyTags: Array<out JSDocPropertyLikeTag> = definedExternally, isArrayType: Boolean = definedExternally): JSDocTypeLiteral
fun  updateJSDocTypeLiteral(node: JSDocTypeLiteral, jsDocPropertyTags: Array<out JSDocPropertyLikeTag>?, isArrayType: Boolean?): JSDocTypeLiteral
fun  createJSDocSignature(typeParameters: Array<out JSDocTemplateTag>?, parameters: Array<out JSDocParameterTag>, type: JSDocReturnTag = definedExternally): JSDocSignature
fun  updateJSDocSignature(node: JSDocSignature, typeParameters: Array<out JSDocTemplateTag>?, parameters: Array<out JSDocParameterTag>, type: JSDocReturnTag?): JSDocSignature
fun  createJSDocTemplateTag(tagName: Identifier?, constraint: JSDocTypeExpression?, typeParameters: Array<out TypeParameterDeclaration>, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocTemplateTag
fun  updateJSDocTemplateTag(node: JSDocTemplateTag, tagName: Identifier?, constraint: JSDocTypeExpression?, typeParameters: Array<out TypeParameterDeclaration>, comment: /* string | NodeArray<JSDocComment> | undefined */): JSDocTemplateTag
fun  createJSDocTypedefTag(tagName: Identifier?, typeExpression: /* JSDocTypeExpression | JSDocTypeLiteral */ = definedExternally, fullName: /* Identifier | JSDocNamespaceDeclaration */ = definedExternally, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocTypedefTag
fun  updateJSDocTypedefTag(node: JSDocTypedefTag, tagName: Identifier?, typeExpression: /* JSDocTypeExpression | JSDocTypeLiteral | undefined */, fullName: /* Identifier | JSDocNamespaceDeclaration | undefined */, comment: /* string | NodeArray<JSDocComment> | undefined */): JSDocTypedefTag
fun  createJSDocParameterTag(tagName: Identifier?, name: EntityName, isBracketed: Boolean, typeExpression: JSDocTypeExpression = definedExternally, isNameFirst: Boolean = definedExternally, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocParameterTag
fun  updateJSDocParameterTag(node: JSDocParameterTag, tagName: Identifier?, name: EntityName, isBracketed: Boolean, typeExpression: JSDocTypeExpression?, isNameFirst: Boolean, comment: /* string | NodeArray<JSDocComment> | undefined */): JSDocParameterTag
fun  createJSDocPropertyTag(tagName: Identifier?, name: EntityName, isBracketed: Boolean, typeExpression: JSDocTypeExpression = definedExternally, isNameFirst: Boolean = definedExternally, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocPropertyTag
fun  updateJSDocPropertyTag(node: JSDocPropertyTag, tagName: Identifier?, name: EntityName, isBracketed: Boolean, typeExpression: JSDocTypeExpression?, isNameFirst: Boolean, comment: /* string | NodeArray<JSDocComment> | undefined */): JSDocPropertyTag
fun  createJSDocTypeTag(tagName: Identifier?, typeExpression: JSDocTypeExpression, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocTypeTag
fun  updateJSDocTypeTag(node: JSDocTypeTag, tagName: Identifier?, typeExpression: JSDocTypeExpression, comment: /* string | NodeArray<JSDocComment> | undefined */): JSDocTypeTag
fun  createJSDocSeeTag(tagName: Identifier?, nameExpression: JSDocNameReference?, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocSeeTag
fun  updateJSDocSeeTag(node: JSDocSeeTag, tagName: Identifier?, nameExpression: JSDocNameReference?, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocSeeTag
fun  createJSDocReturnTag(tagName: Identifier?, typeExpression: JSDocTypeExpression = definedExternally, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocReturnTag
fun  updateJSDocReturnTag(node: JSDocReturnTag, tagName: Identifier?, typeExpression: JSDocTypeExpression?, comment: /* string | NodeArray<JSDocComment> | undefined */): JSDocReturnTag
fun  createJSDocThisTag(tagName: Identifier?, typeExpression: JSDocTypeExpression, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocThisTag
fun  updateJSDocThisTag(node: JSDocThisTag, tagName: Identifier?, typeExpression: JSDocTypeExpression?, comment: /* string | NodeArray<JSDocComment> | undefined */): JSDocThisTag
fun  createJSDocEnumTag(tagName: Identifier?, typeExpression: JSDocTypeExpression, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocEnumTag
fun  updateJSDocEnumTag(node: JSDocEnumTag, tagName: Identifier?, typeExpression: JSDocTypeExpression, comment: /* string | NodeArray<JSDocComment> | undefined */): JSDocEnumTag
fun  createJSDocCallbackTag(tagName: Identifier?, typeExpression: JSDocSignature, fullName: /* Identifier | JSDocNamespaceDeclaration */ = definedExternally, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocCallbackTag
fun  updateJSDocCallbackTag(node: JSDocCallbackTag, tagName: Identifier?, typeExpression: JSDocSignature, fullName: /* Identifier | JSDocNamespaceDeclaration | undefined */, comment: /* string | NodeArray<JSDocComment> | undefined */): JSDocCallbackTag
fun  createJSDocAugmentsTag(tagName: Identifier?, className: /* JSDocAugmentsTag["class"] */, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocAugmentsTag
fun  updateJSDocAugmentsTag(node: JSDocAugmentsTag, tagName: Identifier?, className: /* JSDocAugmentsTag["class"] */, comment: /* string | NodeArray<JSDocComment> | undefined */): JSDocAugmentsTag
fun  createJSDocImplementsTag(tagName: Identifier?, className: /* JSDocImplementsTag["class"] */, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocImplementsTag
fun  updateJSDocImplementsTag(node: JSDocImplementsTag, tagName: Identifier?, className: /* JSDocImplementsTag["class"] */, comment: /* string | NodeArray<JSDocComment> | undefined */): JSDocImplementsTag
fun  createJSDocAuthorTag(tagName: Identifier?, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocAuthorTag
fun  updateJSDocAuthorTag(node: JSDocAuthorTag, tagName: Identifier?, comment: /* string | NodeArray<JSDocComment> | undefined */): JSDocAuthorTag
fun  createJSDocClassTag(tagName: Identifier?, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocClassTag
fun  updateJSDocClassTag(node: JSDocClassTag, tagName: Identifier?, comment: /* string | NodeArray<JSDocComment> | undefined */): JSDocClassTag
fun  createJSDocPublicTag(tagName: Identifier?, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocPublicTag
fun  updateJSDocPublicTag(node: JSDocPublicTag, tagName: Identifier?, comment: /* string | NodeArray<JSDocComment> | undefined */): JSDocPublicTag
fun  createJSDocPrivateTag(tagName: Identifier?, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocPrivateTag
fun  updateJSDocPrivateTag(node: JSDocPrivateTag, tagName: Identifier?, comment: /* string | NodeArray<JSDocComment> | undefined */): JSDocPrivateTag
fun  createJSDocProtectedTag(tagName: Identifier?, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocProtectedTag
fun  updateJSDocProtectedTag(node: JSDocProtectedTag, tagName: Identifier?, comment: /* string | NodeArray<JSDocComment> | undefined */): JSDocProtectedTag
fun  createJSDocReadonlyTag(tagName: Identifier?, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocReadonlyTag
fun  updateJSDocReadonlyTag(node: JSDocReadonlyTag, tagName: Identifier?, comment: /* string | NodeArray<JSDocComment> | undefined */): JSDocReadonlyTag
fun  createJSDocUnknownTag(tagName: Identifier, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocUnknownTag
fun  updateJSDocUnknownTag(node: JSDocUnknownTag, tagName: Identifier, comment: /* string | NodeArray<JSDocComment> | undefined */): JSDocUnknownTag
fun  createJSDocDeprecatedTag(tagName: Identifier, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocDeprecatedTag
fun  updateJSDocDeprecatedTag(node: JSDocDeprecatedTag, tagName: Identifier, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocDeprecatedTag
fun  createJSDocOverrideTag(tagName: Identifier, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocOverrideTag
fun  updateJSDocOverrideTag(node: JSDocOverrideTag, tagName: Identifier, comment: /* string | NodeArray<JSDocComment> */ = definedExternally): JSDocOverrideTag
fun  createJSDocText(text: String): JSDocText
fun  updateJSDocText(node: JSDocText, text: String): JSDocText
fun  createJSDocComment(comment: /* string | NodeArray<JSDocComment> | undefined */ = definedExternally, tags: Array<out JSDocTag>? = definedExternally): JSDoc
fun  updateJSDocComment(node: JSDoc, comment: /* string | NodeArray<JSDocComment> | undefined */, tags: Array<out JSDocTag>?): JSDoc
fun  createJsxElement(openingElement: JsxOpeningElement, children: Array<out JsxChild>, closingElement: JsxClosingElement): JsxElement
fun  updateJsxElement(node: JsxElement, openingElement: JsxOpeningElement, children: Array<out JsxChild>, closingElement: JsxClosingElement): JsxElement
fun  createJsxSelfClosingElement(tagName: JsxTagNameExpression, typeArguments: Array<out TypeNode>?, attributes: JsxAttributes): JsxSelfClosingElement
fun  updateJsxSelfClosingElement(node: JsxSelfClosingElement, tagName: JsxTagNameExpression, typeArguments: Array<out TypeNode>?, attributes: JsxAttributes): JsxSelfClosingElement
fun  createJsxOpeningElement(tagName: JsxTagNameExpression, typeArguments: Array<out TypeNode>?, attributes: JsxAttributes): JsxOpeningElement
fun  updateJsxOpeningElement(node: JsxOpeningElement, tagName: JsxTagNameExpression, typeArguments: Array<out TypeNode>?, attributes: JsxAttributes): JsxOpeningElement
fun  createJsxClosingElement(tagName: JsxTagNameExpression): JsxClosingElement
fun  updateJsxClosingElement(node: JsxClosingElement, tagName: JsxTagNameExpression): JsxClosingElement
fun  createJsxFragment(openingFragment: JsxOpeningFragment, children: Array<out JsxChild>, closingFragment: JsxClosingFragment): JsxFragment
fun  createJsxText(text: String, containsOnlyTriviaWhiteSpaces: Boolean = definedExternally): JsxText
fun  updateJsxText(node: JsxText, text: String, containsOnlyTriviaWhiteSpaces: Boolean = definedExternally): JsxText
fun  createJsxOpeningFragment(): JsxOpeningFragment
fun  createJsxJsxClosingFragment(): JsxClosingFragment
fun  updateJsxFragment(node: JsxFragment, openingFragment: JsxOpeningFragment, children: Array<out JsxChild>, closingFragment: JsxClosingFragment): JsxFragment
fun  createJsxAttribute(name: Identifier, initializer: /* StringLiteral | JsxExpression | undefined */): JsxAttribute
fun  updateJsxAttribute(node: JsxAttribute, name: Identifier, initializer: /* StringLiteral | JsxExpression | undefined */): JsxAttribute
fun  createJsxAttributes(properties: Array<out JsxAttributeLike>): JsxAttributes
fun  updateJsxAttributes(node: JsxAttributes, properties: Array<out JsxAttributeLike>): JsxAttributes
fun  createJsxSpreadAttribute(expression: Expression): JsxSpreadAttribute
fun  updateJsxSpreadAttribute(node: JsxSpreadAttribute, expression: Expression): JsxSpreadAttribute
fun  createJsxExpression(dotDotDotToken: DotDotDotToken?, expression: Expression?): JsxExpression
fun  updateJsxExpression(node: JsxExpression, expression: Expression?): JsxExpression
fun  createCaseClause(expression: Expression, statements: Array<out Statement>): CaseClause
fun  updateCaseClause(node: CaseClause, expression: Expression, statements: Array<out Statement>): CaseClause
fun  createDefaultClause(statements: Array<out Statement>): DefaultClause
fun  updateDefaultClause(node: DefaultClause, statements: Array<out Statement>): DefaultClause
fun  createHeritageClause(token: /* HeritageClause["token"] */, types: Array<out ExpressionWithTypeArguments>): HeritageClause
fun  updateHeritageClause(node: HeritageClause, types: Array<out ExpressionWithTypeArguments>): HeritageClause
fun  createCatchClause(variableDeclaration: /* string | VariableDeclaration | undefined */, block: Block): CatchClause
fun  updateCatchClause(node: CatchClause, variableDeclaration: VariableDeclaration?, block: Block): CatchClause
fun  createPropertyAssignment(name: /* string | PropertyName */, initializer: Expression): PropertyAssignment
fun  updatePropertyAssignment(node: PropertyAssignment, name: PropertyName, initializer: Expression): PropertyAssignment
fun  createShorthandPropertyAssignment(name: /* string | Identifier */, objectAssignmentInitializer: Expression = definedExternally): ShorthandPropertyAssignment
fun  updateShorthandPropertyAssignment(node: ShorthandPropertyAssignment, name: Identifier, objectAssignmentInitializer: Expression?): ShorthandPropertyAssignment
fun  createSpreadAssignment(expression: Expression): SpreadAssignment
fun  updateSpreadAssignment(node: SpreadAssignment, expression: Expression): SpreadAssignment
fun  createEnumMember(name: /* string | PropertyName */, initializer: Expression = definedExternally): EnumMember
fun  updateEnumMember(node: EnumMember, name: PropertyName, initializer: Expression?): EnumMember
fun  createSourceFile(statements: Array<out Statement>, endOfFileToken: EndOfFileToken, flags: NodeFlags): SourceFile
fun  updateSourceFile(node: SourceFile, statements: Array<out Statement>, isDeclarationFile: Boolean = definedExternally, referencedFiles: Array<out FileReference> = definedExternally, typeReferences: Array<out FileReference> = definedExternally, hasNoDefaultLib: Boolean = definedExternally, libReferences: Array<out FileReference> = definedExternally): SourceFile
fun  createNotEmittedStatement(original: Node): NotEmittedStatement
fun  createPartiallyEmittedExpression(expression: Expression, original: Node = definedExternally): PartiallyEmittedExpression
fun  updatePartiallyEmittedExpression(node: PartiallyEmittedExpression, expression: Expression): PartiallyEmittedExpression
fun  createCommaListExpression(elements: Array<out Expression>): CommaListExpression
fun  updateCommaListExpression(node: CommaListExpression, elements: Array<out Expression>): CommaListExpression
fun  createBundle(sourceFiles: Array<out SourceFile>, prepends: Array<out (/* UnparsedSource | InputFiles */)> = definedExternally): Bundle
fun  updateBundle(node: Bundle, sourceFiles: Array<out SourceFile>, prepends: Array<out (/* UnparsedSource | InputFiles */)> = definedExternally): Bundle
fun  createComma(left: Expression, right: Expression): BinaryExpression
fun  createAssignment(left: /* ObjectLiteralExpression | ArrayLiteralExpression */, right: Expression): DestructuringAssignment
fun  createAssignment(left: Expression, right: Expression): AssignmentExpression<EqualsToken>
fun  createLogicalOr(left: Expression, right: Expression): BinaryExpression
fun  createLogicalAnd(left: Expression, right: Expression): BinaryExpression
fun  createBitwiseOr(left: Expression, right: Expression): BinaryExpression
fun  createBitwiseXor(left: Expression, right: Expression): BinaryExpression
fun  createBitwiseAnd(left: Expression, right: Expression): BinaryExpression
fun  createStrictEquality(left: Expression, right: Expression): BinaryExpression
fun  createStrictInequality(left: Expression, right: Expression): BinaryExpression
fun  createEquality(left: Expression, right: Expression): BinaryExpression
fun  createInequality(left: Expression, right: Expression): BinaryExpression
fun  createLessThan(left: Expression, right: Expression): BinaryExpression
fun  createLessThanEquals(left: Expression, right: Expression): BinaryExpression
fun  createGreaterThan(left: Expression, right: Expression): BinaryExpression
fun  createGreaterThanEquals(left: Expression, right: Expression): BinaryExpression
fun  createLeftShift(left: Expression, right: Expression): BinaryExpression
fun  createRightShift(left: Expression, right: Expression): BinaryExpression
fun  createUnsignedRightShift(left: Expression, right: Expression): BinaryExpression
fun  createAdd(left: Expression, right: Expression): BinaryExpression
fun  createSubtract(left: Expression, right: Expression): BinaryExpression
fun  createMultiply(left: Expression, right: Expression): BinaryExpression
fun  createDivide(left: Expression, right: Expression): BinaryExpression
fun  createModulo(left: Expression, right: Expression): BinaryExpression
fun  createExponent(left: Expression, right: Expression): BinaryExpression
fun  createPrefixPlus(operand: Expression): PrefixUnaryExpression
fun  createPrefixMinus(operand: Expression): PrefixUnaryExpression
fun  createPrefixIncrement(operand: Expression): PrefixUnaryExpression
fun  createPrefixDecrement(operand: Expression): PrefixUnaryExpression
fun  createBitwiseNot(operand: Expression): PrefixUnaryExpression
fun  createLogicalNot(operand: Expression): PrefixUnaryExpression
fun  createPostfixIncrement(operand: Expression): PostfixUnaryExpression
fun  createPostfixDecrement(operand: Expression): PostfixUnaryExpression
fun  createImmediatelyInvokedFunctionExpression(statements: Array<out Statement>): CallExpression
fun  createImmediatelyInvokedFunctionExpression(statements: Array<out Statement>, param: ParameterDeclaration, paramValue: Expression): CallExpression
fun  createImmediatelyInvokedArrowFunction(statements: Array<out Statement>): CallExpression
fun  createImmediatelyInvokedArrowFunction(statements: Array<out Statement>, param: ParameterDeclaration, paramValue: Expression): CallExpression
fun  createVoidZero(): VoidExpression
fun  createExportDefault(expression: Expression): ExportAssignment
fun  createExternalModuleExport(exportName: Identifier): ExportDeclaration
fun  restoreOuterExpressions(outerExpression: Expression?, innerExpression: Expression, kinds: OuterExpressionKinds = definedExternally): Expression
        }
    

        external interface CoreTransformationContext   {
            val factory: NodeFactory
fun  getCompilerOptions(): CompilerOptions
fun  startLexicalEnvironment(): Unit
fun  suspendLexicalEnvironment(): Unit
fun  resumeLexicalEnvironment(): Unit
fun  endLexicalEnvironment(): Array<Statement>?
fun  hoistFunctionDeclaration(node: FunctionDeclaration): Unit
fun  hoistVariableDeclaration(node: Identifier): Unit
        }
    

        external interface TransformationContext   : CoreTransformationContext {
            fun  requestEmitHelper(helper: EmitHelper): Unit
fun  readEmitHelpers(): Array<EmitHelper>?
fun  enableSubstitution(kind: SyntaxKind): Unit
fun  isSubstitutionEnabled(node: Node): Boolean
var onSubstituteNode: (hint: EmitHint, node: Node) -> Node
fun  enableEmitNotification(kind: SyntaxKind): Unit
fun  isEmitNotificationEnabled(node: Node): Boolean
var onEmitNode: (hint: EmitHint, node: Node, emitCallback: (hint: EmitHint, node: Node) -> Unit) -> Unit
        }
    

        external interface TransformationResult <T : Node >  {
            var transformed: Array<T>
var diagnostics: Array<DiagnosticWithLocation>?
fun  substituteNode(hint: EmitHint, node: Node): Node
fun  emitNodeWithNotification(hint: EmitHint, node: Node, emitCallback: (hint: EmitHint, node: Node) -> Unit): Unit
fun  isEmitNotificationEnabled(node: Node): Boolean
fun  dispose(): Unit
        }
    
typealias TransformerFactory<T : Node > = (context: TransformationContext) -> Transformer<T>
typealias Transformer<T : Node > = (node: T) -> T
typealias Visitor = (node: Node) -> VisitResult<Node>

        external interface NodeVisitor   {
            fun <T : Node > invoke(nodes: T, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally, lift: (node: Array<out Node>) -> T = definedExternally): T
fun <T : Node > invoke(nodes: T?, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally, lift: (node: Array<out Node>) -> T = definedExternally): T?
        }
    

        external interface NodesVisitor   {
            fun <T : Node > invoke(nodes: NodeArray<T>, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally, start: Double = definedExternally, count: Double = definedExternally): NodeArray<T>
fun <T : Node > invoke(nodes: NodeArray<T>?, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally, start: Double = definedExternally, count: Double = definedExternally): NodeArray<T>?
        }
    
typealias VisitResult<T : Node > = /* T | T[] | undefined */

        external interface Printer   {
            fun  printNode(hint: EmitHint, node: Node, sourceFile: SourceFile): String
fun <T : Node > printList(format: ListFormat, list: NodeArray<T>, sourceFile: SourceFile): String
fun  printFile(sourceFile: SourceFile): String
fun  printBundle(bundle: Bundle): String
        }
    

        external interface PrintHandlers   {
            fun  hasGlobalName(name: String): Boolean
fun  onEmitNode(hint: EmitHint, node: Node, emitCallback: (hint: EmitHint, node: Node) -> Unit): Unit
fun  isEmitNotificationEnabled(node: Node): Boolean
fun  substituteNode(hint: EmitHint, node: Node): Node
        }
    

        external interface PrinterOptions   {
            var removeComments: Boolean?
var newLine: NewLineKind?
var omitTrailingSemicolon: Boolean?
var noEmitHelpers: Boolean?
        }
    

        external interface GetEffectiveTypeRootsHost   {
            fun  directoryExists(directoryName: String): Boolean
fun  getCurrentDirectory(): String
        }
    

        external interface TextSpan   {
            var start: Double
var length: Double
        }
    

        external interface TextChangeRange   {
            var span: TextSpan
var newLength: Double
        }
    

        external interface SyntaxList   : Node {
            var kind: SyntaxKind.SyntaxList
var _children: Array<Node>
        }
    

        external enum class ListFormat {
            None,
SingleLine,
MultiLine,
PreserveLines,
LinesMask,
NotDelimited,
BarDelimited,
AmpersandDelimited,
CommaDelimited,
AsteriskDelimited,
DelimitersMask,
AllowTrailingComma,
Indented,
SpaceBetweenBraces,
SpaceBetweenSiblings,
Braces,
Parenthesis,
AngleBrackets,
SquareBrackets,
BracketsMask,
OptionalIfUndefined,
OptionalIfEmpty,
Optional,
PreferNewLine,
NoTrailingNewLine,
NoInterveningComments,
NoSpaceIfEmpty,
SingleElement,
SpaceAfterList,
Modifiers,
HeritageClauses,
SingleLineTypeLiteralMembers,
MultiLineTypeLiteralMembers,
SingleLineTupleTypeElements,
MultiLineTupleTypeElements,
UnionTypeConstituents,
IntersectionTypeConstituents,
ObjectBindingPatternElements,
ArrayBindingPatternElements,
ObjectLiteralExpressionProperties,
ArrayLiteralExpressionElements,
CommaListElements,
CallExpressionArguments,
NewExpressionArguments,
TemplateExpressionSpans,
SingleLineBlockStatements,
MultiLineBlockStatements,
VariableDeclarationList,
SingleLineFunctionBodyStatements,
MultiLineFunctionBodyStatements,
ClassHeritageClauses,
ClassMembers,
InterfaceMembers,
EnumMembers,
CaseBlockClauses,
NamedImportsOrExportsElements,
JsxElementOrFragmentChildren,
JsxElementAttributes,
CaseOrDefaultClauseStatements,
HeritageClauseTypes,
SourceFileStatements,
Decorators,
TypeArguments,
TypeParameters,
Parameters,
IndexSignatureParameters,
JSDocComment
        }
    

        external interface UserPreferences   {
            val disableSuggestions: Boolean?
val quotePreference: /* "auto" | "double" | "single" */?
val includeCompletionsForModuleExports: Boolean?
val includeCompletionsForImportStatements: Boolean?
val includeCompletionsWithSnippetText: Boolean?
val includeAutomaticOptionalChainCompletions: Boolean?
val includeCompletionsWithInsertText: Boolean?
val allowIncompleteCompletions: Boolean?
val importModuleSpecifierPreference: /* "shortest" | "project-relative" | "relative" | "non-relative" */?
val importModuleSpecifierEnding: /* "auto" | "minimal" | "index" | "js" */?
val allowTextChangesInNewFiles: Boolean?
val providePrefixAndSuffixTextForRename: Boolean?
val includePackageJsonAutoImports: /* "auto" | "on" | "off" */?
val provideRefactorNotApplicableReason: Boolean?
        }
    

        external interface PseudoBigInt   {
            var negative: Boolean
var base10Value: String
        }
    

external fun  setTimeout(handler: (vararg args: Any?) -> Unit, timeout: Double): Any?
external fun  clearTimeout(handle: Any?): Unit

        external enum class FileWatcherEventKind {
            Created,
Changed,
Deleted
        }
    
typealias FileWatcherCallback = (fileName: String, eventKind: FileWatcherEventKind) -> Unit
typealias DirectoryWatcherCallback = (fileName: String) -> Unit

        external interface System   {
            var args: Array<String>
var newLine: String
var useCaseSensitiveFileNames: Boolean
fun  write(s: String): Unit
fun  writeOutputIsTTY(): Boolean
fun  getWidthOfTerminal(): Double
fun  readFile(path: String, encoding: String = definedExternally): String?
fun  getFileSize(path: String): Double
fun  writeFile(path: String, data: String, writeByteOrderMark: Boolean = definedExternally): Unit
fun  watchFile(path: String, callback: FileWatcherCallback, pollingInterval: Double = definedExternally, options: WatchOptions = definedExternally): FileWatcher
fun  watchDirectory(path: String, callback: DirectoryWatcherCallback, recursive: Boolean = definedExternally, options: WatchOptions = definedExternally): FileWatcher
fun  resolvePath(path: String): String
fun  fileExists(path: String): Boolean
fun  directoryExists(path: String): Boolean
fun  createDirectory(path: String): Unit
fun  getExecutingFilePath(): String
fun  getCurrentDirectory(): String
fun  getDirectories(path: String): Array<String>
fun  readDirectory(path: String, extensions: Array<out String> = definedExternally, exclude: Array<out String> = definedExternally, include: Array<out String> = definedExternally, depth: Double = definedExternally): Array<String>
fun  getModifiedTime(path: String): Date?
fun  setModifiedTime(path: String, time: Date): Unit
fun  deleteFile(path: String): Unit
fun  createHash(data: String): String
fun  createSHA256Hash(data: String): String
fun  getMemoryUsage(): Double
fun  exit(exitCode: Double = definedExternally): Unit
fun  realpath(path: String): String
fun  setTimeout(callback: (vararg args: Any?) -> Unit, ms: Double, vararg args: Any?): Any?
fun  clearTimeout(timeoutId: Any?): Unit
fun  clearScreen(): Unit
fun  base64decode(input: String): String
fun  base64encode(input: String): String
        }
    

        external interface FileWatcher   {
            fun  close(): Unit
        }
    
external fun  getNodeMajorVersion(): Double?
external var sys: System

typealias ErrorCallback = (message: DiagnosticMessage, length: Double) -> Unit

        external interface Scanner   {
            fun  getStartPos(): Double
fun  getToken(): SyntaxKind
fun  getTextPos(): Double
fun  getTokenPos(): Double
fun  getTokenText(): String
fun  getTokenValue(): String
fun  hasUnicodeEscape(): Boolean
fun  hasExtendedUnicodeEscape(): Boolean
fun  hasPrecedingLineBreak(): Boolean
fun  isIdentifier(): Boolean
fun  isReservedWord(): Boolean
fun  isUnterminated(): Boolean
fun  reScanGreaterToken(): SyntaxKind
fun  reScanSlashToken(): SyntaxKind
fun  reScanAsteriskEqualsToken(): SyntaxKind
fun  reScanTemplateToken(isTaggedTemplate: Boolean): SyntaxKind
fun  reScanTemplateHeadOrNoSubstitutionTemplate(): SyntaxKind
fun  scanJsxIdentifier(): SyntaxKind
fun  scanJsxAttributeValue(): SyntaxKind
fun  reScanJsxAttributeValue(): SyntaxKind
fun  reScanJsxToken(allowMultilineJsxText: Boolean = definedExternally): JsxTokenSyntaxKind
fun  reScanLessThanToken(): SyntaxKind
fun  reScanHashToken(): SyntaxKind
fun  reScanQuestionToken(): SyntaxKind
fun  reScanInvalidIdentifier(): SyntaxKind
fun  scanJsxToken(): JsxTokenSyntaxKind
fun  scanJsDocToken(): JSDocSyntaxKind
fun  scan(): SyntaxKind
fun  getText(): String
fun  setText(text: String?, start: Double = definedExternally, length: Double = definedExternally): Unit
fun  setOnError(onError: ErrorCallback?): Unit
fun  setScriptTarget(scriptTarget: ScriptTarget): Unit
fun  setLanguageVariant(variant: LanguageVariant): Unit
fun  setTextPos(textPos: Double): Unit
fun <T  > lookAhead(callback: () -> T): T
fun <T  > scanRange(start: Double, length: Double, callback: () -> T): T
fun <T  > tryScan(callback: () -> T): T
        }
    
external fun  tokenToString(t: SyntaxKind): String?
external fun  getPositionOfLineAndCharacter(sourceFile: SourceFileLike, line: Double, character: Double): Double
external fun  getLineAndCharacterOfPosition(sourceFile: SourceFileLike, position: Double): LineAndCharacter
external fun  isWhiteSpaceLike(ch: Double): Boolean
external fun  isWhiteSpaceSingleLine(ch: Double): Boolean
external fun  isLineBreak(ch: Double): Boolean
external fun  couldStartTrivia(text: String, pos: Double): Boolean
external fun <U  > forEachLeadingCommentRange(text: String, pos: Double, cb: (pos: Double, end: Double, kind: CommentKind, hasTrailingNewLine: Boolean) -> U): U?
external fun <T  , U  > forEachLeadingCommentRange(text: String, pos: Double, cb: (pos: Double, end: Double, kind: CommentKind, hasTrailingNewLine: Boolean, state: T) -> U, state: T): U?
external fun <U  > forEachTrailingCommentRange(text: String, pos: Double, cb: (pos: Double, end: Double, kind: CommentKind, hasTrailingNewLine: Boolean) -> U): U?
external fun <T  , U  > forEachTrailingCommentRange(text: String, pos: Double, cb: (pos: Double, end: Double, kind: CommentKind, hasTrailingNewLine: Boolean, state: T) -> U, state: T): U?
external fun <T  , U  > reduceEachLeadingCommentRange(text: String, pos: Double, cb: (pos: Double, end: Double, kind: CommentKind, hasTrailingNewLine: Boolean, state: T, memo: U) -> U, state: T, initial: U): U?
external fun <T  , U  > reduceEachTrailingCommentRange(text: String, pos: Double, cb: (pos: Double, end: Double, kind: CommentKind, hasTrailingNewLine: Boolean, state: T, memo: U) -> U, state: T, initial: U): U?
external fun  getLeadingCommentRanges(text: String, pos: Double): Array<CommentRange>?
external fun  getTrailingCommentRanges(text: String, pos: Double): Array<CommentRange>?
external fun  getShebang(text: String): String?
external fun  isIdentifierStart(ch: Double, languageVersion: ScriptTarget?): Boolean
external fun  isIdentifierPart(ch: Double, languageVersion: ScriptTarget?, identifierVariant: LanguageVariant = definedExternally): Boolean
external fun  createScanner(languageVersion: ScriptTarget, skipTrivia: Boolean, languageVariant: LanguageVariant = definedExternally, textInitial: String = definedExternally, onError: ErrorCallback = definedExternally, start: Double = definedExternally, length: Double = definedExternally): Scanner
external fun  isExternalModuleNameRelative(moduleName: String): Boolean
external fun <T : Diagnostic > sortAndDeduplicateDiagnostics(diagnostics: Array<out T>): SortedReadonlyArray<T>
external fun  getDefaultLibFileName(options: CompilerOptions): String
external fun  textSpanEnd(span: TextSpan): Double
external fun  textSpanIsEmpty(span: TextSpan): Boolean
external fun  textSpanContainsPosition(span: TextSpan, position: Double): Boolean
external fun  textSpanContainsTextSpan(span: TextSpan, other: TextSpan): Boolean
external fun  textSpanOverlapsWith(span: TextSpan, other: TextSpan): Boolean
external fun  textSpanOverlap(span1: TextSpan, span2: TextSpan): TextSpan?
external fun  textSpanIntersectsWithTextSpan(span: TextSpan, other: TextSpan): Boolean
external fun  textSpanIntersectsWith(span: TextSpan, start: Double, length: Double): Boolean
external fun  decodedTextSpanIntersectsWith(start1: Double, length1: Double, start2: Double, length2: Double): Boolean
external fun  textSpanIntersectsWithPosition(span: TextSpan, position: Double): Boolean
external fun  textSpanIntersection(span1: TextSpan, span2: TextSpan): TextSpan?
external fun  createTextSpan(start: Double, length: Double): TextSpan
external fun  createTextSpanFromBounds(start: Double, end: Double): TextSpan
external fun  textChangeRangeNewSpan(range: TextChangeRange): TextSpan
external fun  textChangeRangeIsUnchanged(range: TextChangeRange): Boolean
external fun  createTextChangeRange(span: TextSpan, newLength: Double): TextChangeRange
external var unchangedTextChangeRange: TextChangeRange
external fun  collapseTextChangeRangesAcrossMultipleVersions(changes: Array<out TextChangeRange>): TextChangeRange
external fun  getTypeParameterOwner(d: Declaration): Declaration?
typealias ParameterPropertyDeclaration = /* ParameterDeclaration & {
        parent: ConstructorDeclaration;
        name: Identifier;
    } */
external fun  isParameterPropertyDeclaration(node: Node, parent: Node): Boolean
external fun  isEmptyBindingPattern(node: BindingName): Boolean
external fun  isEmptyBindingElement(node: BindingElement): Boolean
external fun  walkUpBindingElementsAndPatterns(binding: BindingElement): /* VariableDeclaration | ParameterDeclaration */
external fun  getCombinedModifierFlags(node: Declaration): ModifierFlags
external fun  getCombinedNodeFlags(node: Node): NodeFlags
external fun  validateLocaleAndSetLanguage(locale: String, sys: /* {
        getExecutingFilePath(): string;
        resolvePath(path: string): string;
        fileExists(fileName: string): boolean;
        readFile(fileName: string): string | undefined;
    } */, errors: Push<Diagnostic> = definedExternally): Unit
external fun  getOriginalNode(node: Node): Node
external fun <T : Node > getOriginalNode(node: Node, nodeTest: (node: Node) -> Boolean): T
external fun  getOriginalNode(node: Node?): Node?
external fun <T : Node > getOriginalNode(node: Node?, nodeTest: (node: Node?) -> Boolean): T?
external fun <T : Node > findAncestor(node: Node?, callback: (element: Node) -> Boolean): T?
external fun  findAncestor(node: Node?, callback: (element: Node) -> /* boolean | "quit" */): Node?
external fun  isParseTreeNode(node: Node): Boolean
external fun  getParseTreeNode(node: Node?): Node?
external fun <T : Node > getParseTreeNode(node: T?, nodeTest: (node: Node) -> Boolean = definedExternally): T?
external fun  escapeLeadingUnderscores(identifier: String): __String
external fun  unescapeLeadingUnderscores(identifier: __String): String
external fun  idText(identifierOrPrivateName: /* Identifier | PrivateIdentifier */): String
external fun  symbolName(symbol: Symbol): String
external fun  getNameOfJSDocTypedef(declaration: JSDocTypedefTag): /* Identifier | PrivateIdentifier | undefined */
external fun  getNameOfDeclaration(declaration: /* Declaration | Expression | undefined */): DeclarationName?
external fun  getJSDocParameterTags(param: ParameterDeclaration): Array<out JSDocParameterTag>
external fun  getJSDocTypeParameterTags(param: TypeParameterDeclaration): Array<out JSDocTemplateTag>
external fun  hasJSDocParameterTags(node: /* FunctionLikeDeclaration | SignatureDeclaration */): Boolean
external fun  getJSDocAugmentsTag(node: Node): JSDocAugmentsTag?
external fun  getJSDocImplementsTags(node: Node): Array<out JSDocImplementsTag>
external fun  getJSDocClassTag(node: Node): JSDocClassTag?
external fun  getJSDocPublicTag(node: Node): JSDocPublicTag?
external fun  getJSDocPrivateTag(node: Node): JSDocPrivateTag?
external fun  getJSDocProtectedTag(node: Node): JSDocProtectedTag?
external fun  getJSDocReadonlyTag(node: Node): JSDocReadonlyTag?
external fun  getJSDocOverrideTagNoCache(node: Node): JSDocOverrideTag?
external fun  getJSDocDeprecatedTag(node: Node): JSDocDeprecatedTag?
external fun  getJSDocEnumTag(node: Node): JSDocEnumTag?
external fun  getJSDocThisTag(node: Node): JSDocThisTag?
external fun  getJSDocReturnTag(node: Node): JSDocReturnTag?
external fun  getJSDocTemplateTag(node: Node): JSDocTemplateTag?
external fun  getJSDocTypeTag(node: Node): JSDocTypeTag?
external fun  getJSDocType(node: Node): TypeNode?
external fun  getJSDocReturnType(node: Node): TypeNode?
external fun  getJSDocTags(node: Node): Array<out JSDocTag>
external fun <T : JSDocTag > getAllJSDocTags(node: Node, predicate: (tag: JSDocTag) -> Boolean): Array<out T>
external fun  getAllJSDocTagsOfKind(node: Node, kind: SyntaxKind): Array<out JSDocTag>
external fun  getTextOfJSDocComment(comment: /* string | NodeArray<JSDocComment> */ = definedExternally): String?
external fun  getEffectiveTypeParameterDeclarations(node: DeclarationWithTypeParameters): Array<out TypeParameterDeclaration>
external fun  getEffectiveConstraintOfTypeParameter(node: TypeParameterDeclaration): TypeNode?
external fun  isMemberName(node: Node): Boolean
external fun  isPropertyAccessChain(node: Node): Boolean
external fun  isElementAccessChain(node: Node): Boolean
external fun  isCallChain(node: Node): Boolean
external fun  isOptionalChain(node: Node): Boolean
external fun  isNullishCoalesce(node: Node): Boolean
external fun  isConstTypeReference(node: Node): Boolean
external fun  skipPartiallyEmittedExpressions(node: Expression): Expression
external fun  skipPartiallyEmittedExpressions(node: Node): Node
external fun  isNonNullChain(node: Node): Boolean
external fun  isBreakOrContinueStatement(node: Node): Boolean
external fun  isNamedExportBindings(node: Node): Boolean
external fun  isUnparsedTextLike(node: Node): Boolean
external fun  isUnparsedNode(node: Node): Boolean
external fun  isJSDocPropertyLikeTag(node: Node): Boolean
external fun  isTokenKind(kind: SyntaxKind): Boolean
external fun  isToken(n: Node): Boolean
external fun  isLiteralExpression(node: Node): Boolean
external fun  isTemplateLiteralToken(node: Node): Boolean
external fun  isTemplateMiddleOrTemplateTail(node: Node): Boolean
external fun  isImportOrExportSpecifier(node: Node): Boolean
external fun  isTypeOnlyImportOrExportDeclaration(node: Node): Boolean
external fun  isStringTextContainingNode(node: Node): Boolean
external fun  isModifier(node: Node): Boolean
external fun  isEntityName(node: Node): Boolean
external fun  isPropertyName(node: Node): Boolean
external fun  isBindingName(node: Node): Boolean
external fun  isFunctionLike(node: Node?): Boolean
external fun  isClassElement(node: Node): Boolean
external fun  isClassLike(node: Node): Boolean
external fun  isAccessor(node: Node): Boolean
external fun  isTypeElement(node: Node): Boolean
external fun  isClassOrTypeElement(node: Node): Boolean
external fun  isObjectLiteralElementLike(node: Node): Boolean
external fun  isTypeNode(node: Node): Boolean
external fun  isFunctionOrConstructorTypeNode(node: Node): Boolean
external fun  isPropertyAccessOrQualifiedName(node: Node): Boolean
external fun  isCallLikeExpression(node: Node): Boolean
external fun  isCallOrNewExpression(node: Node): Boolean
external fun  isTemplateLiteral(node: Node): Boolean
external fun  isAssertionExpression(node: Node): Boolean
external fun  isIterationStatement(node: Node, lookInLabeledStatements: Boolean): Boolean
external fun  isIterationStatement(node: Node, lookInLabeledStatements: Boolean): Boolean
external fun  isJsxOpeningLikeElement(node: Node): Boolean
external fun  isCaseOrDefaultClause(node: Node): Boolean
external fun  isJSDocCommentContainingNode(node: Node): Boolean
external fun  isSetAccessor(node: Node): Boolean
external fun  isGetAccessor(node: Node): Boolean
external fun  hasOnlyExpressionInitializer(node: Node): Boolean
external fun  isObjectLiteralElement(node: Node): Boolean
external fun  isStringLiteralLike(node: Node): Boolean
external fun  isJSDocLinkLike(node: Node): Boolean
external val factory: NodeFactory
external fun  createUnparsedSourceFile(text: String): UnparsedSource
external fun  createUnparsedSourceFile(inputFile: InputFiles, type: /* "js" | "dts" */, stripInternal: Boolean = definedExternally): UnparsedSource
external fun  createUnparsedSourceFile(text: String, mapPath: String?, map: String?): UnparsedSource
external fun  createInputFiles(javascriptText: String, declarationText: String): InputFiles
external fun  createInputFiles(readFileText: (path: String) -> String?, javascriptPath: String, javascriptMapPath: String?, declarationPath: String, declarationMapPath: String?, buildInfoPath: String?): InputFiles
external fun  createInputFiles(javascriptText: String, declarationText: String, javascriptMapPath: String?, javascriptMapText: String?, declarationMapPath: String?, declarationMapText: String?): InputFiles
external fun  createSourceMapSource(fileName: String, text: String, skipTrivia: (pos: Double) -> Double = definedExternally): SourceMapSource
external fun <T : Node > setOriginalNode(node: T, original: Node?): T
external fun  disposeEmitNodes(sourceFile: SourceFile?): Unit
external fun <T : Node > setEmitFlags(node: T, emitFlags: EmitFlags): T
external fun  getSourceMapRange(node: Node): SourceMapRange
external fun <T : Node > setSourceMapRange(node: T, range: SourceMapRange?): T
external fun  getTokenSourceMapRange(node: Node, token: SyntaxKind): SourceMapRange?
external fun <T : Node > setTokenSourceMapRange(node: T, token: SyntaxKind, range: SourceMapRange?): T
external fun  getCommentRange(node: Node): TextRange
external fun <T : Node > setCommentRange(node: T, range: TextRange): T
external fun  getSyntheticLeadingComments(node: Node): Array<SynthesizedComment>?
external fun <T : Node > setSyntheticLeadingComments(node: T, comments: Array<SynthesizedComment>?): T
external fun <T : Node > addSyntheticLeadingComment(node: T, kind: /* SyntaxKind.SingleLineCommentTrivia | SyntaxKind.MultiLineCommentTrivia */, text: String, hasTrailingNewLine: Boolean = definedExternally): T
external fun  getSyntheticTrailingComments(node: Node): Array<SynthesizedComment>?
external fun <T : Node > setSyntheticTrailingComments(node: T, comments: Array<SynthesizedComment>?): T
external fun <T : Node > addSyntheticTrailingComment(node: T, kind: /* SyntaxKind.SingleLineCommentTrivia | SyntaxKind.MultiLineCommentTrivia */, text: String, hasTrailingNewLine: Boolean = definedExternally): T
external fun <T : Node > moveSyntheticComments(node: T, original: Node): T
external fun  getConstantValue(node: AccessExpression): /* string | number | undefined */
external fun  setConstantValue(node: AccessExpression, value: /* string | number */): AccessExpression
external fun <T : Node > addEmitHelper(node: T, helper: EmitHelper): T
external fun <T : Node > addEmitHelpers(node: T, helpers: Array<EmitHelper>?): T
external fun  removeEmitHelper(node: Node, helper: EmitHelper): Boolean
external fun  getEmitHelpers(node: Node): Array<EmitHelper>?
external fun  moveEmitHelpers(source: Node, target: Node, predicate: (helper: EmitHelper) -> Boolean): Unit
external fun  isNumericLiteral(node: Node): Boolean
external fun  isBigIntLiteral(node: Node): Boolean
external fun  isStringLiteral(node: Node): Boolean
external fun  isJsxText(node: Node): Boolean
external fun  isRegularExpressionLiteral(node: Node): Boolean
external fun  isNoSubstitutionTemplateLiteral(node: Node): Boolean
external fun  isTemplateHead(node: Node): Boolean
external fun  isTemplateMiddle(node: Node): Boolean
external fun  isTemplateTail(node: Node): Boolean
external fun  isDotDotDotToken(node: Node): Boolean
external fun  isPlusToken(node: Node): Boolean
external fun  isMinusToken(node: Node): Boolean
external fun  isAsteriskToken(node: Node): Boolean
external fun  isIdentifier(node: Node): Boolean
external fun  isPrivateIdentifier(node: Node): Boolean
external fun  isQualifiedName(node: Node): Boolean
external fun  isComputedPropertyName(node: Node): Boolean
external fun  isTypeParameterDeclaration(node: Node): Boolean
external fun  isParameter(node: Node): Boolean
external fun  isDecorator(node: Node): Boolean
external fun  isPropertySignature(node: Node): Boolean
external fun  isPropertyDeclaration(node: Node): Boolean
external fun  isMethodSignature(node: Node): Boolean
external fun  isMethodDeclaration(node: Node): Boolean
external fun  isClassStaticBlockDeclaration(node: Node): Boolean
external fun  isConstructorDeclaration(node: Node): Boolean
external fun  isGetAccessorDeclaration(node: Node): Boolean
external fun  isSetAccessorDeclaration(node: Node): Boolean
external fun  isCallSignatureDeclaration(node: Node): Boolean
external fun  isConstructSignatureDeclaration(node: Node): Boolean
external fun  isIndexSignatureDeclaration(node: Node): Boolean
external fun  isTypePredicateNode(node: Node): Boolean
external fun  isTypeReferenceNode(node: Node): Boolean
external fun  isFunctionTypeNode(node: Node): Boolean
external fun  isConstructorTypeNode(node: Node): Boolean
external fun  isTypeQueryNode(node: Node): Boolean
external fun  isTypeLiteralNode(node: Node): Boolean
external fun  isArrayTypeNode(node: Node): Boolean
external fun  isTupleTypeNode(node: Node): Boolean
external fun  isNamedTupleMember(node: Node): Boolean
external fun  isOptionalTypeNode(node: Node): Boolean
external fun  isRestTypeNode(node: Node): Boolean
external fun  isUnionTypeNode(node: Node): Boolean
external fun  isIntersectionTypeNode(node: Node): Boolean
external fun  isConditionalTypeNode(node: Node): Boolean
external fun  isInferTypeNode(node: Node): Boolean
external fun  isParenthesizedTypeNode(node: Node): Boolean
external fun  isThisTypeNode(node: Node): Boolean
external fun  isTypeOperatorNode(node: Node): Boolean
external fun  isIndexedAccessTypeNode(node: Node): Boolean
external fun  isMappedTypeNode(node: Node): Boolean
external fun  isLiteralTypeNode(node: Node): Boolean
external fun  isImportTypeNode(node: Node): Boolean
external fun  isTemplateLiteralTypeSpan(node: Node): Boolean
external fun  isTemplateLiteralTypeNode(node: Node): Boolean
external fun  isObjectBindingPattern(node: Node): Boolean
external fun  isArrayBindingPattern(node: Node): Boolean
external fun  isBindingElement(node: Node): Boolean
external fun  isArrayLiteralExpression(node: Node): Boolean
external fun  isObjectLiteralExpression(node: Node): Boolean
external fun  isPropertyAccessExpression(node: Node): Boolean
external fun  isElementAccessExpression(node: Node): Boolean
external fun  isCallExpression(node: Node): Boolean
external fun  isNewExpression(node: Node): Boolean
external fun  isTaggedTemplateExpression(node: Node): Boolean
external fun  isTypeAssertionExpression(node: Node): Boolean
external fun  isParenthesizedExpression(node: Node): Boolean
external fun  isFunctionExpression(node: Node): Boolean
external fun  isArrowFunction(node: Node): Boolean
external fun  isDeleteExpression(node: Node): Boolean
external fun  isTypeOfExpression(node: Node): Boolean
external fun  isVoidExpression(node: Node): Boolean
external fun  isAwaitExpression(node: Node): Boolean
external fun  isPrefixUnaryExpression(node: Node): Boolean
external fun  isPostfixUnaryExpression(node: Node): Boolean
external fun  isBinaryExpression(node: Node): Boolean
external fun  isConditionalExpression(node: Node): Boolean
external fun  isTemplateExpression(node: Node): Boolean
external fun  isYieldExpression(node: Node): Boolean
external fun  isSpreadElement(node: Node): Boolean
external fun  isClassExpression(node: Node): Boolean
external fun  isOmittedExpression(node: Node): Boolean
external fun  isExpressionWithTypeArguments(node: Node): Boolean
external fun  isAsExpression(node: Node): Boolean
external fun  isNonNullExpression(node: Node): Boolean
external fun  isMetaProperty(node: Node): Boolean
external fun  isSyntheticExpression(node: Node): Boolean
external fun  isPartiallyEmittedExpression(node: Node): Boolean
external fun  isCommaListExpression(node: Node): Boolean
external fun  isTemplateSpan(node: Node): Boolean
external fun  isSemicolonClassElement(node: Node): Boolean
external fun  isBlock(node: Node): Boolean
external fun  isVariableStatement(node: Node): Boolean
external fun  isEmptyStatement(node: Node): Boolean
external fun  isExpressionStatement(node: Node): Boolean
external fun  isIfStatement(node: Node): Boolean
external fun  isDoStatement(node: Node): Boolean
external fun  isWhileStatement(node: Node): Boolean
external fun  isForStatement(node: Node): Boolean
external fun  isForInStatement(node: Node): Boolean
external fun  isForOfStatement(node: Node): Boolean
external fun  isContinueStatement(node: Node): Boolean
external fun  isBreakStatement(node: Node): Boolean
external fun  isReturnStatement(node: Node): Boolean
external fun  isWithStatement(node: Node): Boolean
external fun  isSwitchStatement(node: Node): Boolean
external fun  isLabeledStatement(node: Node): Boolean
external fun  isThrowStatement(node: Node): Boolean
external fun  isTryStatement(node: Node): Boolean
external fun  isDebuggerStatement(node: Node): Boolean
external fun  isVariableDeclaration(node: Node): Boolean
external fun  isVariableDeclarationList(node: Node): Boolean
external fun  isFunctionDeclaration(node: Node): Boolean
external fun  isClassDeclaration(node: Node): Boolean
external fun  isInterfaceDeclaration(node: Node): Boolean
external fun  isTypeAliasDeclaration(node: Node): Boolean
external fun  isEnumDeclaration(node: Node): Boolean
external fun  isModuleDeclaration(node: Node): Boolean
external fun  isModuleBlock(node: Node): Boolean
external fun  isCaseBlock(node: Node): Boolean
external fun  isNamespaceExportDeclaration(node: Node): Boolean
external fun  isImportEqualsDeclaration(node: Node): Boolean
external fun  isImportDeclaration(node: Node): Boolean
external fun  isImportClause(node: Node): Boolean
external fun  isNamespaceImport(node: Node): Boolean
external fun  isNamespaceExport(node: Node): Boolean
external fun  isNamedImports(node: Node): Boolean
external fun  isImportSpecifier(node: Node): Boolean
external fun  isExportAssignment(node: Node): Boolean
external fun  isExportDeclaration(node: Node): Boolean
external fun  isNamedExports(node: Node): Boolean
external fun  isExportSpecifier(node: Node): Boolean
external fun  isMissingDeclaration(node: Node): Boolean
external fun  isNotEmittedStatement(node: Node): Boolean
external fun  isExternalModuleReference(node: Node): Boolean
external fun  isJsxElement(node: Node): Boolean
external fun  isJsxSelfClosingElement(node: Node): Boolean
external fun  isJsxOpeningElement(node: Node): Boolean
external fun  isJsxClosingElement(node: Node): Boolean
external fun  isJsxFragment(node: Node): Boolean
external fun  isJsxOpeningFragment(node: Node): Boolean
external fun  isJsxClosingFragment(node: Node): Boolean
external fun  isJsxAttribute(node: Node): Boolean
external fun  isJsxAttributes(node: Node): Boolean
external fun  isJsxSpreadAttribute(node: Node): Boolean
external fun  isJsxExpression(node: Node): Boolean
external fun  isCaseClause(node: Node): Boolean
external fun  isDefaultClause(node: Node): Boolean
external fun  isHeritageClause(node: Node): Boolean
external fun  isCatchClause(node: Node): Boolean
external fun  isPropertyAssignment(node: Node): Boolean
external fun  isShorthandPropertyAssignment(node: Node): Boolean
external fun  isSpreadAssignment(node: Node): Boolean
external fun  isEnumMember(node: Node): Boolean
external fun  isUnparsedPrepend(node: Node): Boolean
external fun  isSourceFile(node: Node): Boolean
external fun  isBundle(node: Node): Boolean
external fun  isUnparsedSource(node: Node): Boolean
external fun  isJSDocTypeExpression(node: Node): Boolean
external fun  isJSDocNameReference(node: Node): Boolean
external fun  isJSDocMemberName(node: Node): Boolean
external fun  isJSDocLink(node: Node): Boolean
external fun  isJSDocLinkCode(node: Node): Boolean
external fun  isJSDocLinkPlain(node: Node): Boolean
external fun  isJSDocAllType(node: Node): Boolean
external fun  isJSDocUnknownType(node: Node): Boolean
external fun  isJSDocNullableType(node: Node): Boolean
external fun  isJSDocNonNullableType(node: Node): Boolean
external fun  isJSDocOptionalType(node: Node): Boolean
external fun  isJSDocFunctionType(node: Node): Boolean
external fun  isJSDocVariadicType(node: Node): Boolean
external fun  isJSDocNamepathType(node: Node): Boolean
external fun  isJSDoc(node: Node): Boolean
external fun  isJSDocTypeLiteral(node: Node): Boolean
external fun  isJSDocSignature(node: Node): Boolean
external fun  isJSDocAugmentsTag(node: Node): Boolean
external fun  isJSDocAuthorTag(node: Node): Boolean
external fun  isJSDocClassTag(node: Node): Boolean
external fun  isJSDocCallbackTag(node: Node): Boolean
external fun  isJSDocPublicTag(node: Node): Boolean
external fun  isJSDocPrivateTag(node: Node): Boolean
external fun  isJSDocProtectedTag(node: Node): Boolean
external fun  isJSDocReadonlyTag(node: Node): Boolean
external fun  isJSDocOverrideTag(node: Node): Boolean
external fun  isJSDocDeprecatedTag(node: Node): Boolean
external fun  isJSDocSeeTag(node: Node): Boolean
external fun  isJSDocEnumTag(node: Node): Boolean
external fun  isJSDocParameterTag(node: Node): Boolean
external fun  isJSDocReturnTag(node: Node): Boolean
external fun  isJSDocThisTag(node: Node): Boolean
external fun  isJSDocTypeTag(node: Node): Boolean
external fun  isJSDocTemplateTag(node: Node): Boolean
external fun  isJSDocTypedefTag(node: Node): Boolean
external fun  isJSDocUnknownTag(node: Node): Boolean
external fun  isJSDocPropertyTag(node: Node): Boolean
external fun  isJSDocImplementsTag(node: Node): Boolean
external fun <T : TextRange > setTextRange(range: T, location: TextRange?): T
external fun <T  > forEachChild(node: Node, cbNode: (node: Node) -> T?, cbNodes: (nodes: NodeArray<Node>) -> T? = definedExternally): T?
external fun  createSourceFile(fileName: String, sourceText: String, languageVersion: ScriptTarget, setParentNodes: Boolean = definedExternally, scriptKind: ScriptKind = definedExternally): SourceFile
external fun  parseIsolatedEntityName(text: String, languageVersion: ScriptTarget): EntityName?
external fun  parseJsonText(fileName: String, sourceText: String): JsonSourceFile
external fun  isExternalModule(file: SourceFile): Boolean
external fun  updateSourceFile(sourceFile: SourceFile, newText: String, textChangeRange: TextChangeRange, aggressiveChecks: Boolean = definedExternally): SourceFile

external fun  parseCommandLine(commandLine: Array<out String>, readFile: (path: String) -> String? = definedExternally): ParsedCommandLine
typealias DiagnosticReporter = (diagnostic: Diagnostic) -> Unit

        external interface ConfigFileDiagnosticsReporter   {
            var onUnRecoverableConfigFileDiagnostic: DiagnosticReporter
        }
    

        external interface ParseConfigFileHost   : ParseConfigHost, ConfigFileDiagnosticsReporter {
            fun  getCurrentDirectory(): String
        }
    
external fun  getParsedCommandLineOfConfigFile(configFileName: String, optionsToExtend: CompilerOptions?, host: ParseConfigFileHost, extendedConfigCache: Map<ExtendedConfigCacheEntry> = definedExternally, watchOptionsToExtend: WatchOptions = definedExternally, extraFileExtensions: Array<out FileExtensionInfo> = definedExternally): ParsedCommandLine?
external fun  readConfigFile(fileName: String, readFile: (path: String) -> String?): /* {
        config?: any;
        error?: Diagnostic;
    } */
external fun  parseConfigFileTextToJson(fileName: String, jsonText: String): /* {
        config?: any;
        error?: Diagnostic;
    } */
external fun  readJsonConfigFile(fileName: String, readFile: (path: String) -> String?): TsConfigSourceFile
external fun  convertToObject(sourceFile: JsonSourceFile, errors: Push<Diagnostic>): Any?
external fun  parseJsonConfigFileContent(json: Any?, host: ParseConfigHost, basePath: String, existingOptions: CompilerOptions = definedExternally, configFileName: String = definedExternally, resolutionStack: Array<Path> = definedExternally, extraFileExtensions: Array<out FileExtensionInfo> = definedExternally, extendedConfigCache: Map<ExtendedConfigCacheEntry> = definedExternally, existingWatchOptions: WatchOptions = definedExternally): ParsedCommandLine
external fun  parseJsonSourceFileConfigFileContent(sourceFile: TsConfigSourceFile, host: ParseConfigHost, basePath: String, existingOptions: CompilerOptions = definedExternally, configFileName: String = definedExternally, resolutionStack: Array<Path> = definedExternally, extraFileExtensions: Array<out FileExtensionInfo> = definedExternally, extendedConfigCache: Map<ExtendedConfigCacheEntry> = definedExternally, existingWatchOptions: WatchOptions = definedExternally): ParsedCommandLine

        external interface ParsedTsconfig   {
            var raw: Any?
var options: CompilerOptions?
var watchOptions: WatchOptions?
var typeAcquisition: TypeAcquisition?
var extendedConfigPath: String?
        }
    

        external interface ExtendedConfigCacheEntry   {
            var extendedResult: TsConfigSourceFile
var extendedConfig: ParsedTsconfig?
        }
    
external fun  convertCompilerOptionsFromJson(jsonOptions: Any?, basePath: String, configFileName: String = definedExternally): /* {
        options: CompilerOptions;
        errors: Diagnostic[];
    } */
external fun  convertTypeAcquisitionFromJson(jsonOptions: Any?, basePath: String, configFileName: String = definedExternally): /* {
        options: TypeAcquisition;
        errors: Diagnostic[];
    } */

external fun  getEffectiveTypeRoots(options: CompilerOptions, host: GetEffectiveTypeRootsHost): Array<String>?
external fun  resolveTypeReferenceDirective(typeReferenceDirectiveName: String, containingFile: String?, options: CompilerOptions, host: ModuleResolutionHost, redirectedReference: ResolvedProjectReference = definedExternally, cache: TypeReferenceDirectiveResolutionCache = definedExternally): ResolvedTypeReferenceDirectiveWithFailedLookupLocations
external fun  getAutomaticTypeDirectiveNames(options: CompilerOptions, host: ModuleResolutionHost): Array<String>

        external interface TypeReferenceDirectiveResolutionCache   : PerDirectoryResolutionCache<ResolvedTypeReferenceDirectiveWithFailedLookupLocations>, PackageJsonInfoCache {
            
        }
    

        external interface PerDirectoryResolutionCache <T  >  {
            fun  getOrCreateCacheForDirectory(directoryName: String, redirectedReference: ResolvedProjectReference = definedExternally): Map<T>
fun  clear(): Unit
fun  update(options: CompilerOptions): Unit
        }
    

        external interface ModuleResolutionCache   : PerDirectoryResolutionCache<ResolvedModuleWithFailedLookupLocations>, NonRelativeModuleNameResolutionCache, PackageJsonInfoCache {
            fun  getPackageJsonInfoCache(): PackageJsonInfoCache
        }
    

        external interface NonRelativeModuleNameResolutionCache   : PackageJsonInfoCache {
            fun  getOrCreateCacheForModuleName(nonRelativeModuleName: String, redirectedReference: ResolvedProjectReference = definedExternally): PerModuleNameCache
        }
    

        external interface PackageJsonInfoCache   {
            fun  clear(): Unit
        }
    

        external interface PerModuleNameCache   {
            fun  get(directory: String): ResolvedModuleWithFailedLookupLocations?
fun  set(directory: String, result: ResolvedModuleWithFailedLookupLocations): Unit
        }
    
external fun  createModuleResolutionCache(currentDirectory: String, getCanonicalFileName: (s: String) -> String, options: CompilerOptions = definedExternally): ModuleResolutionCache
external fun  createTypeReferenceDirectiveResolutionCache(currentDirectory: String, getCanonicalFileName: (s: String) -> String, options: CompilerOptions = definedExternally, packageJsonInfoCache: PackageJsonInfoCache = definedExternally): TypeReferenceDirectiveResolutionCache
external fun  resolveModuleNameFromCache(moduleName: String, containingFile: String, cache: ModuleResolutionCache): ResolvedModuleWithFailedLookupLocations?
external fun  resolveModuleName(moduleName: String, containingFile: String, compilerOptions: CompilerOptions, host: ModuleResolutionHost, cache: ModuleResolutionCache = definedExternally, redirectedReference: ResolvedProjectReference = definedExternally): ResolvedModuleWithFailedLookupLocations
external fun  nodeModuleNameResolver(moduleName: String, containingFile: String, compilerOptions: CompilerOptions, host: ModuleResolutionHost, cache: ModuleResolutionCache = definedExternally, redirectedReference: ResolvedProjectReference = definedExternally): ResolvedModuleWithFailedLookupLocations
external fun  classicNameResolver(moduleName: String, containingFile: String, compilerOptions: CompilerOptions, host: ModuleResolutionHost, cache: NonRelativeModuleNameResolutionCache = definedExternally, redirectedReference: ResolvedProjectReference = definedExternally): ResolvedModuleWithFailedLookupLocations

external fun <T : Node > visitNode(node: T, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally, lift: (node: Array<out Node>) -> T = definedExternally): T
external fun <T : Node > visitNode(node: T?, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally, lift: (node: Array<out Node>) -> T = definedExternally): T?
external fun <T : Node > visitNodes(nodes: NodeArray<T>, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally, start: Double = definedExternally, count: Double = definedExternally): NodeArray<T>
external fun <T : Node > visitNodes(nodes: NodeArray<T>?, visitor: Visitor?, test: (node: Node) -> Boolean = definedExternally, start: Double = definedExternally, count: Double = definedExternally): NodeArray<T>?
external fun  visitLexicalEnvironment(statements: NodeArray<Statement>, visitor: Visitor, context: TransformationContext, start: Double = definedExternally, ensureUseStrict: Boolean = definedExternally, nodesVisitor: NodesVisitor = definedExternally): NodeArray<Statement>
external fun  visitParameterList(nodes: NodeArray<ParameterDeclaration>, visitor: Visitor, context: TransformationContext, nodesVisitor: NodesVisitor = definedExternally): NodeArray<ParameterDeclaration>
external fun  visitParameterList(nodes: NodeArray<ParameterDeclaration>?, visitor: Visitor, context: TransformationContext, nodesVisitor: NodesVisitor = definedExternally): NodeArray<ParameterDeclaration>?
external fun  visitFunctionBody(node: FunctionBody, visitor: Visitor, context: TransformationContext): FunctionBody
external fun  visitFunctionBody(node: FunctionBody?, visitor: Visitor, context: TransformationContext): FunctionBody?
external fun  visitFunctionBody(node: ConciseBody, visitor: Visitor, context: TransformationContext): ConciseBody
external fun  visitIterationBody(body: Statement, visitor: Visitor, context: TransformationContext): Statement
external fun <T : Node > visitEachChild(node: T, visitor: Visitor, context: TransformationContext): T
external fun <T : Node > visitEachChild(node: T?, visitor: Visitor, context: TransformationContext, nodesVisitor: /* typeof visitNodes */ = definedExternally, tokenVisitor: Visitor = definedExternally): T?
external fun  getTsBuildInfoEmitOutputFilePath(options: CompilerOptions): String?
external fun  getOutputFileNames(commandLine: ParsedCommandLine, inputFileName: String, ignoreCase: Boolean): Array<out String>
external fun  createPrinter(printerOptions: PrinterOptions = definedExternally, handlers: PrintHandlers = definedExternally): Printer
external fun  findConfigFile(searchPath: String, fileExists: (fileName: String) -> Boolean, configName: String = definedExternally): String?
external fun  resolveTripleslashReference(moduleName: String, containingFile: String): String
external fun  createCompilerHost(options: CompilerOptions, setParentNodes: Boolean = definedExternally): CompilerHost
external fun  getPreEmitDiagnostics(program: Program, sourceFile: SourceFile = definedExternally, cancellationToken: CancellationToken = definedExternally): Array<out Diagnostic>

        external interface FormatDiagnosticsHost   {
            fun  getCurrentDirectory(): String
fun  getCanonicalFileName(fileName: String): String
fun  getNewLine(): String
        }
    
external fun  formatDiagnostics(diagnostics: Array<out Diagnostic>, host: FormatDiagnosticsHost): String
external fun  formatDiagnostic(diagnostic: Diagnostic, host: FormatDiagnosticsHost): String
external fun  formatDiagnosticsWithColorAndContext(diagnostics: Array<out Diagnostic>, host: FormatDiagnosticsHost): String
external fun  flattenDiagnosticMessageText(diag: /* string | DiagnosticMessageChain | undefined */, newLine: String, indent: Double = definedExternally): String
external fun  getConfigFileParsingDiagnostics(configFileParseResult: ParsedCommandLine): Array<out Diagnostic>
external fun  createProgram(createProgramOptions: CreateProgramOptions): Program
external fun  createProgram(rootNames: Array<out String>, options: CompilerOptions, host: CompilerHost = definedExternally, oldProgram: Program = definedExternally, configFileParsingDiagnostics: Array<out Diagnostic> = definedExternally): Program

        external interface ResolveProjectReferencePathHost   {
            fun  fileExists(fileName: String): Boolean
        }
    
external fun  resolveProjectReferencePath(ref: ProjectReference): ResolvedConfigFileName
external fun  resolveProjectReferencePath(host: ResolveProjectReferencePathHost, ref: ProjectReference): ResolvedConfigFileName


        external interface EmitOutput   {
            var outputFiles: Array<OutputFile>
var emitSkipped: Boolean
        }
    

        external interface OutputFile   {
            var name: String
var writeByteOrderMark: Boolean
var text: String
        }
    
typealias AffectedFileResult<T  > = /* {
        result: T;
        affected: SourceFile | Program;
    } */?

        external interface BuilderProgramHost   {
            fun  useCaseSensitiveFileNames(): Boolean
var createHash: (data: String) -> String?
var writeFile: WriteFileCallback?
        }
    

        external interface BuilderProgram   {
            fun  getProgram(): Program
fun  getCompilerOptions(): CompilerOptions
fun  getSourceFile(fileName: String): SourceFile?
fun  getSourceFiles(): Array<out SourceFile>
fun  getOptionsDiagnostics(cancellationToken: CancellationToken = definedExternally): Array<out Diagnostic>
fun  getGlobalDiagnostics(cancellationToken: CancellationToken = definedExternally): Array<out Diagnostic>
fun  getConfigFileParsingDiagnostics(): Array<out Diagnostic>
fun  getSyntacticDiagnostics(sourceFile: SourceFile = definedExternally, cancellationToken: CancellationToken = definedExternally): Array<out Diagnostic>
fun  getDeclarationDiagnostics(sourceFile: SourceFile = definedExternally, cancellationToken: CancellationToken = definedExternally): Array<out DiagnosticWithLocation>
fun  getAllDependencies(sourceFile: SourceFile): Array<out String>
fun  getSemanticDiagnostics(sourceFile: SourceFile = definedExternally, cancellationToken: CancellationToken = definedExternally): Array<out Diagnostic>
fun  emit(targetSourceFile: SourceFile = definedExternally, writeFile: WriteFileCallback = definedExternally, cancellationToken: CancellationToken = definedExternally, emitOnlyDtsFiles: Boolean = definedExternally, customTransformers: CustomTransformers = definedExternally): EmitResult
fun  getCurrentDirectory(): String
        }
    

        external interface SemanticDiagnosticsBuilderProgram   : BuilderProgram {
            fun  getSemanticDiagnosticsOfNextAffectedFile(cancellationToken: CancellationToken = definedExternally, ignoreSourceFile: (sourceFile: SourceFile) -> Boolean = definedExternally): AffectedFileResult<Array<out Diagnostic>>
        }
    

        external interface EmitAndSemanticDiagnosticsBuilderProgram   : SemanticDiagnosticsBuilderProgram {
            fun  emitNextAffectedFile(writeFile: WriteFileCallback = definedExternally, cancellationToken: CancellationToken = definedExternally, emitOnlyDtsFiles: Boolean = definedExternally, customTransformers: CustomTransformers = definedExternally): AffectedFileResult<EmitResult>
        }
    
external fun  createSemanticDiagnosticsBuilderProgram(newProgram: Program, host: BuilderProgramHost, oldProgram: SemanticDiagnosticsBuilderProgram = definedExternally, configFileParsingDiagnostics: Array<out Diagnostic> = definedExternally): SemanticDiagnosticsBuilderProgram
external fun  createSemanticDiagnosticsBuilderProgram(rootNames: Array<out String>?, options: CompilerOptions?, host: CompilerHost = definedExternally, oldProgram: SemanticDiagnosticsBuilderProgram = definedExternally, configFileParsingDiagnostics: Array<out Diagnostic> = definedExternally, projectReferences: Array<out ProjectReference> = definedExternally): SemanticDiagnosticsBuilderProgram
external fun  createEmitAndSemanticDiagnosticsBuilderProgram(newProgram: Program, host: BuilderProgramHost, oldProgram: EmitAndSemanticDiagnosticsBuilderProgram = definedExternally, configFileParsingDiagnostics: Array<out Diagnostic> = definedExternally): EmitAndSemanticDiagnosticsBuilderProgram
external fun  createEmitAndSemanticDiagnosticsBuilderProgram(rootNames: Array<out String>?, options: CompilerOptions?, host: CompilerHost = definedExternally, oldProgram: EmitAndSemanticDiagnosticsBuilderProgram = definedExternally, configFileParsingDiagnostics: Array<out Diagnostic> = definedExternally, projectReferences: Array<out ProjectReference> = definedExternally): EmitAndSemanticDiagnosticsBuilderProgram
external fun  createAbstractBuilder(newProgram: Program, host: BuilderProgramHost, oldProgram: BuilderProgram = definedExternally, configFileParsingDiagnostics: Array<out Diagnostic> = definedExternally): BuilderProgram
external fun  createAbstractBuilder(rootNames: Array<out String>?, options: CompilerOptions?, host: CompilerHost = definedExternally, oldProgram: BuilderProgram = definedExternally, configFileParsingDiagnostics: Array<out Diagnostic> = definedExternally, projectReferences: Array<out ProjectReference> = definedExternally): BuilderProgram

        external interface ReadBuildProgramHost   {
            fun  useCaseSensitiveFileNames(): Boolean
fun  getCurrentDirectory(): String
fun  readFile(fileName: String): String?
        }
    
external fun  readBuilderProgram(compilerOptions: CompilerOptions, host: ReadBuildProgramHost): EmitAndSemanticDiagnosticsBuilderProgram?
external fun  createIncrementalCompilerHost(options: CompilerOptions, system: System = definedExternally): CompilerHost

        external interface IncrementalProgramOptions <T : BuilderProgram >  {
            var rootNames: Array<out String>
var options: CompilerOptions
var configFileParsingDiagnostics: Array<out Diagnostic>?
var projectReferences: Array<out ProjectReference>?
var host: CompilerHost?
var createProgram: CreateProgram<T>?
        }
    
external fun <T : BuilderProgram /* default is EmitAndSemanticDiagnosticsBuilderProgram */> createIncrementalProgram(/* { rootNames, options, configFileParsingDiagnostics, projectReferences, host, createProgram } */: IncrementalProgramOptions<T>): T
typealias WatchStatusReporter = (diagnostic: Diagnostic, newLine: String, options: CompilerOptions, errorCount: Double = definedExternally) -> Unit
typealias CreateProgram<T : BuilderProgram > = (rootNames: Array<out String>?, options: CompilerOptions?, host: CompilerHost = definedExternally, oldProgram: T = definedExternally, configFileParsingDiagnostics: Array<out Diagnostic> = definedExternally, projectReferences: Array<out ProjectReference>? = definedExternally) -> T

        external interface WatchHost   {
            fun  onWatchStatusChange(diagnostic: Diagnostic, newLine: String, options: CompilerOptions, errorCount: Double = definedExternally): Unit
fun  watchFile(path: String, callback: FileWatcherCallback, pollingInterval: Double = definedExternally, options: CompilerOptions = definedExternally): FileWatcher
fun  watchDirectory(path: String, callback: DirectoryWatcherCallback, recursive: Boolean = definedExternally, options: CompilerOptions = definedExternally): FileWatcher
fun  setTimeout(callback: (vararg args: Any?) -> Unit, ms: Double, vararg args: Any?): Any?
fun  clearTimeout(timeoutId: Any?): Unit
        }
    

        external interface ProgramHost <T : BuilderProgram >  {
            var createProgram: CreateProgram<T>
fun  useCaseSensitiveFileNames(): Boolean
fun  getNewLine(): String
fun  getCurrentDirectory(): String
fun  getDefaultLibFileName(options: CompilerOptions): String
fun  getDefaultLibLocation(): String
fun  createHash(data: String): String
fun  fileExists(path: String): Boolean
fun  readFile(path: String, encoding: String = definedExternally): String?
fun  directoryExists(path: String): Boolean
fun  getDirectories(path: String): Array<String>
fun  readDirectory(path: String, extensions: Array<out String> = definedExternally, exclude: Array<out String> = definedExternally, include: Array<out String> = definedExternally, depth: Double = definedExternally): Array<String>
fun  realpath(path: String): String
fun  trace(s: String): Unit
fun  getEnvironmentVariable(name: String): String?
fun  resolveModuleNames(moduleNames: Array<String>, containingFile: String, reusedNames: Array<String>?, redirectedReference: ResolvedProjectReference?, options: CompilerOptions): Array<(ResolvedModule?)>
fun  resolveTypeReferenceDirectives(typeReferenceDirectiveNames: Array<String>, containingFile: String, redirectedReference: ResolvedProjectReference?, options: CompilerOptions): Array<(ResolvedTypeReferenceDirective?)>
        }
    

        external interface WatchCompilerHost <T : BuilderProgram >  : ProgramHost<T>, WatchHost {
            fun  useSourceOfProjectReferenceRedirect(): Boolean
fun  getParsedCommandLine(fileName: String): ParsedCommandLine?
fun  afterProgramCreate(program: T): Unit
        }
    

        external interface WatchCompilerHostOfFilesAndCompilerOptions <T : BuilderProgram >  : WatchCompilerHost<T> {
            var rootFiles: Array<String>
var options: CompilerOptions
var watchOptions: WatchOptions?
var projectReferences: Array<out ProjectReference>?
        }
    

        external interface WatchCompilerHostOfConfigFile <T : BuilderProgram >  : WatchCompilerHost<T>, ConfigFileDiagnosticsReporter {
            var configFileName: String
var optionsToExtend: CompilerOptions?
var watchOptionsToExtend: WatchOptions?
var extraFileExtensions: Array<out FileExtensionInfo>?
fun  readDirectory(path: String, extensions: Array<out String> = definedExternally, exclude: Array<out String> = definedExternally, include: Array<out String> = definedExternally, depth: Double = definedExternally): Array<String>
        }
    

        external interface Watch <T  >  {
            fun  getProgram(): T
fun  close(): Unit
        }
    

        external interface WatchOfConfigFile <T  >  : Watch<T> {
            
        }
    

        external interface WatchOfFilesAndCompilerOptions <T  >  : Watch<T> {
            fun  updateRootFileNames(fileNames: Array<String>): Unit
        }
    
external fun <T : BuilderProgram > createWatchCompilerHost(configFileName: String, optionsToExtend: CompilerOptions?, system: System, createProgram: CreateProgram<T> = definedExternally, reportDiagnostic: DiagnosticReporter = definedExternally, reportWatchStatus: WatchStatusReporter = definedExternally, watchOptionsToExtend: WatchOptions = definedExternally, extraFileExtensions: Array<out FileExtensionInfo> = definedExternally): WatchCompilerHostOfConfigFile<T>
external fun <T : BuilderProgram > createWatchCompilerHost(rootFiles: Array<String>, options: CompilerOptions, system: System, createProgram: CreateProgram<T> = definedExternally, reportDiagnostic: DiagnosticReporter = definedExternally, reportWatchStatus: WatchStatusReporter = definedExternally, projectReferences: Array<out ProjectReference> = definedExternally, watchOptions: WatchOptions = definedExternally): WatchCompilerHostOfFilesAndCompilerOptions<T>
external fun <T : BuilderProgram > createWatchProgram(host: WatchCompilerHostOfFilesAndCompilerOptions<T>): WatchOfFilesAndCompilerOptions<T>
external fun <T : BuilderProgram > createWatchProgram(host: WatchCompilerHostOfConfigFile<T>): WatchOfConfigFile<T>

        external interface BuildOptions   {
            var dry: Boolean?
var force: Boolean?
var verbose: Boolean?
var incremental: Boolean?
var assumeChangesOnlyAffectDirectDependencies: Boolean?
var traceResolution: Boolean?
/* [option: string]: CompilerOptionsValue | undefined; */
        }
    
typealias ReportEmitErrorSummary = (errorCount: Double) -> Unit

        external interface SolutionBuilderHostBase <T : BuilderProgram >  : ProgramHost<T> {
            fun  createDirectory(path: String): Unit
fun  writeFile(path: String, data: String, writeByteOrderMark: Boolean = definedExternally): Unit
var getCustomTransformers: (project: String) -> CustomTransformers??
fun  getModifiedTime(fileName: String): Date?
fun  setModifiedTime(fileName: String, date: Date): Unit
fun  deleteFile(fileName: String): Unit
fun  getParsedCommandLine(fileName: String): ParsedCommandLine?
var reportDiagnostic: DiagnosticReporter
var reportSolutionBuilderStatus: DiagnosticReporter
fun  afterProgramEmitAndDiagnostics(program: T): Unit
        }
    

        external interface SolutionBuilderHost <T : BuilderProgram >  : SolutionBuilderHostBase<T> {
            var reportErrorSummary: ReportEmitErrorSummary?
        }
    

        external interface SolutionBuilderWithWatchHost <T : BuilderProgram >  : SolutionBuilderHostBase<T>, WatchHost {
            
        }
    

        external interface SolutionBuilder <T : BuilderProgram >  {
            fun  build(project: String = definedExternally, cancellationToken: CancellationToken = definedExternally, writeFile: WriteFileCallback = definedExternally, getCustomTransformers: (project: String) -> CustomTransformers = definedExternally): ExitStatus
fun  clean(project: String = definedExternally): ExitStatus
fun  buildReferences(project: String, cancellationToken: CancellationToken = definedExternally, writeFile: WriteFileCallback = definedExternally, getCustomTransformers: (project: String) -> CustomTransformers = definedExternally): ExitStatus
fun  cleanReferences(project: String = definedExternally): ExitStatus
fun  getNextInvalidatedProject(cancellationToken: CancellationToken = definedExternally): InvalidatedProject<T>?
        }
    
external fun  createBuilderStatusReporter(system: System, pretty: Boolean = definedExternally): DiagnosticReporter
external fun <T : BuilderProgram /* default is EmitAndSemanticDiagnosticsBuilderProgram */> createSolutionBuilderHost(system: System = definedExternally, createProgram: CreateProgram<T> = definedExternally, reportDiagnostic: DiagnosticReporter = definedExternally, reportSolutionBuilderStatus: DiagnosticReporter = definedExternally, reportErrorSummary: ReportEmitErrorSummary = definedExternally): SolutionBuilderHost<T>
external fun <T : BuilderProgram /* default is EmitAndSemanticDiagnosticsBuilderProgram */> createSolutionBuilderWithWatchHost(system: System = definedExternally, createProgram: CreateProgram<T> = definedExternally, reportDiagnostic: DiagnosticReporter = definedExternally, reportSolutionBuilderStatus: DiagnosticReporter = definedExternally, reportWatchStatus: WatchStatusReporter = definedExternally): SolutionBuilderWithWatchHost<T>
external fun <T : BuilderProgram > createSolutionBuilder(host: SolutionBuilderHost<T>, rootNames: Array<out String>, defaultOptions: BuildOptions): SolutionBuilder<T>
external fun <T : BuilderProgram > createSolutionBuilderWithWatch(host: SolutionBuilderWithWatchHost<T>, rootNames: Array<out String>, defaultOptions: BuildOptions, baseWatchOptions: WatchOptions = definedExternally): SolutionBuilder<T>

        external enum class InvalidatedProjectKind {
            Build,
UpdateBundle,
UpdateOutputFileStamps
        }
    

        external interface InvalidatedProjectBase   {
            val kind: InvalidatedProjectKind
val project: ResolvedConfigFileName
fun  done(cancellationToken: CancellationToken = definedExternally, writeFile: WriteFileCallback = definedExternally, customTransformers: CustomTransformers = definedExternally): ExitStatus
fun  getCompilerOptions(): CompilerOptions
fun  getCurrentDirectory(): String
        }
    

        external interface UpdateOutputFileStampsProject   : InvalidatedProjectBase {
            val kind: InvalidatedProjectKind.UpdateOutputFileStamps
fun  updateOutputFileStatmps(): Unit
        }
    

        external interface BuildInvalidedProject <T : BuilderProgram >  : InvalidatedProjectBase {
            val kind: InvalidatedProjectKind.Build
fun  getBuilderProgram(): T?
fun  getProgram(): Program?
fun  getSourceFile(fileName: String): SourceFile?
fun  getSourceFiles(): Array<out SourceFile>
fun  getOptionsDiagnostics(cancellationToken: CancellationToken = definedExternally): Array<out Diagnostic>
fun  getGlobalDiagnostics(cancellationToken: CancellationToken = definedExternally): Array<out Diagnostic>
fun  getConfigFileParsingDiagnostics(): Array<out Diagnostic>
fun  getSyntacticDiagnostics(sourceFile: SourceFile = definedExternally, cancellationToken: CancellationToken = definedExternally): Array<out Diagnostic>
fun  getAllDependencies(sourceFile: SourceFile): Array<out String>
fun  getSemanticDiagnostics(sourceFile: SourceFile = definedExternally, cancellationToken: CancellationToken = definedExternally): Array<out Diagnostic>
fun  getSemanticDiagnosticsOfNextAffectedFile(cancellationToken: CancellationToken = definedExternally, ignoreSourceFile: (sourceFile: SourceFile) -> Boolean = definedExternally): AffectedFileResult<Array<out Diagnostic>>
fun  emit(targetSourceFile: SourceFile = definedExternally, writeFile: WriteFileCallback = definedExternally, cancellationToken: CancellationToken = definedExternally, emitOnlyDtsFiles: Boolean = definedExternally, customTransformers: CustomTransformers = definedExternally): EmitResult?
        }
    

        external interface UpdateBundleProject <T : BuilderProgram >  : InvalidatedProjectBase {
            val kind: InvalidatedProjectKind.UpdateBundle
fun  emit(writeFile: WriteFileCallback = definedExternally, customTransformers: CustomTransformers = definedExternally): /* EmitResult | BuildInvalidedProject<T> | undefined */
        }
    
typealias InvalidatedProject<T : BuilderProgram > = /* UpdateOutputFileStampsProject | BuildInvalidedProject<T> | UpdateBundleProject<T> */
typealias ActionSet = action::set
typealias ActionInvalidate = action::invalidate
typealias ActionPackageInstalled = action::packageInstalled
typealias EventTypesRegistry = event::typesRegistry
typealias EventBeginInstallTypes = event::beginInstallTypes
typealias EventEndInstallTypes = event::endInstallTypes
typealias EventInitializationFailed = event::initializationFailed

        external interface TypingInstallerResponse   {
            val kind: /* ActionSet | ActionInvalidate | EventTypesRegistry | ActionPackageInstalled | EventBeginInstallTypes | EventEndInstallTypes | EventInitializationFailed */
        }
    

        external interface TypingInstallerRequestWithProjectName   {
            val projectName: String
        }
    

        external interface DiscoverTypings   : TypingInstallerRequestWithProjectName {
            val fileNames: Array<String>
val projectRootPath: Path
val compilerOptions: CompilerOptions
val watchOptions: WatchOptions?
val typeAcquisition: TypeAcquisition
val unresolvedImports: SortedReadonlyArray<String>
val cachePath: String?
val kind: discover
        }
    

        external interface CloseProject   : TypingInstallerRequestWithProjectName {
            val kind: closeProject
        }
    

        external interface TypesRegistryRequest   {
            val kind: typesRegistry
        }
    

        external interface InstallPackageRequest   : TypingInstallerRequestWithProjectName {
            val kind: installPackage
val fileName: Path
val packageName: String
val projectRootPath: Path
        }
    

        external interface PackageInstalledResponse   : ProjectResponse {
            val kind: ActionPackageInstalled
val success: Boolean
val message: String
        }
    

        external interface InitializationFailedResponse   : TypingInstallerResponse {
            val kind: EventInitializationFailed
val message: String
val stack: String?
        }
    

        external interface ProjectResponse   : TypingInstallerResponse {
            val projectName: String
        }
    

        external interface InvalidateCachedTypings   : ProjectResponse {
            val kind: ActionInvalidate
        }
    

        external interface InstallTypes   : ProjectResponse {
            val kind: /* EventBeginInstallTypes | EventEndInstallTypes */
val eventId: Double
val typingsInstallerVersion: String
val packagesToInstall: Array<out String>
        }
    

        external interface BeginInstallTypes   : InstallTypes {
            val kind: EventBeginInstallTypes
        }
    

        external interface EndInstallTypes   : InstallTypes {
            val kind: EventEndInstallTypes
val installSuccess: Boolean
        }
    

        external interface SetTypings   : ProjectResponse {
            val typeAcquisition: TypeAcquisition
val compilerOptions: CompilerOptions
val typings: Array<String>
val unresolvedImports: SortedReadonlyArray<String>
val kind: ActionSet
        }
    

        external interface SourceFileLike   {
            fun  getLineAndCharacterOfPosition(pos: Double): LineAndCharacter
        }
    

        external interface IScriptSnapshot   {
            fun  getText(start: Double, end: Double): String
fun  getLength(): Double
fun  getChangeRange(oldSnapshot: IScriptSnapshot): TextChangeRange?
fun  dispose(): Unit
        }
    
external fun  fromString(text: String): IScriptSnapshot

        external interface PreProcessedFileInfo   {
            var referencedFiles: Array<FileReference>
var typeReferenceDirectives: Array<FileReference>
var libReferenceDirectives: Array<FileReference>
var importedFiles: Array<FileReference>
var ambientExternalModules: Array<String>?
var isLibFile: Boolean
        }
    

        external interface HostCancellationToken   {
            fun  isCancellationRequested(): Boolean
        }
    

        external interface InstallPackageOptions   {
            var fileName: Path
var packageName: String
        }
    

        external interface PerformanceEvent   {
            var kind: /* "UpdateGraph" | "CreatePackageJsonAutoImportProvider" */
var durationMs: Double
        }
    

        external enum class LanguageServiceMode {
            Semantic,
PartialSemantic,
Syntactic
        }
    

        external interface IncompleteCompletionsCache   {
            fun  get(): CompletionInfo?
fun  set(response: CompletionInfo): Unit
fun  clear(): Unit
        }
    

        external interface LanguageServiceHost   : GetEffectiveTypeRootsHost {
            fun  getCompilationSettings(): CompilerOptions
fun  getNewLine(): String
fun  getProjectVersion(): String
fun  getScriptFileNames(): Array<String>
fun  getScriptKind(fileName: String): ScriptKind
fun  getScriptVersion(fileName: String): String
fun  getScriptSnapshot(fileName: String): IScriptSnapshot?
fun  getProjectReferences(): Array<out ProjectReference>?
fun  getLocalizedDiagnosticMessages(): Any?
fun  getCancellationToken(): HostCancellationToken
fun  getCurrentDirectory(): String
fun  getDefaultLibFileName(options: CompilerOptions): String
fun  log(s: String): Unit
fun  trace(s: String): Unit
fun  error(s: String): Unit
fun  useCaseSensitiveFileNames(): Boolean
fun  readDirectory(path: String, extensions: Array<out String> = definedExternally, exclude: Array<out String> = definedExternally, include: Array<out String> = definedExternally, depth: Double = definedExternally): Array<String>
fun  readFile(path: String, encoding: String = definedExternally): String?
fun  realpath(path: String): String
fun  fileExists(path: String): Boolean
fun  getTypeRootsVersion(): Double
fun  resolveModuleNames(moduleNames: Array<String>, containingFile: String, reusedNames: Array<String>?, redirectedReference: ResolvedProjectReference?, options: CompilerOptions): Array<(ResolvedModule?)>
fun  getResolvedModuleWithFailedLookupLocationsFromCache(modulename: String, containingFile: String): ResolvedModuleWithFailedLookupLocations?
fun  resolveTypeReferenceDirectives(typeDirectiveNames: Array<String>, containingFile: String, redirectedReference: ResolvedProjectReference?, options: CompilerOptions): Array<(ResolvedTypeReferenceDirective?)>
fun  getDirectories(directoryName: String): Array<String>
fun  getCustomTransformers(): CustomTransformers?
fun  isKnownTypesPackageName(name: String): Boolean
fun  installPackage(options: InstallPackageOptions): Promise<ApplyCodeActionCommandResult>
fun  writeFile(fileName: String, content: String): Unit
fun  getParsedCommandLine(fileName: String): ParsedCommandLine?
        }
    
typealias WithMetadata<T  > = /* T & {
        metadata?: unknown;
    } */

        external enum class SemanticClassificationFormat {
            Original,
TwentyTwenty
        }
    

        external interface LanguageService   {
            fun  cleanupSemanticCache(): Unit
fun  getSyntacticDiagnostics(fileName: String): Array<DiagnosticWithLocation>
fun  getSemanticDiagnostics(fileName: String): Array<Diagnostic>
fun  getSuggestionDiagnostics(fileName: String): Array<DiagnosticWithLocation>
fun  getCompilerOptionsDiagnostics(): Array<Diagnostic>
fun  getSyntacticClassifications(fileName: String, span: TextSpan): Array<ClassifiedSpan>
fun  getSyntacticClassifications(fileName: String, span: TextSpan, format: SemanticClassificationFormat): /* ClassifiedSpan[] | ClassifiedSpan2020[] */
fun  getSemanticClassifications(fileName: String, span: TextSpan): Array<ClassifiedSpan>
fun  getSemanticClassifications(fileName: String, span: TextSpan, format: SemanticClassificationFormat): /* ClassifiedSpan[] | ClassifiedSpan2020[] */
fun  getEncodedSyntacticClassifications(fileName: String, span: TextSpan): Classifications
fun  getEncodedSemanticClassifications(fileName: String, span: TextSpan, format: SemanticClassificationFormat = definedExternally): Classifications
fun  getCompletionsAtPosition(fileName: String, position: Double, options: GetCompletionsAtPositionOptions?): WithMetadata<CompletionInfo>?
fun  getCompletionEntryDetails(fileName: String, position: Double, entryName: String, formatOptions: /* FormatCodeOptions | FormatCodeSettings | undefined */, source: String?, preferences: UserPreferences?, data: CompletionEntryData?): CompletionEntryDetails?
fun  getCompletionEntrySymbol(fileName: String, position: Double, name: String, source: String?): Symbol?
fun  getQuickInfoAtPosition(fileName: String, position: Double): QuickInfo?
fun  getNameOrDottedNameSpan(fileName: String, startPos: Double, endPos: Double): TextSpan?
fun  getBreakpointStatementAtPosition(fileName: String, position: Double): TextSpan?
fun  getSignatureHelpItems(fileName: String, position: Double, options: SignatureHelpItemsOptions?): SignatureHelpItems?
fun  getRenameInfo(fileName: String, position: Double, options: RenameInfoOptions = definedExternally): RenameInfo
fun  findRenameLocations(fileName: String, position: Double, findInStrings: Boolean, findInComments: Boolean, providePrefixAndSuffixTextForRename: Boolean = definedExternally): Array<out RenameLocation>?
fun  getSmartSelectionRange(fileName: String, position: Double): SelectionRange
fun  getDefinitionAtPosition(fileName: String, position: Double): Array<out DefinitionInfo>?
fun  getDefinitionAndBoundSpan(fileName: String, position: Double): DefinitionInfoAndBoundSpan?
fun  getTypeDefinitionAtPosition(fileName: String, position: Double): Array<out DefinitionInfo>?
fun  getImplementationAtPosition(fileName: String, position: Double): Array<out ImplementationLocation>?
fun  getReferencesAtPosition(fileName: String, position: Double): Array<ReferenceEntry>?
fun  findReferences(fileName: String, position: Double): Array<ReferencedSymbol>?
fun  getDocumentHighlights(fileName: String, position: Double, filesToSearch: Array<String>): Array<DocumentHighlights>?
fun  getFileReferences(fileName: String): Array<ReferenceEntry>
fun  getOccurrencesAtPosition(fileName: String, position: Double): Array<out ReferenceEntry>?
fun  getNavigateToItems(searchValue: String, maxResultCount: Double = definedExternally, fileName: String = definedExternally, excludeDtsFiles: Boolean = definedExternally): Array<NavigateToItem>
fun  getNavigationBarItems(fileName: String): Array<NavigationBarItem>
fun  getNavigationTree(fileName: String): NavigationTree
fun  prepareCallHierarchy(fileName: String, position: Double): /* CallHierarchyItem | CallHierarchyItem[] | undefined */
fun  provideCallHierarchyIncomingCalls(fileName: String, position: Double): Array<CallHierarchyIncomingCall>
fun  provideCallHierarchyOutgoingCalls(fileName: String, position: Double): Array<CallHierarchyOutgoingCall>
fun  provideInlayHints(fileName: String, span: TextSpan, preferences: UserPreferences?): Array<InlayHint>
fun  getOutliningSpans(fileName: String): Array<OutliningSpan>
fun  getTodoComments(fileName: String, descriptors: Array<TodoCommentDescriptor>): Array<TodoComment>
fun  getBraceMatchingAtPosition(fileName: String, position: Double): Array<TextSpan>
fun  getIndentationAtPosition(fileName: String, position: Double, options: /* EditorOptions | EditorSettings */): Double
fun  getFormattingEditsForRange(fileName: String, start: Double, end: Double, options: /* FormatCodeOptions | FormatCodeSettings */): Array<TextChange>
fun  getFormattingEditsForDocument(fileName: String, options: /* FormatCodeOptions | FormatCodeSettings */): Array<TextChange>
fun  getFormattingEditsAfterKeystroke(fileName: String, position: Double, key: String, options: /* FormatCodeOptions | FormatCodeSettings */): Array<TextChange>
fun  getDocCommentTemplateAtPosition(fileName: String, position: Double, options: DocCommentTemplateOptions = definedExternally): TextInsertion?
fun  isValidBraceCompletionAtPosition(fileName: String, position: Double, openingBrace: Double): Boolean
fun  getJsxClosingTagAtPosition(fileName: String, position: Double): JsxClosingTagInfo?
fun  getSpanOfEnclosingComment(fileName: String, position: Double, onlyMultiLine: Boolean): TextSpan?
fun  toLineColumnOffset(fileName: String, position: Double): LineAndCharacter
fun  getCodeFixesAtPosition(fileName: String, start: Double, end: Double, errorCodes: Array<out Double>, formatOptions: FormatCodeSettings, preferences: UserPreferences): Array<out CodeFixAction>
fun  getCombinedCodeFix(scope: CombinedCodeFixScope, fixId: /* {} */, formatOptions: FormatCodeSettings, preferences: UserPreferences): CombinedCodeActions
fun  applyCodeActionCommand(action: CodeActionCommand, formatSettings: FormatCodeSettings = definedExternally): Promise<ApplyCodeActionCommandResult>
fun  applyCodeActionCommand(action: Array<CodeActionCommand>, formatSettings: FormatCodeSettings = definedExternally): Promise<Array<ApplyCodeActionCommandResult>>
fun  applyCodeActionCommand(action: /* CodeActionCommand | CodeActionCommand[] */, formatSettings: FormatCodeSettings = definedExternally): Promise</* ApplyCodeActionCommandResult | ApplyCodeActionCommandResult[] */>
fun  applyCodeActionCommand(fileName: String, action: CodeActionCommand): Promise<ApplyCodeActionCommandResult>
fun  applyCodeActionCommand(fileName: String, action: Array<CodeActionCommand>): Promise<Array<ApplyCodeActionCommandResult>>
fun  applyCodeActionCommand(fileName: String, action: /* CodeActionCommand | CodeActionCommand[] */): Promise</* ApplyCodeActionCommandResult | ApplyCodeActionCommandResult[] */>
fun  getApplicableRefactors(fileName: String, positionOrRange: /* number | TextRange */, preferences: UserPreferences?, triggerReason: RefactorTriggerReason = definedExternally, kind: String = definedExternally): Array<ApplicableRefactorInfo>
fun  getEditsForRefactor(fileName: String, formatOptions: FormatCodeSettings, positionOrRange: /* number | TextRange */, refactorName: String, actionName: String, preferences: UserPreferences?): RefactorEditInfo?
fun  organizeImports(args: OrganizeImportsArgs, formatOptions: FormatCodeSettings, preferences: UserPreferences?): Array<out FileTextChanges>
fun  getEditsForFileRename(oldFilePath: String, newFilePath: String, formatOptions: FormatCodeSettings, preferences: UserPreferences?): Array<out FileTextChanges>
fun  getEmitOutput(fileName: String, emitOnlyDtsFiles: Boolean = definedExternally, forceDtsEmit: Boolean = definedExternally): EmitOutput
fun  getProgram(): Program?
fun  toggleLineComment(fileName: String, textRange: TextRange): Array<TextChange>
fun  toggleMultilineComment(fileName: String, textRange: TextRange): Array<TextChange>
fun  commentSelection(fileName: String, textRange: TextRange): Array<TextChange>
fun  uncommentSelection(fileName: String, textRange: TextRange): Array<TextChange>
fun  dispose(): Unit
        }
    

        external interface JsxClosingTagInfo   {
            val newText: String
        }
    

        external interface CombinedCodeFixScope   {
            var type: file
var fileName: String
        }
    

        external interface OrganizeImportsArgs   : CombinedCodeFixScope {
            var skipDestructiveCodeActions: Boolean?
        }
    
typealias CompletionsTriggerCharacter = /* "." | '"' | "'" | "`" | "/" | "@" | "<" | "#" | " " */

        external enum class CompletionTriggerKind {
            Invoked,
TriggerCharacter,
TriggerForIncompleteCompletions
        }
    

        external interface GetCompletionsAtPositionOptions   : UserPreferences {
            var triggerCharacter: CompletionsTriggerCharacter?
var triggerKind: CompletionTriggerKind?
var includeExternalModuleExports: Boolean?
var includeInsertTextCompletions: Boolean?
        }
    

        external interface InlayHintsOptions   : UserPreferences {
            val includeInlayParameterNameHints: /* "none" | "literals" | "all" */?
val includeInlayParameterNameHintsWhenArgumentMatchesName: Boolean?
val includeInlayFunctionParameterTypeHints: Boolean?
val includeInlayVariableTypeHints: Boolean?
val includeInlayPropertyDeclarationTypeHints: Boolean?
val includeInlayFunctionLikeReturnTypeHints: Boolean?
val includeInlayEnumMemberValueHints: Boolean?
        }
    
typealias SignatureHelpTriggerCharacter = /* "," | "(" | "<" */
typealias SignatureHelpRetriggerCharacter = /* SignatureHelpTriggerCharacter | ")" */

        external interface SignatureHelpItemsOptions   {
            var triggerReason: SignatureHelpTriggerReason?
        }
    
typealias SignatureHelpTriggerReason = /* SignatureHelpInvokedReason | SignatureHelpCharacterTypedReason | SignatureHelpRetriggeredReason */

        external interface SignatureHelpInvokedReason   {
            var kind: invoked
var triggerCharacter: null?
        }
    

        external interface SignatureHelpCharacterTypedReason   {
            var kind: characterTyped
var triggerCharacter: SignatureHelpTriggerCharacter
        }
    

        external interface SignatureHelpRetriggeredReason   {
            var kind: retrigger
var triggerCharacter: SignatureHelpRetriggerCharacter?
        }
    

        external interface ApplyCodeActionCommandResult   {
            var successMessage: String
        }
    

        external interface Classifications   {
            var spans: Array<Double>
var endOfLineState: EndOfLineState
        }
    

        external interface ClassifiedSpan   {
            var textSpan: TextSpan
var classificationType: ClassificationTypeNames
        }
    

        external interface ClassifiedSpan2020   {
            var textSpan: TextSpan
var classificationType: Double
        }
    

        external interface NavigationBarItem   {
            var text: String
var kind: ScriptElementKind
var kindModifiers: String
var spans: Array<TextSpan>
var childItems: Array<NavigationBarItem>
var indent: Double
var bolded: Boolean
var grayed: Boolean
        }
    

        external interface NavigationTree   {
            var text: String
var kind: ScriptElementKind
var kindModifiers: String
var spans: Array<TextSpan>
var nameSpan: TextSpan?
var childItems: Array<NavigationTree>?
        }
    

        external interface CallHierarchyItem   {
            var name: String
var kind: ScriptElementKind
var kindModifiers: String?
var file: String
var span: TextSpan
var selectionSpan: TextSpan
var containerName: String?
        }
    

        external interface CallHierarchyIncomingCall   {
            var from: CallHierarchyItem
var fromSpans: Array<TextSpan>
        }
    

        external interface CallHierarchyOutgoingCall   {
            var to: CallHierarchyItem
var fromSpans: Array<TextSpan>
        }
    

        external enum class InlayHintKind {
            Type,
Parameter,
Enum
        }
    

        external interface InlayHint   {
            var text: String
var position: Double
var kind: InlayHintKind
var whitespaceBefore: Boolean?
var whitespaceAfter: Boolean?
        }
    

        external interface TodoCommentDescriptor   {
            var text: String
var priority: Double
        }
    

        external interface TodoComment   {
            var descriptor: TodoCommentDescriptor
var message: String
var position: Double
        }
    

        external interface TextChange   {
            var span: TextSpan
var newText: String
        }
    

        external interface FileTextChanges   {
            var fileName: String
var textChanges: Array<out TextChange>
var isNewFile: Boolean?
        }
    

        external interface CodeAction   {
            var description: String
var changes: Array<FileTextChanges>
var commands: Array<CodeActionCommand>?
        }
    

        external interface CodeFixAction   : CodeAction {
            var fixName: String
var fixId: /* {} */?
var fixAllDescription: String?
        }
    

        external interface CombinedCodeActions   {
            var changes: Array<out FileTextChanges>
var commands: Array<out CodeActionCommand>?
        }
    
typealias CodeActionCommand = InstallPackageAction

        external interface InstallPackageAction   {
            
        }
    

        external interface ApplicableRefactorInfo   {
            var name: String
var description: String
var inlineable: Boolean?
var actions: Array<RefactorActionInfo>
        }
    

        external interface RefactorActionInfo   {
            var name: String
var description: String
var notApplicableReason: String?
var kind: String?
        }
    

        external interface RefactorEditInfo   {
            var edits: Array<FileTextChanges>
var renameFilename: String?
var renameLocation: Double?
var commands: Array<CodeActionCommand>?
        }
    
typealias RefactorTriggerReason = /* "implicit" | "invoked" */

        external interface TextInsertion   {
            var newText: String
var caretOffset: Double
        }
    

        external interface DocumentSpan   {
            var textSpan: TextSpan
var fileName: String
var originalTextSpan: TextSpan?
var originalFileName: String?
var contextSpan: TextSpan?
var originalContextSpan: TextSpan?
        }
    

        external interface RenameLocation   : DocumentSpan {
            val prefixText: String?
val suffixText: String?
        }
    

        external interface ReferenceEntry   : DocumentSpan {
            var isWriteAccess: Boolean
var isDefinition: Boolean
var isInString: Boolean?
        }
    

        external interface ImplementationLocation   : DocumentSpan {
            var kind: ScriptElementKind
var displayParts: Array<SymbolDisplayPart>
        }
    

        external enum class HighlightSpanKind {
            none,
definition,
reference,
writtenReference
        }
    

        external interface HighlightSpan   {
            var fileName: String?
var isInString: Boolean?
var textSpan: TextSpan
var contextSpan: TextSpan?
var kind: HighlightSpanKind
        }
    

        external interface NavigateToItem   {
            var name: String
var kind: ScriptElementKind
var kindModifiers: String
var matchKind: /* "exact" | "prefix" | "substring" | "camelCase" */
var isCaseSensitive: Boolean
var fileName: String
var textSpan: TextSpan
var containerName: String
var containerKind: ScriptElementKind
        }
    

        external enum class IndentStyle {
            None,
Block,
Smart
        }
    

        external enum class SemicolonPreference {
            Ignore,
Insert,
Remove
        }
    

        external interface EditorOptions   {
            var BaseIndentSize: Double?
var IndentSize: Double
var TabSize: Double
var NewLineCharacter: String
var ConvertTabsToSpaces: Boolean
var IndentStyle: IndentStyle
        }
    

        external interface EditorSettings   {
            var baseIndentSize: Double?
var indentSize: Double?
var tabSize: Double?
var newLineCharacter: String?
var convertTabsToSpaces: Boolean?
var indentStyle: IndentStyle?
var trimTrailingWhitespace: Boolean?
        }
    

        external interface FormatCodeOptions   : EditorOptions {
            var InsertSpaceAfterCommaDelimiter: Boolean
var InsertSpaceAfterSemicolonInForStatements: Boolean
var InsertSpaceBeforeAndAfterBinaryOperators: Boolean
var InsertSpaceAfterConstructor: Boolean?
var InsertSpaceAfterKeywordsInControlFlowStatements: Boolean
var InsertSpaceAfterFunctionKeywordForAnonymousFunctions: Boolean
var InsertSpaceAfterOpeningAndBeforeClosingNonemptyParenthesis: Boolean
var InsertSpaceAfterOpeningAndBeforeClosingNonemptyBrackets: Boolean
var InsertSpaceAfterOpeningAndBeforeClosingNonemptyBraces: Boolean?
var InsertSpaceAfterOpeningAndBeforeClosingTemplateStringBraces: Boolean
var InsertSpaceAfterOpeningAndBeforeClosingJsxExpressionBraces: Boolean?
var InsertSpaceAfterTypeAssertion: Boolean?
var InsertSpaceBeforeFunctionParenthesis: Boolean?
var PlaceOpenBraceOnNewLineForFunctions: Boolean
var PlaceOpenBraceOnNewLineForControlBlocks: Boolean
var insertSpaceBeforeTypeAnnotation: Boolean?
        }
    

        external interface FormatCodeSettings   : EditorSettings {
            val insertSpaceAfterCommaDelimiter: Boolean?
val insertSpaceAfterSemicolonInForStatements: Boolean?
val insertSpaceBeforeAndAfterBinaryOperators: Boolean?
val insertSpaceAfterConstructor: Boolean?
val insertSpaceAfterKeywordsInControlFlowStatements: Boolean?
val insertSpaceAfterFunctionKeywordForAnonymousFunctions: Boolean?
val insertSpaceAfterOpeningAndBeforeClosingNonemptyParenthesis: Boolean?
val insertSpaceAfterOpeningAndBeforeClosingNonemptyBrackets: Boolean?
val insertSpaceAfterOpeningAndBeforeClosingNonemptyBraces: Boolean?
val insertSpaceAfterOpeningAndBeforeClosingEmptyBraces: Boolean?
val insertSpaceAfterOpeningAndBeforeClosingTemplateStringBraces: Boolean?
val insertSpaceAfterOpeningAndBeforeClosingJsxExpressionBraces: Boolean?
val insertSpaceAfterTypeAssertion: Boolean?
val insertSpaceBeforeFunctionParenthesis: Boolean?
val placeOpenBraceOnNewLineForFunctions: Boolean?
val placeOpenBraceOnNewLineForControlBlocks: Boolean?
val insertSpaceBeforeTypeAnnotation: Boolean?
val indentMultiLineObjectLiteralBeginningOnBlankLine: Boolean?
val semicolons: SemicolonPreference?
        }
    
external fun  getDefaultFormatCodeSettings(newLineCharacter: String = definedExternally): FormatCodeSettings

        external interface DefinitionInfo   : DocumentSpan {
            var kind: ScriptElementKind
var name: String
var containerKind: ScriptElementKind
var containerName: String
var unverified: Boolean?
        }
    

        external interface DefinitionInfoAndBoundSpan   {
            var definitions: Array<out DefinitionInfo>?
var textSpan: TextSpan
        }
    

        external interface ReferencedSymbolDefinitionInfo   : DefinitionInfo {
            var displayParts: Array<SymbolDisplayPart>
        }
    

        external interface ReferencedSymbol   {
            var definition: ReferencedSymbolDefinitionInfo
var references: Array<ReferenceEntry>
        }
    

        external enum class SymbolDisplayPartKind {
            aliasName,
className,
enumName,
fieldName,
interfaceName,
keyword,
lineBreak,
numericLiteral,
stringLiteral,
localName,
methodName,
moduleName,
operator,
parameterName,
propertyName,
punctuation,
space,
text,
typeParameterName,
enumMemberName,
functionName,
regularExpressionLiteral,
link,
linkName,
linkText
        }
    

        external interface SymbolDisplayPart   {
            var text: String
var kind: String
        }
    

        external interface JSDocLinkDisplayPart   : SymbolDisplayPart {
            var target: DocumentSpan
        }
    

        external interface JSDocTagInfo   {
            var name: String
var text: Array<SymbolDisplayPart>?
        }
    

        external interface QuickInfo   {
            var kind: ScriptElementKind
var kindModifiers: String
var textSpan: TextSpan
var displayParts: Array<SymbolDisplayPart>?
var documentation: Array<SymbolDisplayPart>?
var tags: Array<JSDocTagInfo>?
        }
    
typealias RenameInfo = /* RenameInfoSuccess | RenameInfoFailure */

        external interface RenameInfoSuccess   {
            var canRename: Boolean
var fileToRename: String?
var displayName: String
var fullDisplayName: String
var kind: ScriptElementKind
var kindModifiers: String
var triggerSpan: TextSpan
        }
    

        external interface RenameInfoFailure   {
            var canRename: Boolean
var localizedErrorMessage: String
        }
    

        external interface RenameInfoOptions   {
            val allowRenameOfImportPath: Boolean?
        }
    

        external interface DocCommentTemplateOptions   {
            val generateReturnInDocTemplate: Boolean?
        }
    

        external interface SignatureHelpParameter   {
            var name: String
var documentation: Array<SymbolDisplayPart>
var displayParts: Array<SymbolDisplayPart>
var isOptional: Boolean
var isRest: Boolean?
        }
    

        external interface SelectionRange   {
            var textSpan: TextSpan
var parent: SelectionRange?
        }
    

        external interface SignatureHelpItem   {
            var isVariadic: Boolean
var prefixDisplayParts: Array<SymbolDisplayPart>
var suffixDisplayParts: Array<SymbolDisplayPart>
var separatorDisplayParts: Array<SymbolDisplayPart>
var parameters: Array<SignatureHelpParameter>
var documentation: Array<SymbolDisplayPart>
var tags: Array<JSDocTagInfo>
        }
    

        external interface SignatureHelpItems   {
            var items: Array<SignatureHelpItem>
var applicableSpan: TextSpan
var selectedItemIndex: Double
var argumentIndex: Double
var argumentCount: Double
        }
    

        external interface CompletionInfo   {
            var isGlobalCompletion: Boolean
var isMemberCompletion: Boolean
var optionalReplacementSpan: TextSpan?
var isNewIdentifierLocation: Boolean
var isIncomplete: Boolean?
var entries: Array<CompletionEntry>
        }
    

        external interface CompletionEntryData   {
            var fileName: String?
var ambientModuleName: String?
var isPackageJsonImport: Boolean?
var exportName: String
var moduleSpecifier: String?
        }
    

        external interface CompletionEntry   {
            var name: String
var kind: ScriptElementKind
var kindModifiers: String?
var sortText: String
var insertText: String?
var isSnippet: Boolean?
var replacementSpan: TextSpan?
var hasAction: Boolean?
var source: String?
var sourceDisplay: Array<SymbolDisplayPart>?
var isRecommended: Boolean?
var isFromUncheckedFile: Boolean?
var isPackageJsonImport: Boolean?
var isImportStatementCompletion: Boolean?
var data: CompletionEntryData?
        }
    

        external interface CompletionEntryDetails   {
            var name: String
var kind: ScriptElementKind
var kindModifiers: String
var displayParts: Array<SymbolDisplayPart>
var documentation: Array<SymbolDisplayPart>?
var tags: Array<JSDocTagInfo>?
var codeActions: Array<CodeAction>?
var source: Array<SymbolDisplayPart>?
var sourceDisplay: Array<SymbolDisplayPart>?
        }
    

        external interface OutliningSpan   {
            var textSpan: TextSpan
var hintSpan: TextSpan
var bannerText: String
var autoCollapse: Boolean
var kind: OutliningSpanKind
        }
    

        external enum class OutliningSpanKind {
            Comment,
Region,
Code,
Imports
        }
    

        external enum class OutputFileType {
            JavaScript,
SourceMap,
Declaration
        }
    

        external enum class EndOfLineState {
            None,
InMultiLineCommentTrivia,
InSingleQuoteStringLiteral,
InDoubleQuoteStringLiteral,
InTemplateHeadOrNoSubstitutionTemplate,
InTemplateMiddleOrTail,
InTemplateSubstitutionPosition
        }
    

        external enum class TokenClass {
            Punctuation,
Keyword,
Operator,
Comment,
Whitespace,
Identifier,
NumberLiteral,
BigIntLiteral,
StringLiteral,
RegExpLiteral
        }
    

        external interface ClassificationResult   {
            var finalLexState: EndOfLineState
var entries: Array<ClassificationInfo>
        }
    

        external interface ClassificationInfo   {
            var length: Double
var classification: TokenClass
        }
    

        external interface Classifier   {
            fun  getClassificationsForLine(text: String, lexState: EndOfLineState, syntacticClassifierAbsent: Boolean): ClassificationResult
fun  getEncodedLexicalClassifications(text: String, endOfLineState: EndOfLineState, syntacticClassifierAbsent: Boolean): Classifications
        }
    

        external enum class ScriptElementKind {
            unknown,
warning,
keyword,
scriptElement,
moduleElement,
classElement,
localClassElement,
interfaceElement,
typeElement,
enumElement,
enumMemberElement,
variableElement,
localVariableElement,
functionElement,
localFunctionElement,
memberFunctionElement,
memberGetAccessorElement,
memberSetAccessorElement,
memberVariableElement,
constructorImplementationElement,
callSignatureElement,
indexSignatureElement,
constructSignatureElement,
parameterElement,
typeParameterElement,
primitiveType,
label,
alias,
constElement,
letElement,
directory,
externalModuleName,
jsxAttribute,
string,
link,
linkName,
linkText
        }
    

        external enum class ScriptElementKindModifier {
            none,
publicMemberModifier,
privateMemberModifier,
protectedMemberModifier,
exportedModifier,
ambientModifier,
staticModifier,
abstractModifier,
optionalModifier,
deprecatedModifier,
dtsModifier,
tsModifier,
tsxModifier,
jsModifier,
jsxModifier,
jsonModifier
        }
    

        external enum class ClassificationTypeNames {
            comment,
identifier,
keyword,
numericLiteral,
bigintLiteral,
operator,
stringLiteral,
whiteSpace,
text,
punctuation,
className,
enumName,
interfaceName,
moduleName,
typeParameterName,
typeAliasName,
parameterName,
docCommentTagName,
jsxOpenTagName,
jsxCloseTagName,
jsxSelfClosingTagName,
jsxAttribute,
jsxText,
jsxAttributeStringLiteralValue
        }
    

        external enum class ClassificationType {
            comment,
identifier,
keyword,
numericLiteral,
operator,
stringLiteral,
regularExpressionLiteral,
whiteSpace,
text,
punctuation,
className,
enumName,
interfaceName,
moduleName,
typeParameterName,
typeAliasName,
parameterName,
docCommentTagName,
jsxOpenTagName,
jsxCloseTagName,
jsxSelfClosingTagName,
jsxAttribute,
jsxText,
jsxAttributeStringLiteralValue,
bigintLiteral
        }
    

        external interface InlayHintsContext   {
            var file: SourceFile
var program: Program
var cancellationToken: CancellationToken
var host: LanguageServiceHost
var span: TextSpan
var preferences: InlayHintsOptions
        }
    
external fun  createClassifier(): Classifier

        external interface DocumentHighlights   {
            var fileName: String
var highlightSpans: Array<HighlightSpan>
        }
    

        external interface DocumentRegistry   {
            fun  acquireDocument(fileName: String, compilationSettings: CompilerOptions, scriptSnapshot: IScriptSnapshot, version: String, scriptKind: ScriptKind = definedExternally): SourceFile
fun  acquireDocumentWithKey(fileName: String, path: Path, compilationSettings: CompilerOptions, key: DocumentRegistryBucketKey, scriptSnapshot: IScriptSnapshot, version: String, scriptKind: ScriptKind = definedExternally): SourceFile
fun  updateDocument(fileName: String, compilationSettings: CompilerOptions, scriptSnapshot: IScriptSnapshot, version: String, scriptKind: ScriptKind = definedExternally): SourceFile
fun  updateDocumentWithKey(fileName: String, path: Path, compilationSettings: CompilerOptions, key: DocumentRegistryBucketKey, scriptSnapshot: IScriptSnapshot, version: String, scriptKind: ScriptKind = definedExternally): SourceFile
fun  getKeyForCompilationSettings(settings: CompilerOptions): DocumentRegistryBucketKey
fun  releaseDocument(fileName: String, compilationSettings: CompilerOptions): Unit
fun  releaseDocument(fileName: String, compilationSettings: CompilerOptions, scriptKind: ScriptKind): Unit
fun  releaseDocumentWithKey(path: Path, key: DocumentRegistryBucketKey): Unit
fun  releaseDocumentWithKey(path: Path, key: DocumentRegistryBucketKey, scriptKind: ScriptKind): Unit
fun  reportStats(): String
        }
    
typealias DocumentRegistryBucketKey = /* string & {
        __bucketKey: any;
    } */
external fun  createDocumentRegistry(useCaseSensitiveFileNames: Boolean = definedExternally, currentDirectory: String = definedExternally): DocumentRegistry
external fun  preProcessFile(sourceText: String, readImportFiles: Boolean = definedExternally, detectJavaScriptImports: Boolean = definedExternally): PreProcessedFileInfo

        external interface TranspileOptions   {
            var compilerOptions: CompilerOptions?
var fileName: String?
var reportDiagnostics: Boolean?
var moduleName: String?
var renamedDependencies: MapLike<String>?
var transformers: CustomTransformers?
        }
    

        external interface TranspileOutput   {
            var outputText: String
var diagnostics: Array<Diagnostic>?
var sourceMapText: String?
        }
    
external fun  transpileModule(input: String, transpileOptions: TranspileOptions): TranspileOutput
external fun  transpile(input: String, compilerOptions: CompilerOptions = definedExternally, fileName: String = definedExternally, diagnostics: Array<Diagnostic> = definedExternally, moduleName: String = definedExternally): String
external val servicesVersion: Any? /* should be inferred */
external fun  toEditorSettings(options: /* EditorOptions | EditorSettings */): EditorSettings
external fun  displayPartsToString(displayParts: Array<SymbolDisplayPart>?): String
external fun  getDefaultCompilerOptions(): CompilerOptions
external fun  getSupportedCodeFixes(): Array<String>
external fun  createLanguageServiceSourceFile(fileName: String, scriptSnapshot: IScriptSnapshot, scriptTarget: ScriptTarget, version: String, setNodeParents: Boolean, scriptKind: ScriptKind = definedExternally): SourceFile
external fun  updateLanguageServiceSourceFile(sourceFile: SourceFile, scriptSnapshot: IScriptSnapshot, version: String, textChangeRange: TextChangeRange?, aggressiveChecks: Boolean = definedExternally): SourceFile
external fun  createLanguageService(host: LanguageServiceHost, documentRegistry: DocumentRegistry = definedExternally, syntaxOnlyOrLanguageServiceMode: /* boolean | LanguageServiceMode */ = definedExternally): LanguageService
external fun  getDefaultLibFilePath(options: CompilerOptions): String
external fun <T : Node > transform(source: /* T | T[] */, transformers: Array<TransformerFactory<T>>, compilerOptions: CompilerOptions = definedExternally): TransformationResult<T>
external val createNodeArray: (vararg args: Any?) -> Any?
external val createNumericLiteral: (value: /* string | number */, numericLiteralFlags: TokenFlags? = definedExternally) -> NumericLiteral
external val createBigIntLiteral: (value: /* string | PseudoBigInt */) -> BigIntLiteral
external val createStringLiteral: /* {
        (text: string, isSingleQuote?: boolean | undefined): StringLiteral;
        (text: string, isSingleQuote?: boolean | undefined, hasExtendedUnicodeEscape?: boolean | undefined): StringLiteral;
    } */
external val createStringLiteralFromNode: (sourceNode: PropertyNameLiteral, isSingleQuote: Boolean? = definedExternally) -> StringLiteral
external val createRegularExpressionLiteral: (text: String) -> RegularExpressionLiteral
external val createLoopVariable: (reservedInNestedScopes: Boolean? = definedExternally) -> Identifier
external val createUniqueName: (text: String, flags: GeneratedIdentifierFlags? = definedExternally) -> Identifier
external val createPrivateIdentifier: (text: String) -> PrivateIdentifier
external val createSuper: () -> SuperExpression
external val createThis: () -> ThisExpression
external val createNull: () -> NullLiteral
external val createTrue: () -> TrueLiteral
external val createFalse: () -> FalseLiteral
external val createModifier: (vararg args: Any?) -> Any?
external val createModifiersFromModifierFlags: (flags: ModifierFlags) -> Array<Modifier>
external val createQualifiedName: (left: EntityName, right: /* string | Identifier */) -> QualifiedName
external val updateQualifiedName: (node: QualifiedName, left: EntityName, right: Identifier) -> QualifiedName
external val createComputedPropertyName: (expression: Expression) -> ComputedPropertyName
external val updateComputedPropertyName: (node: ComputedPropertyName, expression: Expression) -> ComputedPropertyName
external val createTypeParameterDeclaration: (name: /* string | Identifier */, constraint: TypeNode? = definedExternally, defaultType: TypeNode? = definedExternally) -> TypeParameterDeclaration
external val updateTypeParameterDeclaration: (node: TypeParameterDeclaration, name: Identifier, constraint: TypeNode?, defaultType: TypeNode?) -> TypeParameterDeclaration
external val createParameter: (decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, dotDotDotToken: DotDotDotToken?, name: /* string | BindingName */, questionToken: QuestionToken? = definedExternally, type: TypeNode? = definedExternally, initializer: Expression? = definedExternally) -> ParameterDeclaration
external val updateParameter: (node: ParameterDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, dotDotDotToken: DotDotDotToken?, name: /* string | BindingName */, questionToken: QuestionToken?, type: TypeNode?, initializer: Expression?) -> ParameterDeclaration
external val createDecorator: (expression: Expression) -> Decorator
external val updateDecorator: (node: Decorator, expression: Expression) -> Decorator
external val createProperty: (decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: /* string | PropertyName */, questionOrExclamationToken: /* QuestionToken | ExclamationToken | undefined */, type: TypeNode?, initializer: Expression?) -> PropertyDeclaration
external val updateProperty: (node: PropertyDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: /* string | PropertyName */, questionOrExclamationToken: /* QuestionToken | ExclamationToken | undefined */, type: TypeNode?, initializer: Expression?) -> PropertyDeclaration
external val createMethod: (decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, asteriskToken: AsteriskToken?, name: /* string | PropertyName */, questionToken: QuestionToken?, typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode?, body: Block?) -> MethodDeclaration
external val updateMethod: (node: MethodDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, asteriskToken: AsteriskToken?, name: PropertyName, questionToken: QuestionToken?, typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode?, body: Block?) -> MethodDeclaration
external val createConstructor: (decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, parameters: Array<out ParameterDeclaration>, body: Block?) -> ConstructorDeclaration
external val updateConstructor: (node: ConstructorDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, parameters: Array<out ParameterDeclaration>, body: Block?) -> ConstructorDeclaration
external val createGetAccessor: (decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: /* string | PropertyName */, parameters: Array<out ParameterDeclaration>, type: TypeNode?, body: Block?) -> GetAccessorDeclaration
external val updateGetAccessor: (node: GetAccessorDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: PropertyName, parameters: Array<out ParameterDeclaration>, type: TypeNode?, body: Block?) -> GetAccessorDeclaration
external val createSetAccessor: (decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: /* string | PropertyName */, parameters: Array<out ParameterDeclaration>, body: Block?) -> SetAccessorDeclaration
external val updateSetAccessor: (node: SetAccessorDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: PropertyName, parameters: Array<out ParameterDeclaration>, body: Block?) -> SetAccessorDeclaration
external val createCallSignature: (typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode?) -> CallSignatureDeclaration
external val updateCallSignature: (node: CallSignatureDeclaration, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode?) -> CallSignatureDeclaration
external val createConstructSignature: (typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode?) -> ConstructSignatureDeclaration
external val updateConstructSignature: (node: ConstructSignatureDeclaration, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode?) -> ConstructSignatureDeclaration
external val updateIndexSignature: (node: IndexSignatureDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, parameters: Array<out ParameterDeclaration>, type: TypeNode) -> IndexSignatureDeclaration
external val createKeywordTypeNode: (vararg args: Any?) -> Any?
external val createTypePredicateNodeWithModifier: (assertsModifier: AssertsKeyword?, parameterName: /* string | Identifier | ThisTypeNode */, type: TypeNode?) -> TypePredicateNode
external val updateTypePredicateNodeWithModifier: (node: TypePredicateNode, assertsModifier: AssertsKeyword?, parameterName: /* Identifier | ThisTypeNode */, type: TypeNode?) -> TypePredicateNode
external val createTypeReferenceNode: (typeName: /* string | EntityName */, typeArguments: Array<out TypeNode>? = definedExternally) -> TypeReferenceNode
external val updateTypeReferenceNode: (node: TypeReferenceNode, typeName: EntityName, typeArguments: NodeArray<TypeNode>?) -> TypeReferenceNode
external val createFunctionTypeNode: (typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode) -> FunctionTypeNode
external val updateFunctionTypeNode: (node: FunctionTypeNode, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode) -> FunctionTypeNode
external val createConstructorTypeNode: (typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode) -> ConstructorTypeNode
external val updateConstructorTypeNode: (node: ConstructorTypeNode, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode) -> ConstructorTypeNode
external val createTypeQueryNode: (exprName: EntityName) -> TypeQueryNode
external val updateTypeQueryNode: (node: TypeQueryNode, exprName: EntityName) -> TypeQueryNode
external val createTypeLiteralNode: (members: Array<out TypeElement>?) -> TypeLiteralNode
external val updateTypeLiteralNode: (node: TypeLiteralNode, members: NodeArray<TypeElement>) -> TypeLiteralNode
external val createArrayTypeNode: (elementType: TypeNode) -> ArrayTypeNode
external val updateArrayTypeNode: (node: ArrayTypeNode, elementType: TypeNode) -> ArrayTypeNode
external val createTupleTypeNode: (elements: Array<out (/* TypeNode | NamedTupleMember */)>) -> TupleTypeNode
external val updateTupleTypeNode: (node: TupleTypeNode, elements: Array<out (/* TypeNode | NamedTupleMember */)>) -> TupleTypeNode
external val createOptionalTypeNode: (type: TypeNode) -> OptionalTypeNode
external val updateOptionalTypeNode: (node: OptionalTypeNode, type: TypeNode) -> OptionalTypeNode
external val createRestTypeNode: (type: TypeNode) -> RestTypeNode
external val updateRestTypeNode: (node: RestTypeNode, type: TypeNode) -> RestTypeNode
external val createUnionTypeNode: (types: Array<out TypeNode>) -> UnionTypeNode
external val updateUnionTypeNode: (node: UnionTypeNode, types: NodeArray<TypeNode>) -> UnionTypeNode
external val createIntersectionTypeNode: (types: Array<out TypeNode>) -> IntersectionTypeNode
external val updateIntersectionTypeNode: (node: IntersectionTypeNode, types: NodeArray<TypeNode>) -> IntersectionTypeNode
external val createConditionalTypeNode: (checkType: TypeNode, extendsType: TypeNode, trueType: TypeNode, falseType: TypeNode) -> ConditionalTypeNode
external val updateConditionalTypeNode: (node: ConditionalTypeNode, checkType: TypeNode, extendsType: TypeNode, trueType: TypeNode, falseType: TypeNode) -> ConditionalTypeNode
external val createInferTypeNode: (typeParameter: TypeParameterDeclaration) -> InferTypeNode
external val updateInferTypeNode: (node: InferTypeNode, typeParameter: TypeParameterDeclaration) -> InferTypeNode
external val createImportTypeNode: (argument: TypeNode, qualifier: EntityName? = definedExternally, typeArguments: Array<out TypeNode>? = definedExternally, isTypeOf: Boolean? = definedExternally) -> ImportTypeNode
external val updateImportTypeNode: (node: ImportTypeNode, argument: TypeNode, qualifier: EntityName?, typeArguments: Array<out TypeNode>?, isTypeOf: Boolean? = definedExternally) -> ImportTypeNode
external val createParenthesizedType: (type: TypeNode) -> ParenthesizedTypeNode
external val updateParenthesizedType: (node: ParenthesizedTypeNode, type: TypeNode) -> ParenthesizedTypeNode
external val createThisTypeNode: () -> ThisTypeNode
external val updateTypeOperatorNode: (node: TypeOperatorNode, type: TypeNode) -> TypeOperatorNode
external val createIndexedAccessTypeNode: (objectType: TypeNode, indexType: TypeNode) -> IndexedAccessTypeNode
external val updateIndexedAccessTypeNode: (node: IndexedAccessTypeNode, objectType: TypeNode, indexType: TypeNode) -> IndexedAccessTypeNode
external val createMappedTypeNode: (readonlyToken: /* ReadonlyKeyword | PlusToken | MinusToken | undefined */, typeParameter: TypeParameterDeclaration, nameType: TypeNode?, questionToken: /* QuestionToken | PlusToken | MinusToken | undefined */, type: TypeNode?) -> MappedTypeNode
external val updateMappedTypeNode: (node: MappedTypeNode, readonlyToken: /* ReadonlyKeyword | PlusToken | MinusToken | undefined */, typeParameter: TypeParameterDeclaration, nameType: TypeNode?, questionToken: /* QuestionToken | PlusToken | MinusToken | undefined */, type: TypeNode?) -> MappedTypeNode
external val createLiteralTypeNode: (literal: /* LiteralExpression | BooleanLiteral | PrefixUnaryExpression | NullLiteral */) -> LiteralTypeNode
external val updateLiteralTypeNode: (node: LiteralTypeNode, literal: /* LiteralExpression | BooleanLiteral | PrefixUnaryExpression | NullLiteral */) -> LiteralTypeNode
external val createObjectBindingPattern: (elements: Array<out BindingElement>) -> ObjectBindingPattern
external val updateObjectBindingPattern: (node: ObjectBindingPattern, elements: Array<out BindingElement>) -> ObjectBindingPattern
external val createArrayBindingPattern: (elements: Array<out ArrayBindingElement>) -> ArrayBindingPattern
external val updateArrayBindingPattern: (node: ArrayBindingPattern, elements: Array<out ArrayBindingElement>) -> ArrayBindingPattern
external val createBindingElement: (dotDotDotToken: DotDotDotToken?, propertyName: /* string | PropertyName | undefined */, name: /* string | BindingName */, initializer: Expression? = definedExternally) -> BindingElement
external val updateBindingElement: (node: BindingElement, dotDotDotToken: DotDotDotToken?, propertyName: PropertyName?, name: BindingName, initializer: Expression?) -> BindingElement
external val createArrayLiteral: (elements: Array<out Expression>? = definedExternally, multiLine: Boolean? = definedExternally) -> ArrayLiteralExpression
external val updateArrayLiteral: (node: ArrayLiteralExpression, elements: Array<out Expression>) -> ArrayLiteralExpression
external val createObjectLiteral: (properties: Array<out ObjectLiteralElementLike>? = definedExternally, multiLine: Boolean? = definedExternally) -> ObjectLiteralExpression
external val updateObjectLiteral: (node: ObjectLiteralExpression, properties: Array<out ObjectLiteralElementLike>) -> ObjectLiteralExpression
external val createPropertyAccess: (expression: Expression, name: /* string | MemberName */) -> PropertyAccessExpression
external val updatePropertyAccess: (node: PropertyAccessExpression, expression: Expression, name: MemberName) -> PropertyAccessExpression
external val createPropertyAccessChain: (expression: Expression, questionDotToken: QuestionDotToken?, name: /* string | MemberName */) -> PropertyAccessChain
external val updatePropertyAccessChain: (node: PropertyAccessChain, expression: Expression, questionDotToken: QuestionDotToken?, name: MemberName) -> PropertyAccessChain
external val createElementAccess: (expression: Expression, index: /* number | Expression */) -> ElementAccessExpression
external val updateElementAccess: (node: ElementAccessExpression, expression: Expression, argumentExpression: Expression) -> ElementAccessExpression
external val createElementAccessChain: (expression: Expression, questionDotToken: QuestionDotToken?, index: /* number | Expression */) -> ElementAccessChain
external val updateElementAccessChain: (node: ElementAccessChain, expression: Expression, questionDotToken: QuestionDotToken?, argumentExpression: Expression) -> ElementAccessChain
external val createCall: (expression: Expression, typeArguments: Array<out TypeNode>?, argumentsArray: Array<out Expression>?) -> CallExpression
external val updateCall: (node: CallExpression, expression: Expression, typeArguments: Array<out TypeNode>?, argumentsArray: Array<out Expression>) -> CallExpression
external val createCallChain: (expression: Expression, questionDotToken: QuestionDotToken?, typeArguments: Array<out TypeNode>?, argumentsArray: Array<out Expression>?) -> CallChain
external val updateCallChain: (node: CallChain, expression: Expression, questionDotToken: QuestionDotToken?, typeArguments: Array<out TypeNode>?, argumentsArray: Array<out Expression>) -> CallChain
external val createNew: (expression: Expression, typeArguments: Array<out TypeNode>?, argumentsArray: Array<out Expression>?) -> NewExpression
external val updateNew: (node: NewExpression, expression: Expression, typeArguments: Array<out TypeNode>?, argumentsArray: Array<out Expression>?) -> NewExpression
external val createTypeAssertion: (type: TypeNode, expression: Expression) -> TypeAssertion
external val updateTypeAssertion: (node: TypeAssertion, type: TypeNode, expression: Expression) -> TypeAssertion
external val createParen: (expression: Expression) -> ParenthesizedExpression
external val updateParen: (node: ParenthesizedExpression, expression: Expression) -> ParenthesizedExpression
external val createFunctionExpression: (modifiers: Array<out Modifier>?, asteriskToken: AsteriskToken?, name: /* string | Identifier | undefined */, typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>?, type: TypeNode?, body: Block) -> FunctionExpression
external val updateFunctionExpression: (node: FunctionExpression, modifiers: Array<out Modifier>?, asteriskToken: AsteriskToken?, name: Identifier?, typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode?, body: Block) -> FunctionExpression
external val createDelete: (expression: Expression) -> DeleteExpression
external val updateDelete: (node: DeleteExpression, expression: Expression) -> DeleteExpression
external val createTypeOf: (expression: Expression) -> TypeOfExpression
external val updateTypeOf: (node: TypeOfExpression, expression: Expression) -> TypeOfExpression
external val createVoid: (expression: Expression) -> VoidExpression
external val updateVoid: (node: VoidExpression, expression: Expression) -> VoidExpression
external val createAwait: (expression: Expression) -> AwaitExpression
external val updateAwait: (node: AwaitExpression, expression: Expression) -> AwaitExpression
external val createPrefix: (operator: PrefixUnaryOperator, operand: Expression) -> PrefixUnaryExpression
external val updatePrefix: (node: PrefixUnaryExpression, operand: Expression) -> PrefixUnaryExpression
external val createPostfix: (operand: Expression, operator: PostfixUnaryOperator) -> PostfixUnaryExpression
external val updatePostfix: (node: PostfixUnaryExpression, operand: Expression) -> PostfixUnaryExpression
external val createBinary: (left: Expression, operator: /* BinaryOperator | BinaryOperatorToken */, right: Expression) -> BinaryExpression
external val updateConditional: (node: ConditionalExpression, condition: Expression, questionToken: QuestionToken, whenTrue: Expression, colonToken: ColonToken, whenFalse: Expression) -> ConditionalExpression
external val createTemplateExpression: (head: TemplateHead, templateSpans: Array<out TemplateSpan>) -> TemplateExpression
external val updateTemplateExpression: (node: TemplateExpression, head: TemplateHead, templateSpans: Array<out TemplateSpan>) -> TemplateExpression
external val createTemplateHead: /* {
        (text: string, rawText?: string | undefined, templateFlags?: TokenFlags | undefined): TemplateHead;
        (text: string | undefined, rawText: string, templateFlags?: TokenFlags | undefined): TemplateHead;
    } */
external val createTemplateMiddle: /* {
        (text: string, rawText?: string | undefined, templateFlags?: TokenFlags | undefined): TemplateMiddle;
        (text: string | undefined, rawText: string, templateFlags?: TokenFlags | undefined): TemplateMiddle;
    } */
external val createTemplateTail: /* {
        (text: string, rawText?: string | undefined, templateFlags?: TokenFlags | undefined): TemplateTail;
        (text: string | undefined, rawText: string, templateFlags?: TokenFlags | undefined): TemplateTail;
    } */
external val createNoSubstitutionTemplateLiteral: /* {
        (text: string, rawText?: string | undefined): NoSubstitutionTemplateLiteral;
        (text: string | undefined, rawText: string): NoSubstitutionTemplateLiteral;
    } */
external val updateYield: (node: YieldExpression, asteriskToken: AsteriskToken?, expression: Expression?) -> YieldExpression
external val createSpread: (expression: Expression) -> SpreadElement
external val updateSpread: (node: SpreadElement, expression: Expression) -> SpreadElement
external val createOmittedExpression: () -> OmittedExpression
external val createAsExpression: (expression: Expression, type: TypeNode) -> AsExpression
external val updateAsExpression: (node: AsExpression, expression: Expression, type: TypeNode) -> AsExpression
external val createNonNullExpression: (expression: Expression) -> NonNullExpression
external val updateNonNullExpression: (node: NonNullExpression, expression: Expression) -> NonNullExpression
external val createNonNullChain: (expression: Expression) -> NonNullChain
external val updateNonNullChain: (node: NonNullChain, expression: Expression) -> NonNullChain
external val createMetaProperty: (keywordToken: /* SyntaxKind.ImportKeyword | SyntaxKind.NewKeyword */, name: Identifier) -> MetaProperty
external val updateMetaProperty: (node: MetaProperty, name: Identifier) -> MetaProperty
external val createTemplateSpan: (expression: Expression, literal: /* TemplateMiddle | TemplateTail */) -> TemplateSpan
external val updateTemplateSpan: (node: TemplateSpan, expression: Expression, literal: /* TemplateMiddle | TemplateTail */) -> TemplateSpan
external val createSemicolonClassElement: () -> SemicolonClassElement
external val createBlock: (statements: Array<out Statement>, multiLine: Boolean? = definedExternally) -> Block
external val updateBlock: (node: Block, statements: Array<out Statement>) -> Block
external val createVariableStatement: (modifiers: Array<out Modifier>?, declarationList: /* VariableDeclarationList | readonly VariableDeclaration[] */) -> VariableStatement
external val updateVariableStatement: (node: VariableStatement, modifiers: Array<out Modifier>?, declarationList: VariableDeclarationList) -> VariableStatement
external val createEmptyStatement: () -> EmptyStatement
external val createExpressionStatement: (expression: Expression) -> ExpressionStatement
external val updateExpressionStatement: (node: ExpressionStatement, expression: Expression) -> ExpressionStatement
external val createStatement: (expression: Expression) -> ExpressionStatement
external val updateStatement: (node: ExpressionStatement, expression: Expression) -> ExpressionStatement
external val createIf: (expression: Expression, thenStatement: Statement, elseStatement: Statement? = definedExternally) -> IfStatement
external val updateIf: (node: IfStatement, expression: Expression, thenStatement: Statement, elseStatement: Statement?) -> IfStatement
external val createDo: (statement: Statement, expression: Expression) -> DoStatement
external val updateDo: (node: DoStatement, statement: Statement, expression: Expression) -> DoStatement
external val createWhile: (expression: Expression, statement: Statement) -> WhileStatement
external val updateWhile: (node: WhileStatement, expression: Expression, statement: Statement) -> WhileStatement
external val createFor: (initializer: ForInitializer?, condition: Expression?, incrementor: Expression?, statement: Statement) -> ForStatement
external val updateFor: (node: ForStatement, initializer: ForInitializer?, condition: Expression?, incrementor: Expression?, statement: Statement) -> ForStatement
external val createForIn: (initializer: ForInitializer, expression: Expression, statement: Statement) -> ForInStatement
external val updateForIn: (node: ForInStatement, initializer: ForInitializer, expression: Expression, statement: Statement) -> ForInStatement
external val createForOf: (awaitModifier: AwaitKeyword?, initializer: ForInitializer, expression: Expression, statement: Statement) -> ForOfStatement
external val updateForOf: (node: ForOfStatement, awaitModifier: AwaitKeyword?, initializer: ForInitializer, expression: Expression, statement: Statement) -> ForOfStatement
external val createContinue: (label: /* string | Identifier | undefined */ = definedExternally) -> ContinueStatement
external val updateContinue: (node: ContinueStatement, label: Identifier?) -> ContinueStatement
external val createBreak: (label: /* string | Identifier | undefined */ = definedExternally) -> BreakStatement
external val updateBreak: (node: BreakStatement, label: Identifier?) -> BreakStatement
external val createReturn: (expression: Expression? = definedExternally) -> ReturnStatement
external val updateReturn: (node: ReturnStatement, expression: Expression?) -> ReturnStatement
external val createWith: (expression: Expression, statement: Statement) -> WithStatement
external val updateWith: (node: WithStatement, expression: Expression, statement: Statement) -> WithStatement
external val createSwitch: (expression: Expression, caseBlock: CaseBlock) -> SwitchStatement
external val updateSwitch: (node: SwitchStatement, expression: Expression, caseBlock: CaseBlock) -> SwitchStatement
external val createLabel: (label: /* string | Identifier */, statement: Statement) -> LabeledStatement
external val updateLabel: (node: LabeledStatement, label: Identifier, statement: Statement) -> LabeledStatement
external val createThrow: (expression: Expression) -> ThrowStatement
external val updateThrow: (node: ThrowStatement, expression: Expression) -> ThrowStatement
external val createTry: (tryBlock: Block, catchClause: CatchClause?, finallyBlock: Block?) -> TryStatement
external val updateTry: (node: TryStatement, tryBlock: Block, catchClause: CatchClause?, finallyBlock: Block?) -> TryStatement
external val createDebuggerStatement: () -> DebuggerStatement
external val createVariableDeclarationList: (declarations: Array<out VariableDeclaration>, flags: NodeFlags? = definedExternally) -> VariableDeclarationList
external val updateVariableDeclarationList: (node: VariableDeclarationList, declarations: Array<out VariableDeclaration>) -> VariableDeclarationList
external val createFunctionDeclaration: (decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, asteriskToken: AsteriskToken?, name: /* string | Identifier | undefined */, typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode?, body: Block?) -> FunctionDeclaration
external val updateFunctionDeclaration: (node: FunctionDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, asteriskToken: AsteriskToken?, name: Identifier?, typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode?, body: Block?) -> FunctionDeclaration
external val createClassDeclaration: (decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: /* string | Identifier | undefined */, typeParameters: Array<out TypeParameterDeclaration>?, heritageClauses: Array<out HeritageClause>?, members: Array<out ClassElement>) -> ClassDeclaration
external val updateClassDeclaration: (node: ClassDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: Identifier?, typeParameters: Array<out TypeParameterDeclaration>?, heritageClauses: Array<out HeritageClause>?, members: Array<out ClassElement>) -> ClassDeclaration
external val createInterfaceDeclaration: (decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: /* string | Identifier */, typeParameters: Array<out TypeParameterDeclaration>?, heritageClauses: Array<out HeritageClause>?, members: Array<out TypeElement>) -> InterfaceDeclaration
external val updateInterfaceDeclaration: (node: InterfaceDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: Identifier, typeParameters: Array<out TypeParameterDeclaration>?, heritageClauses: Array<out HeritageClause>?, members: Array<out TypeElement>) -> InterfaceDeclaration
external val createTypeAliasDeclaration: (decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: /* string | Identifier */, typeParameters: Array<out TypeParameterDeclaration>?, type: TypeNode) -> TypeAliasDeclaration
external val updateTypeAliasDeclaration: (node: TypeAliasDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: Identifier, typeParameters: Array<out TypeParameterDeclaration>?, type: TypeNode) -> TypeAliasDeclaration
external val createEnumDeclaration: (decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: /* string | Identifier */, members: Array<out EnumMember>) -> EnumDeclaration
external val updateEnumDeclaration: (node: EnumDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: Identifier, members: Array<out EnumMember>) -> EnumDeclaration
external val createModuleDeclaration: (decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: ModuleName, body: ModuleBody?, flags: NodeFlags? = definedExternally) -> ModuleDeclaration
external val updateModuleDeclaration: (node: ModuleDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, name: ModuleName, body: ModuleBody?) -> ModuleDeclaration
external val createModuleBlock: (statements: Array<out Statement>) -> ModuleBlock
external val updateModuleBlock: (node: ModuleBlock, statements: Array<out Statement>) -> ModuleBlock
external val createCaseBlock: (clauses: Array<out CaseOrDefaultClause>) -> CaseBlock
external val updateCaseBlock: (node: CaseBlock, clauses: Array<out CaseOrDefaultClause>) -> CaseBlock
external val createNamespaceExportDeclaration: (name: /* string | Identifier */) -> NamespaceExportDeclaration
external val updateNamespaceExportDeclaration: (node: NamespaceExportDeclaration, name: Identifier) -> NamespaceExportDeclaration
external val createImportEqualsDeclaration: (decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, isTypeOnly: Boolean, name: /* string | Identifier */, moduleReference: ModuleReference) -> ImportEqualsDeclaration
external val updateImportEqualsDeclaration: (node: ImportEqualsDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, isTypeOnly: Boolean, name: Identifier, moduleReference: ModuleReference) -> ImportEqualsDeclaration
external val createImportDeclaration: (decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, importClause: ImportClause?, moduleSpecifier: Expression) -> ImportDeclaration
external val updateImportDeclaration: (node: ImportDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, importClause: ImportClause?, moduleSpecifier: Expression) -> ImportDeclaration
external val createNamespaceImport: (name: Identifier) -> NamespaceImport
external val updateNamespaceImport: (node: NamespaceImport, name: Identifier) -> NamespaceImport
external val createNamedImports: (elements: Array<out ImportSpecifier>) -> NamedImports
external val updateNamedImports: (node: NamedImports, elements: Array<out ImportSpecifier>) -> NamedImports
external val createImportSpecifier: (propertyName: Identifier?, name: Identifier) -> ImportSpecifier
external val updateImportSpecifier: (node: ImportSpecifier, propertyName: Identifier?, name: Identifier) -> ImportSpecifier
external val createExportAssignment: (decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, isExportEquals: Boolean?, expression: Expression) -> ExportAssignment
external val updateExportAssignment: (node: ExportAssignment, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, expression: Expression) -> ExportAssignment
external val createNamedExports: (elements: Array<out ExportSpecifier>) -> NamedExports
external val updateNamedExports: (node: NamedExports, elements: Array<out ExportSpecifier>) -> NamedExports
external val createExportSpecifier: (propertyName: /* string | Identifier | undefined */, name: /* string | Identifier */) -> ExportSpecifier
external val updateExportSpecifier: (node: ExportSpecifier, propertyName: Identifier?, name: Identifier) -> ExportSpecifier
external val createExternalModuleReference: (expression: Expression) -> ExternalModuleReference
external val updateExternalModuleReference: (node: ExternalModuleReference, expression: Expression) -> ExternalModuleReference
external val createJSDocTypeExpression: (type: TypeNode) -> JSDocTypeExpression
external val createJSDocTypeTag: (tagName: Identifier?, typeExpression: JSDocTypeExpression, comment: /* string | NodeArray<JSDocComment> | undefined */ = definedExternally) -> JSDocTypeTag
external val createJSDocReturnTag: (tagName: Identifier?, typeExpression: JSDocTypeExpression? = definedExternally, comment: /* string | NodeArray<JSDocComment> | undefined */ = definedExternally) -> JSDocReturnTag
external val createJSDocThisTag: (tagName: Identifier?, typeExpression: JSDocTypeExpression, comment: /* string | NodeArray<JSDocComment> | undefined */ = definedExternally) -> JSDocThisTag
external val createJSDocComment: (comment: /* string | NodeArray<JSDocComment> | undefined */ = definedExternally, tags: Array<out JSDocTag>? = definedExternally) -> JSDoc
external val createJSDocParameterTag: (tagName: Identifier?, name: EntityName, isBracketed: Boolean, typeExpression: JSDocTypeExpression? = definedExternally, isNameFirst: Boolean? = definedExternally, comment: /* string | NodeArray<JSDocComment> | undefined */ = definedExternally) -> JSDocParameterTag
external val createJSDocClassTag: (tagName: Identifier?, comment: /* string | NodeArray<JSDocComment> | undefined */ = definedExternally) -> JSDocClassTag
external val createJSDocAugmentsTag: (tagName: Identifier?, className: /* ExpressionWithTypeArguments & {
        readonly expression: Identifier | PropertyAccessEntityNameExpression;
    } */, comment: /* string | NodeArray<JSDocComment> | undefined */ = definedExternally) -> JSDocAugmentsTag
external val createJSDocEnumTag: (tagName: Identifier?, typeExpression: JSDocTypeExpression, comment: /* string | NodeArray<JSDocComment> | undefined */ = definedExternally) -> JSDocEnumTag
external val createJSDocTemplateTag: (tagName: Identifier?, constraint: JSDocTypeExpression?, typeParameters: Array<out TypeParameterDeclaration>, comment: /* string | NodeArray<JSDocComment> | undefined */ = definedExternally) -> JSDocTemplateTag
external val createJSDocTypedefTag: (tagName: Identifier?, typeExpression: /* JSDocTypeLiteral | JSDocTypeExpression | undefined */ = definedExternally, fullName: /* Identifier | JSDocNamespaceDeclaration | undefined */ = definedExternally, comment: /* string | NodeArray<JSDocComment> | undefined */ = definedExternally) -> JSDocTypedefTag
external val createJSDocCallbackTag: (tagName: Identifier?, typeExpression: JSDocSignature, fullName: /* Identifier | JSDocNamespaceDeclaration | undefined */ = definedExternally, comment: /* string | NodeArray<JSDocComment> | undefined */ = definedExternally) -> JSDocCallbackTag
external val createJSDocSignature: (typeParameters: Array<out JSDocTemplateTag>?, parameters: Array<out JSDocParameterTag>, type: JSDocReturnTag? = definedExternally) -> JSDocSignature
external val createJSDocPropertyTag: (tagName: Identifier?, name: EntityName, isBracketed: Boolean, typeExpression: JSDocTypeExpression? = definedExternally, isNameFirst: Boolean? = definedExternally, comment: /* string | NodeArray<JSDocComment> | undefined */ = definedExternally) -> JSDocPropertyTag
external val createJSDocTypeLiteral: (jsDocPropertyTags: Array<out JSDocPropertyLikeTag>? = definedExternally, isArrayType: Boolean? = definedExternally) -> JSDocTypeLiteral
external val createJSDocImplementsTag: (tagName: Identifier?, className: /* ExpressionWithTypeArguments & {
        readonly expression: Identifier | PropertyAccessEntityNameExpression;
    } */, comment: /* string | NodeArray<JSDocComment> | undefined */ = definedExternally) -> JSDocImplementsTag
external val createJSDocAuthorTag: (tagName: Identifier?, comment: /* string | NodeArray<JSDocComment> | undefined */ = definedExternally) -> JSDocAuthorTag
external val createJSDocPublicTag: (tagName: Identifier?, comment: /* string | NodeArray<JSDocComment> | undefined */ = definedExternally) -> JSDocPublicTag
external val createJSDocPrivateTag: (tagName: Identifier?, comment: /* string | NodeArray<JSDocComment> | undefined */ = definedExternally) -> JSDocPrivateTag
external val createJSDocProtectedTag: (tagName: Identifier?, comment: /* string | NodeArray<JSDocComment> | undefined */ = definedExternally) -> JSDocProtectedTag
external val createJSDocReadonlyTag: (tagName: Identifier?, comment: /* string | NodeArray<JSDocComment> | undefined */ = definedExternally) -> JSDocReadonlyTag
external val createJSDocTag: (tagName: Identifier, comment: /* string | NodeArray<JSDocComment> | undefined */ = definedExternally) -> JSDocUnknownTag
external val createJsxElement: (openingElement: JsxOpeningElement, children: Array<out JsxChild>, closingElement: JsxClosingElement) -> JsxElement
external val updateJsxElement: (node: JsxElement, openingElement: JsxOpeningElement, children: Array<out JsxChild>, closingElement: JsxClosingElement) -> JsxElement
external val createJsxSelfClosingElement: (tagName: JsxTagNameExpression, typeArguments: Array<out TypeNode>?, attributes: JsxAttributes) -> JsxSelfClosingElement
external val updateJsxSelfClosingElement: (node: JsxSelfClosingElement, tagName: JsxTagNameExpression, typeArguments: Array<out TypeNode>?, attributes: JsxAttributes) -> JsxSelfClosingElement
external val createJsxOpeningElement: (tagName: JsxTagNameExpression, typeArguments: Array<out TypeNode>?, attributes: JsxAttributes) -> JsxOpeningElement
external val updateJsxOpeningElement: (node: JsxOpeningElement, tagName: JsxTagNameExpression, typeArguments: Array<out TypeNode>?, attributes: JsxAttributes) -> JsxOpeningElement
external val createJsxClosingElement: (tagName: JsxTagNameExpression) -> JsxClosingElement
external val updateJsxClosingElement: (node: JsxClosingElement, tagName: JsxTagNameExpression) -> JsxClosingElement
external val createJsxFragment: (openingFragment: JsxOpeningFragment, children: Array<out JsxChild>, closingFragment: JsxClosingFragment) -> JsxFragment
external val createJsxText: (text: String, containsOnlyTriviaWhiteSpaces: Boolean? = definedExternally) -> JsxText
external val updateJsxText: (node: JsxText, text: String, containsOnlyTriviaWhiteSpaces: Boolean? = definedExternally) -> JsxText
external val createJsxOpeningFragment: () -> JsxOpeningFragment
external val createJsxJsxClosingFragment: () -> JsxClosingFragment
external val updateJsxFragment: (node: JsxFragment, openingFragment: JsxOpeningFragment, children: Array<out JsxChild>, closingFragment: JsxClosingFragment) -> JsxFragment
external val createJsxAttribute: (name: Identifier, initializer: /* StringLiteral | JsxExpression | undefined */) -> JsxAttribute
external val updateJsxAttribute: (node: JsxAttribute, name: Identifier, initializer: /* StringLiteral | JsxExpression | undefined */) -> JsxAttribute
external val createJsxAttributes: (properties: Array<out JsxAttributeLike>) -> JsxAttributes
external val updateJsxAttributes: (node: JsxAttributes, properties: Array<out JsxAttributeLike>) -> JsxAttributes
external val createJsxSpreadAttribute: (expression: Expression) -> JsxSpreadAttribute
external val updateJsxSpreadAttribute: (node: JsxSpreadAttribute, expression: Expression) -> JsxSpreadAttribute
external val createJsxExpression: (dotDotDotToken: DotDotDotToken?, expression: Expression?) -> JsxExpression
external val updateJsxExpression: (node: JsxExpression, expression: Expression?) -> JsxExpression
external val createCaseClause: (expression: Expression, statements: Array<out Statement>) -> CaseClause
external val updateCaseClause: (node: CaseClause, expression: Expression, statements: Array<out Statement>) -> CaseClause
external val createDefaultClause: (statements: Array<out Statement>) -> DefaultClause
external val updateDefaultClause: (node: DefaultClause, statements: Array<out Statement>) -> DefaultClause
external val createHeritageClause: (token: /* SyntaxKind.ExtendsKeyword | SyntaxKind.ImplementsKeyword */, types: Array<out ExpressionWithTypeArguments>) -> HeritageClause
external val updateHeritageClause: (node: HeritageClause, types: Array<out ExpressionWithTypeArguments>) -> HeritageClause
external val createCatchClause: (variableDeclaration: /* string | VariableDeclaration | undefined */, block: Block) -> CatchClause
external val updateCatchClause: (node: CatchClause, variableDeclaration: VariableDeclaration?, block: Block) -> CatchClause
external val createPropertyAssignment: (name: /* string | PropertyName */, initializer: Expression) -> PropertyAssignment
external val updatePropertyAssignment: (node: PropertyAssignment, name: PropertyName, initializer: Expression) -> PropertyAssignment
external val createShorthandPropertyAssignment: (name: /* string | Identifier */, objectAssignmentInitializer: Expression? = definedExternally) -> ShorthandPropertyAssignment
external val updateShorthandPropertyAssignment: (node: ShorthandPropertyAssignment, name: Identifier, objectAssignmentInitializer: Expression?) -> ShorthandPropertyAssignment
external val createSpreadAssignment: (expression: Expression) -> SpreadAssignment
external val updateSpreadAssignment: (node: SpreadAssignment, expression: Expression) -> SpreadAssignment
external val createEnumMember: (name: /* string | PropertyName */, initializer: Expression? = definedExternally) -> EnumMember
external val updateEnumMember: (node: EnumMember, name: PropertyName, initializer: Expression?) -> EnumMember
external val updateSourceFileNode: (node: SourceFile, statements: Array<out Statement>, isDeclarationFile: Boolean? = definedExternally, referencedFiles: Array<out FileReference>? = definedExternally, typeReferences: Array<out FileReference>? = definedExternally, hasNoDefaultLib: Boolean? = definedExternally, libReferences: Array<out FileReference>? = definedExternally) -> SourceFile
external val createNotEmittedStatement: (original: Node) -> NotEmittedStatement
external val createPartiallyEmittedExpression: (expression: Expression, original: Node? = definedExternally) -> PartiallyEmittedExpression
external val updatePartiallyEmittedExpression: (node: PartiallyEmittedExpression, expression: Expression) -> PartiallyEmittedExpression
external val createCommaList: (elements: Array<out Expression>) -> CommaListExpression
external val updateCommaList: (node: CommaListExpression, elements: Array<out Expression>) -> CommaListExpression
external val createBundle: (sourceFiles: Array<out SourceFile>, prepends: Array<out (/* UnparsedSource | InputFiles */)>? = definedExternally) -> Bundle
external val updateBundle: (node: Bundle, sourceFiles: Array<out SourceFile>, prepends: Array<out (/* UnparsedSource | InputFiles */)>? = definedExternally) -> Bundle
external val createImmediatelyInvokedFunctionExpression: /* {
        (statements: readonly Statement[]): CallExpression;
        (statements: readonly Statement[], param: ParameterDeclaration, paramValue: Expression): CallExpression;
    } */
external val createImmediatelyInvokedArrowFunction: /* {
        (statements: readonly Statement[]): CallExpression;
        (statements: readonly Statement[], param: ParameterDeclaration, paramValue: Expression): CallExpression;
    } */
external val createVoidZero: () -> VoidExpression
external val createExportDefault: (expression: Expression) -> ExportAssignment
external val createExternalModuleExport: (exportName: Identifier) -> ExportDeclaration
external val createNamespaceExport: (name: Identifier) -> NamespaceExport
external val updateNamespaceExport: (node: NamespaceExport, name: Identifier) -> NamespaceExport
external val createToken: (vararg args: Any?) -> Any?
external val createIdentifier: (text: String) -> Identifier
external val createTempVariable: (recordTempVariable: ((node: Identifier) -> Unit)?) -> Identifier
external val getGeneratedNameForNode: (node: Node?) -> Identifier
external val createOptimisticUniqueName: (text: String) -> Identifier
external val createFileLevelUniqueName: (text: String) -> Identifier
external val createIndexSignature: (decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, parameters: Array<out ParameterDeclaration>, type: TypeNode) -> IndexSignatureDeclaration
external val createTypePredicateNode: (parameterName: /* Identifier | ThisTypeNode | string */, type: TypeNode) -> TypePredicateNode
external val updateTypePredicateNode: (node: TypePredicateNode, parameterName: /* Identifier | ThisTypeNode */, type: TypeNode) -> TypePredicateNode
external val createLiteral: /* {
        (value: string | StringLiteral | NoSubstitutionTemplateLiteral | NumericLiteral | Identifier): StringLiteral;
        (value: number | PseudoBigInt): NumericLiteral;
        (value: boolean): BooleanLiteral;
        (value: string | number | PseudoBigInt | boolean): PrimaryExpression;
    } */
external val createMethodSignature: (typeParameters: Array<out TypeParameterDeclaration>?, parameters: Array<out ParameterDeclaration>, type: TypeNode?, name: /* string | PropertyName */, questionToken: QuestionToken?) -> MethodSignature
external val updateMethodSignature: (node: MethodSignature, typeParameters: NodeArray<TypeParameterDeclaration>?, parameters: NodeArray<ParameterDeclaration>, type: TypeNode?, name: PropertyName, questionToken: QuestionToken?) -> MethodSignature
external val createTypeOperatorNode: /* {
        (type: TypeNode): TypeOperatorNode;
        (operator: SyntaxKind.KeyOfKeyword | SyntaxKind.UniqueKeyword | SyntaxKind.ReadonlyKeyword, type: TypeNode): TypeOperatorNode;
    } */
external val createTaggedTemplate: /* {
        (tag: Expression, template: TemplateLiteral): TaggedTemplateExpression;
        (tag: Expression, typeArguments: readonly TypeNode[] | undefined, template: TemplateLiteral): TaggedTemplateExpression;
    } */
external val updateTaggedTemplate: /* {
        (node: TaggedTemplateExpression, tag: Expression, template: TemplateLiteral): TaggedTemplateExpression;
        (node: TaggedTemplateExpression, tag: Expression, typeArguments: readonly TypeNode[] | undefined, template: TemplateLiteral): TaggedTemplateExpression;
    } */
external val updateBinary: (node: BinaryExpression, left: Expression, right: Expression, operator: /* BinaryOperator | BinaryOperatorToken */ = definedExternally) -> BinaryExpression
external val createConditional: /* {
        (condition: Expression, whenTrue: Expression, whenFalse: Expression): ConditionalExpression;
        (condition: Expression, questionToken: QuestionToken, whenTrue: Expression, colonToken: ColonToken, whenFalse: Expression): ConditionalExpression;
    } */
external val createYield: /* {
        (expression?: Expression | undefined): YieldExpression;
        (asteriskToken: AsteriskToken | undefined, expression: Expression): YieldExpression;
    } */
external val createClassExpression: (modifiers: Array<out Modifier>?, name: /* string | Identifier | undefined */, typeParameters: Array<out TypeParameterDeclaration>?, heritageClauses: Array<out HeritageClause>?, members: Array<out ClassElement>) -> ClassExpression
external val updateClassExpression: (node: ClassExpression, modifiers: Array<out Modifier>?, name: Identifier?, typeParameters: Array<out TypeParameterDeclaration>?, heritageClauses: Array<out HeritageClause>?, members: Array<out ClassElement>) -> ClassExpression
external val createPropertySignature: (modifiers: Array<out Modifier>?, name: /* PropertyName | string */, questionToken: QuestionToken?, type: TypeNode?, initializer: Expression? = definedExternally) -> PropertySignature
external val updatePropertySignature: (node: PropertySignature, modifiers: Array<out Modifier>?, name: PropertyName, questionToken: QuestionToken?, type: TypeNode?, initializer: Expression?) -> PropertySignature
external val createExpressionWithTypeArguments: (typeArguments: Array<out TypeNode>?, expression: Expression) -> ExpressionWithTypeArguments
external val updateExpressionWithTypeArguments: (node: ExpressionWithTypeArguments, typeArguments: Array<out TypeNode>?, expression: Expression) -> ExpressionWithTypeArguments
external val createArrowFunction: /* {
        (modifiers: readonly Modifier[] | undefined, typeParameters: readonly TypeParameterDeclaration[] | undefined, parameters: readonly ParameterDeclaration[], type: TypeNode | undefined, equalsGreaterThanToken: EqualsGreaterThanToken | undefined, body: ConciseBody): ArrowFunction;
        (modifiers: readonly Modifier[] | undefined, typeParameters: readonly TypeParameterDeclaration[] | undefined, parameters: readonly ParameterDeclaration[], type: TypeNode | undefined, body: ConciseBody): ArrowFunction;
    } */
external val updateArrowFunction: /* {
        (node: ArrowFunction, modifiers: readonly Modifier[] | undefined, typeParameters: readonly TypeParameterDeclaration[] | undefined, parameters: readonly ParameterDeclaration[], type: TypeNode | undefined, equalsGreaterThanToken: EqualsGreaterThanToken, body: ConciseBody): ArrowFunction;
        (node: ArrowFunction, modifiers: readonly Modifier[] | undefined, typeParameters: readonly TypeParameterDeclaration[] | undefined, parameters: readonly ParameterDeclaration[], type: TypeNode | undefined, body: ConciseBody): ArrowFunction;
    } */
external val createVariableDeclaration: /* {
        (name: string | BindingName, type?: TypeNode | undefined, initializer?: Expression | undefined): VariableDeclaration;
        (name: string | BindingName, exclamationToken: ExclamationToken | undefined, type: TypeNode | undefined, initializer: Expression | undefined): VariableDeclaration;
    } */
external val updateVariableDeclaration: /* {
        (node: VariableDeclaration, name: BindingName, type: TypeNode | undefined, initializer: Expression | undefined): VariableDeclaration;
        (node: VariableDeclaration, name: BindingName, exclamationToken: ExclamationToken | undefined, type: TypeNode | undefined, initializer: Expression | undefined): VariableDeclaration;
    } */
external val createImportClause: (name: Identifier?, namedBindings: NamedImportBindings?, isTypeOnly: Any? = definedExternally) -> ImportClause
external val updateImportClause: (node: ImportClause, name: Identifier?, namedBindings: NamedImportBindings?, isTypeOnly: Boolean) -> ImportClause
external val createExportDeclaration: (decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, exportClause: NamedExportBindings?, moduleSpecifier: Expression? = definedExternally, isTypeOnly: Any? = definedExternally) -> ExportDeclaration
external val updateExportDeclaration: (node: ExportDeclaration, decorators: Array<out Decorator>?, modifiers: Array<out Modifier>?, exportClause: NamedExportBindings?, moduleSpecifier: Expression?, isTypeOnly: Boolean) -> ExportDeclaration
external val createJSDocParamTag: (name: EntityName, isBracketed: Boolean, typeExpression: JSDocTypeExpression? = definedExternally, comment: String? = definedExternally) -> JSDocParameterTag
external val createComma: (left: Expression, right: Expression) -> Expression
external val createLessThan: (left: Expression, right: Expression) -> Expression
external val createAssignment: (left: Expression, right: Expression) -> BinaryExpression
external val createStrictEquality: (left: Expression, right: Expression) -> BinaryExpression
external val createStrictInequality: (left: Expression, right: Expression) -> BinaryExpression
external val createAdd: (left: Expression, right: Expression) -> BinaryExpression
external val createSubtract: (left: Expression, right: Expression) -> BinaryExpression
external val createLogicalAnd: (left: Expression, right: Expression) -> BinaryExpression
external val createLogicalOr: (left: Expression, right: Expression) -> BinaryExpression
external val createPostfixIncrement: (operand: Expression) -> PostfixUnaryExpression
external val createLogicalNot: (operand: Expression) -> PrefixUnaryExpression
external val createNode: (kind: SyntaxKind, pos: Any? = definedExternally, end: Any? = definedExternally) -> Node
external val getMutableClone: (vararg args: Any?) -> Any?
external val isTypeAssertion: (node: Node) -> Boolean
external val isIdentifierOrPrivateIdentifier: (node: Node) -> Boolean
/* export = ts; */