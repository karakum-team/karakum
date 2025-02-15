declare module "karakum" {
    export type Many<T> = T | T[]
    export type ReadonlyRecord<K extends PropertyKey, V> = Readonly<Record<K, V>>
    export type Rule = string | ReadonlyRecord<string, string>
    export type JsTuple2<A, B> = [A, B]

    export type Granularity = "bundle" | "file" | "top-level"
    export type NamespaceStrategy = "ignore" | "object" | "package"
    export type ConflictResolutionStrategy = "join" | "replace" | "error"

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

    // TODO: should be removed after JSO fix
    type Configuration = unknown
    type GeneratedFile = unknown
    type DerivedFile = unknown
    type CheckCoverageResult = unknown
    type ParameterDeclarationConfiguration = unknown
    type ParameterDeclarationsConfiguration = unknown
    type ParameterInfo = unknown
    type DerivedDeclaration = unknown
}
