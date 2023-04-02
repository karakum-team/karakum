import {Configuration} from "../../configuration/configuration";
import {applyMapper} from "../../utils/fileName";

export interface ModuleMappingResult {
    moduleName: string
}

export function applyModuleNameMapper(
    moduleName: string,
    configuration: Configuration,
): ModuleMappingResult {
    const packageNameMapper = configuration.moduleNameMapper

    return {
        moduleName: applyMapper(moduleName, packageNameMapper)
    }
}
