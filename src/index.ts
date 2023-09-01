// configuration
export type {Configuration} from "./configuration/configuration.js"

// context
export type {ConverterContext} from "./converter/context.js"

// plugin
export type {ConverterPlugin, SimpleConverterPlugin} from "./converter/plugin.js"
export {createSimplePlugin} from "./converter/plugin.js"

// annotation
export type {Annotation} from "./converter/annotation.js"

// name resolver
export type {NameResolver} from "./converter/nameResolver.js"

// inheritance modifier
export type {InheritanceModifier} from "./converter/inheritanceModifier.js"

// plugin services
export {
    checkCoverageServiceKey,
    type CheckCoverageService
} from "./converter/plugins/CheckCoveragePlugin.js"
export {
    configurationServiceKey,
    type ConfigurationService
} from "./converter/plugins/ConfigurationPlugin.js"
export {
    typeScriptServiceKey,
    type TypeScriptService
} from "./converter/plugins/TypeScriptPlugin.js"
export {
    nameResolverServiceKey,
    type NameResolverService
} from "./converter/plugins/NameResolverPlugin.js"
export {
    inheritanceModifierServiceKey,
    type InheritanceModifierService
} from "./converter/plugins/InheritanceModifierPlugin.js"
export {
    namespaceInfoServiceKey,
    type NamespaceInfoService
} from "./converter/plugins/NamespaceInfoPlugin.js"
export {
    declarationMergingServiceKey,
    type DeclarationMergingService
} from "./converter/plugins/DeclarationMergingPlugin.js"

// plugin utils
export {
    type ParameterDeclarationsConfiguration,
    convertParameterDeclarations
} from "./converter/plugins/convertParameterDeclaration.js"

// render
export type {Render} from "./converter/render.js"
export {ifPresent, renderNullable} from "./converter/render.js"

// utils
export {traverse} from "./utils/traverse.js"
export {capitalize, camelize, isValidIdentifier, escapeIdentifier, identifier, constIdentifier} from "./utils/strings.js"

// structure
export {applyPackageNameMapper, type PackageMappingResult} from "./structure/package/applyPackageNameMapper.js"
export {applyModuleNameMapper, type ModuleMappingResult} from "./structure/module/applyModuleNameMapper.js"

export {packageToOutputFileName} from "./structure/package/packageToFileName.js"
export {createGeneratedFile} from "./structure/createGeneratedFile.js"

export type {StructureItem} from "./structure/structure.js"
export {createSourceFileInfoItem, type SourceFileInfoItem} from "./structure/sourceFile/createSourceFileInfoItem.js"
export {createNamespaceInfoItem, type NamespaceInfoItem} from "./structure/namespace/createNamespaceInfoItem.js"

export type {DerivedDeclaration} from "./structure/derived/derivedDeclaration.js"
export {generateDerivedDeclarations } from "./structure/derived/generateDerivedDeclarations.js"

export {generate} from "./generate.js"
