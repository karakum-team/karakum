import {
    CompilerOptions,
    Program,
    SourceFile,
    CommentRange,
    Symbol as TsSymbol,

    Declaration,
    NamedDeclaration,
    ModuleDeclaration,
    ParameterDeclaration,
    TypeParameterDeclaration,
    SignatureDeclarationBase,

    Node,
    TypeNode,
    UnionTypeNode,
    TypeReferenceNode,

    HeritageClause
} from "typescript"

type Symbol = symbol
export type Many<T> = T | T[]
export type ReadonlyRecord<K extends PropertyKey, V> = Readonly<Record<K, V>>
export type Rule = string | ReadonlyRecord<string, string>
export type Tuple2<A, B> = [A, B]

export type Granularity = "bundle" | "file" | "top-level"
export type NamespaceStrategy = "ignore" | "object" | "package"
export type ConflictResolutionStrategy = "join" | "replace" | "error"
export type InputResolutionStrategy = "node" | "plain"

export type ParameterDeclarationStrategy = "function" | "lambda"

export type AnonymousDeclaration = string | {
    name: string,
    declaration: string,
    reference: string,
}

export type InjectionType =
    | "MEMBER"
    | "STATIC_MEMBER"

    | "PARAMETER"
    | "TYPE_PARAMETER"

    | "HERITAGE_CLAUSE"

export type Render<TNode extends Node> = (node: TNode) => string

export declare interface UnionService {
    readonly uncoveredUnionParents: Map<TsSymbol, Array<string>>
}
