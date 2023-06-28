import path from "path";
import {Configuration} from "../../configuration/configuration";
import {removePrefix} from "../removePrefix";

function generateRelativeFileName(prefixes: string[], sourceFileName: string) {
    return removePrefix(sourceFileName, prefixes)
        .replace(/\.d\.ts$/, "")
        .replace(/\.ts$/, "")
}

export function extractModuleName(
    sourceFileName: string,
    configuration: Configuration,
) {
    const {inputRoots, libraryName} = configuration

    const relativeFileName = generateRelativeFileName(inputRoots, sourceFileName)

    return path.join(libraryName, relativeFileName)
}
