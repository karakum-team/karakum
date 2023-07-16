import path from "node:path";
import {Configuration} from "../../configuration/configuration.js";
import {moduleNameToPackage} from "../module/moduleNameToPackage.js";
import {removePrefix} from "../removePrefix.js";

export function dirNameToPackage(dirName: string) {
    return dirName
        .split(path.posix.sep)
        .filter(it => it !== "")
}

export function packageToFileName(
    packageChunks: string[],
    fileName: string,
) {
    return path.posix.join(
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

    const basePackage = path.posix.join(...moduleNameToPackage(libraryName)) + path.posix.sep

    return removePrefix(result, [basePackage])
}
