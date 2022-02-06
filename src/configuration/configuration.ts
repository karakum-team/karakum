import {CompilerOptions} from "typescript";

export type Configuration = {
    input: string | string[];
    output: string;
    compilerOptions?: CompilerOptions;
}
