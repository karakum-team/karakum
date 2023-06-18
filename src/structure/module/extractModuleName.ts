import path from "path";
import {Configuration} from "../../configuration/configuration";
import {removePrefix} from "../removePrefix";

function generateRelativeFileName(prefixes: string[], sourceFileName: string) {
    return removePrefix(sourceFileName, prefixes)
        .replace(/\.d\.ts$/, "")
        .replace(/\.ts$/, "")
}

export function extractModuleName(
    prefixes: string[],
    sourceFileName: string,
    configuration: Configuration,
) {
    const libraryName = configuration.libraryName ?? ""

    const relativeFileName = generateRelativeFileName(prefixes, sourceFileName)

    return path.join(libraryName, relativeFileName)
}
