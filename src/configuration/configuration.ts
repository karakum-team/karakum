import {CompilerOptions} from "typescript";

export type Configuration = {
    input: string | string[];
    output: string;

    libraryName?: string;
    compilerOptions?: CompilerOptions;

    verbose?: boolean;
}
