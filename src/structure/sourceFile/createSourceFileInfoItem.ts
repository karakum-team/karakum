import {Configuration} from "../../configuration/configuration.js";
import path from "node:path";
import {StructureItem} from "../structure.js";
import {removePrefix} from "../removePrefix.js";
import {moduleNameToPackage} from "../module/moduleNameToPackage.js";
import {extractModuleName} from "../module/extractModuleName.js";
import {camelize} from "../../utils/strings.js";
import {dirNameToPackage} from "../package/packageToFileName.js";

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
    return camelize(
        path.basename(sourceFileName)
            .replace(/\.d\.ts$/, ".kt")
            .replace(/\.ts$/, ".kt")
    )
}

export function createSourceFileInfoItem(
    sourceFileName: string,
    imports: string[],
    configuration: Configuration,
): SourceFileInfoItem {
    const {inputRoots, libraryName} = configuration

    const dirName = extractDirName(inputRoots, sourceFileName)
    const fileName = extractFileName(sourceFileName)

    const packageChunks = [
        ...moduleNameToPackage(libraryName),
        ...dirNameToPackage(dirName)
    ]

    const moduleName = extractModuleName(sourceFileName, configuration)

    const hasRuntime = true

    return {
        fileName,
        package: packageChunks,
        moduleName,
        qualifier: undefined,
        hasRuntime,
        imports,
    }
}
