import {Configuration} from "../../configuration/configuration";
import path from "path";
import {StructureItem} from "../structure";
import {moduleNameToPackage} from "../module/moduleNameToPackage";
import {extractModuleName} from "../module/extractModuleName";

export interface SourceFileInfoItem extends StructureItem {
}

function extractDirName(prefix: string, sourceFileName: string) {
    const relativeFileName = sourceFileName.replace(prefix, "")

    const dirName = path.dirname(relativeFileName)

    if (dirName === ".") {
        return ""
    }

    return dirName
}

function extractFileName(sourceFileName: string) {
    return path.basename(sourceFileName)
        .replace(/\.d\.ts$/, ".kt")
        .replace(/\.ts$/, ".kt")
}

export function createSourceFileInfoItem(
    prefix: string,
    sourceFileName: string,
    configuration: Configuration,
): SourceFileInfoItem {
    const libraryName = configuration.libraryName ?? ""

    const dirName = extractDirName(prefix, sourceFileName)
    const fileName = extractFileName(sourceFileName)

    const packageChunks = [
        ...moduleNameToPackage(libraryName),
        ...moduleNameToPackage(dirName)
    ]

    const moduleName = extractModuleName(prefix, sourceFileName, configuration)

    const hasRuntime = true

    return {
        fileName,
        package: packageChunks,
        moduleName,
        qualifier: undefined,
        hasRuntime,
    }
}
