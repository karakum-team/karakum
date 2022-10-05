import {CompilerOptions} from "typescript";

export type Configuration = {
    input: string | string[];
    output: string;

    ignore?: string | string[];

    libraryName?: string;
    compilerOptions?: CompilerOptions;

    verbose?: boolean;
}
