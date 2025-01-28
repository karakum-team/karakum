declare module "karakum" {
    import {CompilerOptions} from "typescript";

    export type Many<T> = T | T[]
    export type ReadonlyRecord<K extends PropertyKey, V> = Readonly<Record<K, V>>
    export type Rule = string | ReadonlyRecord<string, string>

    export type Granularity = "bundle" | "file" | "top-level"
    export type NamespaceStrategy = "ignore" | "object" | "package"
    export type ConflictResolutionStrategy = "join" | "replace" | "error"

    export interface PartialConfiguration {
        readonly compilerOptions?: CompilerOptions | null | undefined;
    }
}
