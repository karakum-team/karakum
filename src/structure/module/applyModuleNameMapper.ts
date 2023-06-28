import {Configuration} from "../../configuration/configuration";
import {applyMapper} from "../../utils/fileName";

export interface ModuleMappingResult {
    moduleName: string
}

export function applyModuleNameMapper(
    moduleName: string,
    configuration: Configuration,
): ModuleMappingResult {
    const {moduleNameMapper} = configuration

    return {
        moduleName: applyMapper(moduleName, moduleNameMapper)
    }
}
