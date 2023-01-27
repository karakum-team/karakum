// configuration
export type {Configuration} from "./configuration/configuration"

// context
export type {ConverterContext} from "./converter/context"

// plugin
export type {ConverterPlugin, SimpleConverterPlugin} from "./converter/plugin"
export {createSimplePlugin} from "./converter/plugin"

// name resolver
export type {NameResolver} from "./converter/nameResolver"

// plugin services
export {checkCoverageServiceKey, type CheckCoverageService} from "./converter/plugins/CheckCoveragePlugin"
export {configurationServiceKey, type ConfigurationService} from "./converter/plugins/ConfigurationPlugin"
export {typeScriptServiceKey, type TypeScriptService} from "./converter/plugins/TypeScriptPlugin"

// plugin utils
export {type ParameterInfo, type Signature, prepareParameters} from "./converter/plugins/prepareParameters"
export {convertParameterDeclarationWithFixedType} from "./converter/plugins/convertParameterDeclaration"

// render
export type {Render} from "./converter/render"
export {ifPresent} from "./converter/render"

export {process} from "./process"
