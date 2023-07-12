import {Configuration} from "../../configuration/configuration.js";
import {applyMapper} from "../../utils/fileName.js";

export interface ModuleMappingResult {
    moduleName: string
    qualifier: string | undefined
}

export function applyModuleNameMapper(
    moduleName: string,
    qualifier: string | undefined,
    configuration: Configuration,
): ModuleMappingResult {
    const {moduleNameMapper} = configuration

    const fullName = [moduleName, qualifier].filter(Boolean).join("#")

    const mappedFullName = applyMapper(fullName, moduleNameMapper)

    const [mappedModuleName, mappedQualifier] = mappedFullName.split("#")

    return {
        moduleName: mappedModuleName,
        qualifier: mappedQualifier,
    }
}
