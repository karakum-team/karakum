import {CompilerOptions} from "typescript";

export type Configuration = {
    input: string | string[];
    output: string;

    ignore?: string | string[];

    libraryName?: string;
    singlePackage?: boolean;
    // TODO: provide file name mapping

    compilerOptions?: CompilerOptions;

    verbose?: boolean;
}
