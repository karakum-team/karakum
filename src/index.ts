// configuration
export type {Configuration} from "./configuration/configuration.js"

// context
export type {ConverterContext} from "./converter/context.js"

// generated
export type {GeneratedFile, DerivedFile} from "./converter/generated.js"
export {isDerivedFile} from "./converter/generated.js"

// plugin
export type {ConverterPlugin, SimpleConverterPlugin} from "./converter/plugin.js"
export {createSimplePlugin} from "./converter/plugin.js"

// injection
export type {Injection, SimpleInjection, InjectionContext} from "./converter/injection.js"
export {InjectionType} from "./converter/injection.js"
export {createSimpleInjection} from "./converter/injection.js"

// annotation
export type {Annotation} from "./converter/annotation.js"

// name resolver
export type {NameResolver} from "./converter/nameResolver.js"

// inheritance modifier
export type {InheritanceModifier, InheritanceModifierContext} from "./converter/inheritanceModifier.js"

// variance modifier
export type {VarianceModifier} from "./converter/varianceModifier.js"

// plugin services
export {
    injectionServiceKey,
    type InjectionService
} from "./converter/plugins/InjectionPlugin.js"
export {
    annotationServiceKey,
    type AnnotationService
} from "./converter/plugins/AnnotationPlugin.js"
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
    varianceModifierServiceKey,
    type VarianceModifierService
} from "./converter/plugins/VarianceModifierPlugin.js"
export {
    importInfoServiceKey,
    type ImportInfoService
} from "./converter/plugins/ImportInfoPlugin.js"
export {
    namespaceInfoServiceKey,
    type NamespaceInfoService
} from "./converter/plugins/NamespaceInfoPlugin.js"
export {
    declarationMergingServiceKey,
    type DeclarationMergingService
} from "./converter/plugins/DeclarationMergingPlugin.js"
export {
    commentServiceKey,
    type CommentService
} from "./converter/plugins/CommentPlugin.js"
export {
    unionServiceKey,
    type UnionService,
    UnionInjection,
} from "./converter/plugins/configurable/UnionInjection.js"

// plugin utils
export {
    type ParameterDeclarationsConfiguration,
    convertParameterDeclarations,
    convertParameterDeclarationWithFixedType,
} from "./converter/plugins/convertParameterDeclaration.js"

export {
    type AnonymousDeclarationContext,
    type AnonymousDeclarationRender,
    type AnonymousDeclarationRenderResult,
    createAnonymousDeclarationPlugin,
} from "./converter/plugins/AnonymousDeclarationPlugin.js"

export {
    type TypeParameterExtractionResult,
    extractTypeParameters,
    renderDeclaration,
    renderReference,
} from "./converter/extractTypeParameters.js"

// render
export type {Render} from "./converter/render.js"
export {ifPresent, renderNullable} from "./converter/render.js"

// utils
export {DeepMap} from "./utils/deepMap.js"
export {traverse} from "./utils/traverse.js"
export {
    capitalize,
    camelize,
    isValidIdentifier,
    escapeIdentifier,
    identifier,
    constIdentifier,
} from "./utils/strings.js"

// structure
export {applyPackageNameMapper, type PackageMappingResult} from "./structure/package/applyPackageNameMapper.js"
export {applyModuleNameMapper, type ModuleMappingResult} from "./structure/module/applyModuleNameMapper.js"

export {packageToOutputFileName} from "./structure/package/packageToFileName.js"
export {createGeneratedFile} from "./structure/createGeneratedFile.js"

export type {StructureItem} from "./structure/structure.js"
export {createSourceFileInfoItem, type SourceFileInfoItem} from "./structure/sourceFile/createSourceFileInfoItem.js"
export {createNamespaceInfoItem, type NamespaceInfoItem} from "./structure/namespace/createNamespaceInfoItem.js"

export type {DerivedDeclaration} from "./structure/derived/derivedDeclaration.js"
export {generateDerivedDeclarations} from "./structure/derived/generateDerivedDeclarations.js"

export {generate} from "./generate.js"
