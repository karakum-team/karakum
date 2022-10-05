import {CompilerOptions} from "typescript";

export type Configuration = {
    input: string | string[];
    output: string;

    ignore?: string | string[];

    libraryName?: string;

    moduleNameMapper?: Record<string, string>
    packageNameMapper?: Record<string, string>

    compilerOptions?: CompilerOptions;

    verbose?: boolean;
}
