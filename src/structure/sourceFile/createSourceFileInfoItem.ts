import {Configuration} from "../../configuration/configuration";
import path from "path";
import {StructureItem} from "../structure";
import {removePrefix} from "../removePrefix";
import {moduleNameToPackage} from "../module/moduleNameToPackage";
import {extractModuleName} from "../module/extractModuleName";
import {camelize, capitalize} from "../../utils/strings";

export interface SourceFileInfoItem extends StructureItem {
}

function extractDirName(prefixes: string[], sourceFileName: string) {
    const relativeFileName = removePrefix(sourceFileName, prefixes)

    const dirName = path.dirname(relativeFileName)

    if (dirName === ".") {
        return ""
    }

    return dirName
}

function extractFileName(sourceFileName: string) {
    return capitalize(
        camelize(
            path.basename(sourceFileName)
                .replace(/\.d\.ts$/, ".kt")
                .replace(/\.ts$/, ".kt")
                .toLowerCase()
        )
    )
}

export function createSourceFileInfoItem(
    prefixes: string[],
    sourceFileName: string,
    configuration: Configuration,
): SourceFileInfoItem {
    const libraryName = configuration.libraryName ?? ""

    const dirName = extractDirName(prefixes, sourceFileName)
    const fileName = extractFileName(sourceFileName)

    const packageChunks = [
        ...moduleNameToPackage(libraryName),
        ...moduleNameToPackage(dirName)
    ]

    const moduleName = extractModuleName(prefixes, sourceFileName, configuration)

    const hasRuntime = true

    return {
        fileName,
        package: packageChunks,
        moduleName,
        qualifier: undefined,
        hasRuntime,
    }
}
