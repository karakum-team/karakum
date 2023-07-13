import path from "node:path";
import {Configuration} from "../../configuration/configuration.js";
import {applyMapper} from "../../utils/fileName.js";
import {moduleNameToPackage} from "../module/moduleNameToPackage.js";
import {packageToFileName} from "./packageToFileName.js";

export interface PackageMappingResult {
    fileName: string
    package: string[],
}

export function applyPackageNameMapper(
    packageChunks: string[],
    fileName: string,
    configuration: Configuration,
): PackageMappingResult {
    const {packageNameMapper} = configuration

    const filePath = packageToFileName(packageChunks, fileName)

    const mappedFilePath = applyMapper(filePath, packageNameMapper)

    const dirName = path.dirname(mappedFilePath)
    const baseName = path.basename(mappedFilePath)

    return {
        fileName: baseName,
        package: moduleNameToPackage(dirName)
    }
}
