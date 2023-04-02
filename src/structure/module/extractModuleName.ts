import path from "path";
import {Configuration} from "../../configuration/configuration";

function generateRelativeFileName(prefix: string, sourceFileName: string) {
    return sourceFileName
        .replace(prefix, "")
        .replace(/\.d\.ts$/, "")
        .replace(/\.ts$/, "")
}

export function extractModuleName(
    prefix: string,
    sourceFileName: string,
    configuration: Configuration,
) {
    const libraryName = configuration.libraryName ?? ""

    const relativeFileName = generateRelativeFileName(prefix, sourceFileName)

    return path.join(libraryName, relativeFileName)
}
