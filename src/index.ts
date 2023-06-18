// configuration
export type {Configuration} from "./configuration/configuration"

// context
export type {ConverterContext} from "./converter/context"

// plugin
export type {ConverterPlugin, SimpleConverterPlugin} from "./converter/plugin"
export {createSimplePlugin} from "./converter/plugin"

// annotation
export type {Annotation} from "./converter/annotation"

// name resolver
export type {NameResolver} from "./converter/nameResolver"

// inheritance modifier
export type {InheritanceModifier} from "./converter/inheritanceModifier"

// plugin services
export {
    checkCoverageServiceKey,
    type CheckCoverageService
} from "./converter/plugins/CheckCoveragePlugin"
export {
    configurationServiceKey,
    type ConfigurationService
} from "./converter/plugins/ConfigurationPlugin"
export {
    typeScriptServiceKey,
    type TypeScriptService
} from "./converter/plugins/TypeScriptPlugin"
export {
    inheritanceModifierServiceKey,
    type InheritanceModifierService
} from "./converter/plugins/InheritanceModifierPlugin"
export {
    namespaceInfoServiceKey,
    type NamespaceInfoService
} from "./converter/plugins/NamespaceInfoPlugin"
export {
    declarationMergingServiceKey,
    type DeclarationMergingService
} from "./converter/plugins/DeclarationMergingPlugin"

// plugin utils
export {
    type ParameterDeclarationsConfiguration,
    convertParameterDeclarations
} from "./converter/plugins/convertParameterDeclaration"

// render
export type {Render} from "./converter/render"
export {ifPresent} from "./converter/render"

// utils
export {traverse} from "./utils/traverse"
export {findClosest} from "./utils/findClosest"
export {capitalize, camelize, identifier, constIdentifier} from "./utils/strings"

// structure
export {applyPackageNameMapper, type PackageMappingResult} from "./structure/package/applyPackageNameMapper"
export {applyModuleNameMapper, type ModuleMappingResult} from "./structure/module/applyModuleNameMapper"

export {createPackageName} from "./structure/package/createPackageName"

export {packageToOutputFileName} from "./structure/package/packageToFileName"
export {moduleNameToPackage} from "./structure/module/moduleNameToPackage"

export {createGeneratedFile} from "./structure/createGeneratedFile"

export type {StructureItem} from "./structure/structure"
export {createSourceFileInfoItem, SourceFileInfoItem} from "./structure/sourceFile/createSourceFileInfoItem"
export {createNamespaceInfoItem, NamespaceInfoItem} from "./structure/namespace/createNamespaceInfoItem"

export {process} from "./process"
