import path from "path";
import {Configuration} from "../../configuration/configuration";
import {applyMapper} from "../../utils/fileName";
import {moduleNameToPackage} from "../module/moduleNameToPackage";
import {packageToFileName} from "./packageToFileName";

export interface PackageMappingResult {
    fileName: string
    package: string[],
}

export function applyPackageNameMapper(
    packageChunks: string[],
    fileName: string,
    configuration: Configuration,
): PackageMappingResult {
    const packageNameMapper = configuration.packageNameMapper

    const filePath = packageToFileName(packageChunks, fileName)

    const mappedFilePath = applyMapper(filePath, packageNameMapper)

    const dirName = path.dirname(mappedFilePath)
    const baseName = path.dirname(mappedFilePath)

    return {
        fileName: baseName,
        package: moduleNameToPackage(dirName)
    }
}
