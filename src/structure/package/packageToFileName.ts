import path from "path";
import {Configuration} from "../../configuration/configuration.js";
import {moduleNameToPackage} from "../module/moduleNameToPackage.js";

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
    const {libraryName, libraryNameOutputPrefix} = configuration

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
