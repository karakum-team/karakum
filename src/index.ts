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
export {inheritanceModifierServiceKey, type InheritanceModifierService} from "./converter/plugins/InheritanceModifierPlugin"

// plugin utils
export {
    type ParameterDeclarationsConfiguration,
    convertParameterDeclarations
} from "./converter/plugins/convertParameterDeclaration"

// controversial plugins
export {
    convertCallableInterfaceDeclaration
} from "./converter/plugins/controversial/convertCallableInterfaceDeclaration"

// render
export type {Render} from "./converter/render"
export {ifPresent} from "./converter/render"

// utils
export {traverse} from "./utils/traverse"
export {findClosest} from "./utils/findClosest"

export {process} from "./process"
