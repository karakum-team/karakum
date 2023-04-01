import path from "path";
import {snakeToCamelCase} from "../utils/strings";
import {applyMapper, dirNameToPackage} from "../utils/fileName";
import {Configuration} from "../configuration/configuration";
import {generateModuleName} from "./generateModuleName";

export interface OutputFileInfo {
    outputFileName: string,
    packageName: string,
    moduleName: string,
}

function libraryNameToDir(libraryName: string) {
    return libraryName
        .replace(/[-:.]/g, "/")
        .replace(/@/g, "")
}

function generateOutputFileName(prefix: string, sourceFileName: string) {
    return sourceFileName
        .replace(prefix, "")
        .replace(/\.d\.ts$/, ".kt")
        .replace(/\.ts$/, ".kt")
}

export function generateOutputFileInfo(
    prefix: string,
    sourceFileName: string,
    configuration: Configuration,
): OutputFileInfo {
    const libraryName = configuration.libraryName ?? ""
    const libraryNameOutputPrefix = configuration.libraryNameOutputPrefix ?? false
    const packageNameMapper = configuration.packageNameMapper

    const outputFileName = generateOutputFileName(prefix, sourceFileName)
    const preparedOutputFileName = snakeToCamelCase(applyMapper(outputFileName, packageNameMapper))

    const preparedLibraryName = libraryNameToDir(libraryName)
    const prefixedOutputFileName = path.join(preparedLibraryName, preparedOutputFileName)

    const outputDirName = path.dirname(prefixedOutputFileName)
    const packageName = dirNameToPackage(outputDirName)

    const moduleName = generateModuleName(prefix, sourceFileName, configuration)

    return {
        outputFileName: libraryNameOutputPrefix
            ? prefixedOutputFileName
            : preparedOutputFileName,
        packageName,
        moduleName,
    }
}
