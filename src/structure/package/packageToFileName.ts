import path from "path";
import {Configuration} from "../../configuration/configuration";
import {moduleNameToPackage} from "../module/moduleNameToPackage";

export function packageToFileName(
    packageChunks: string[],
    fileName: string,
) {
    return path.join(
        ...packageChunks,
        fileName,
    )
}

export function packageToOutputFileName(
    packageChunks: string[],
    fileName: string,
    configuration: Configuration,
) {
    const libraryName = configuration.libraryName ?? ""
    const libraryNameOutputPrefix = configuration.libraryNameOutputPrefix ?? false

    const result = packageToFileName(packageChunks, fileName)

    if (libraryNameOutputPrefix) {
        return result
    }

    const basePackage = moduleNameToPackage(libraryName).join("/") + "/"

    if (!result.startsWith(basePackage)) {
        return result
    }

    return result.replace(basePackage, "")
}
