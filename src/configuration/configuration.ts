import {CompilerOptions} from "typescript"

export type Granularity = "bundle" | "file" | "top-level"

export type NamespaceStrategy = "ignore" | "object" | "package"

export type ConflictResolutionStrategy = "join" | "replace" | "error"

export interface SchemaConfiguration {
    inputRoots?: string | string[]

    input: string | string[]
    output: string

    ignoreInput?: string | string[]
    ignoreOutput?: string | string[]

    libraryName?: string
    libraryNameOutputPrefix?: boolean

    /**
     * @TJS-type string
     * @$ref #/definitions/Granularity
     * */
    granularity?: Granularity

    plugins?: string | string[]

    annotations?: string | string[]

    nameResolvers?: string | string[]

    inheritanceModifiers?: string | string[]

    /**
     * @TJS-type object
     * @additionalProperties { "type": "string" }
     * */
    moduleNameMapper?: Record<string, string>

    /**
     * @TJS-type object
     * @additionalProperties { "type": "string" }
     * */
    packageNameMapper?: Record<string, string>

    /**
     * @TJS-type object
     * @additionalProperties { "type": "array", "items": { "type": "string" } }
     * */
    importInjector?: Record<string, string[]>

    /**
     * @TJS-type object
     * @additionalProperties { "$ref": "#/definitions/NamespaceStrategy" }
     * */
    namespaceStrategy?: Record<string, NamespaceStrategy>

    /**
     * @TJS-type object
     * @additionalProperties { "$ref": "#/definitions/ConflictResolutionStrategy" }
     * */
    conflictResolutionStrategy?: Record<string, ConflictResolutionStrategy>

    /**
     * @TJS-type object
     * @$ref https://json.schemastore.org/tsconfig#/definitions/compilerOptionsDefinition/properties/compilerOptions
     * */
    compilerOptions?: Record<string, unknown>

    verbose?: boolean
    cwd?: string
}

export interface PartialConfiguration extends SchemaConfiguration {
    compilerOptions?: CompilerOptions
}

export interface Configuration extends PartialConfiguration {
    inputRoots: string[]

    input: string[]
    inputFileNames: string[]
    output: string
    outputFileName?: string

    ignoreInput: string[]
    ignoreOutput: string[]

    libraryName: string
    libraryNameOutputPrefix: boolean

    granularity: Granularity

    plugins: string[]

    annotations: string[]

    nameResolvers: string[]

    inheritanceModifiers: string[]

    moduleNameMapper: Record<string, string>
    packageNameMapper: Record<string, string>

    importInjector: Record<string, string[]>

    namespaceStrategy: Record<string, NamespaceStrategy>

    conflictResolutionStrategy: Record<string, ConflictResolutionStrategy>

    compilerOptions: CompilerOptions

    verbose: boolean
    cwd: string
}
