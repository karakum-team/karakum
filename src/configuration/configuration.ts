import {CompilerOptions} from "typescript";

export type Configuration = {
    input: string | string[];
    output: string;

    ignoreInput?: string | string[];
    ignoreOutput?: string | string[];

    libraryName?: string;
    libraryNameOutputPrefix?: boolean;

    granularity?: /* TODO: support "bundle"*/ | "file" | "top-level"

    plugins?: string | string[];

    nameResolvers?: string | string[];

    moduleNameMapper?: Record<string, string>
    packageNameMapper?: Record<string, string>

    importInjector?: Record<string, string[]>

    compilerOptions?: CompilerOptions;

    verbose?: boolean;
}
