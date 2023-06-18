import {CompilerOptions} from "typescript";

export type Granularity = /* TODO: support "bundle" | */ "file" | "top-level"

export type NamespaceStrategy = "ignore" | "object" | "package"

export type Configuration = {
    input: string | string[];
    inputRoots?: string | string[],
    output: string;

    ignoreInput?: string | string[];
    ignoreOutput?: string | string[];

    libraryName?: string;
    libraryNameOutputPrefix?: boolean;

    granularity?: Granularity

    plugins?: string | string[];

    annotations?: string | string[];

    nameResolvers?: string | string[];

    inheritanceModifiers?: string | string[];

    moduleNameMapper?: Record<string, string>
    packageNameMapper?: Record<string, string>

    importInjector?: Record<string, string[]>

    namespaceStrategy?: Record<string, NamespaceStrategy>

    compilerOptions?: CompilerOptions;

    verbose?: boolean;
}
