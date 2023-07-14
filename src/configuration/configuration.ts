import {CompilerOptions} from "typescript"

export type Granularity = "bundle" | "file" | "top-level"

export type NamespaceStrategy = "ignore" | "object" | "package"

export interface PartialConfiguration {
    inputRoots?: string | string[]

    input: string | string[]
    output: string

    ignoreInput?: string | string[]
    ignoreOutput?: string | string[]

    libraryName?: string
    libraryNameOutputPrefix?: boolean

    granularity?: Granularity

    plugins?: string | string[]

    annotations?: string | string[]

    nameResolvers?: string | string[]

    inheritanceModifiers?: string | string[]

    moduleNameMapper?: Record<string, string>
    packageNameMapper?: Record<string, string>

    importInjector?: Record<string, string[]>

    namespaceStrategy?: Record<string, NamespaceStrategy>

    compilerOptions?: CompilerOptions

    verbose?: boolean
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

    compilerOptions: CompilerOptions

    verbose: boolean
}
