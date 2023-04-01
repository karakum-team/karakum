import path from "path";
import {applyMapper} from "../utils/fileName";
import {Configuration} from "../configuration/configuration";

function generateRelativeFileName(prefix: string, sourceFileName: string) {
    return sourceFileName
        .replace(prefix, "")
        .replace(/\.d\.ts$/, "")
        .replace(/\.ts$/, "")
}

export function generateModuleName(
    prefix: string,
    sourceFileName: string,
    configuration: Configuration,
) {
    const libraryName = configuration.libraryName ?? ""
    const moduleNameMapper = configuration.moduleNameMapper

    const relativeFileName = generateRelativeFileName(prefix, sourceFileName)

    const mappedRelativeFileName = applyMapper(relativeFileName, moduleNameMapper)

    return path.join(libraryName, mappedRelativeFileName)
}
