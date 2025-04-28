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

    TypeNode,
    UnionTypeNode,
    TypeReferenceNode,

    HeritageClause
} from "typescript"

type Symbol = symbol
export type Many<T> = T | T[]
export type ReadonlyRecord<K extends PropertyKey, V> = Readonly<Record<K, V>>
export type Rule = string | ReadonlyRecord<string, string>
export type JsTuple2<A, B> = [A, B]

export type Granularity = "bundle" | "file" | "top-level"
export type NamespaceStrategy = "ignore" | "object" | "package"
export type ConflictResolutionStrategy = "join" | "replace" | "error"

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

export declare class UnionService {
    readonly uncoveredUnionParents: Map<TsSymbol, Array<string>>
}
