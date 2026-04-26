package io.github.sgrishchenko.karakum.configuration

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.util.Many
import io.github.sgrishchenko.karakum.util.Rule
import js.array.ReadonlyArray
import js.objects.ReadonlyRecord
import js.reflect.unsafeCast
import kotlinx.js.JsPlainObject
import typescript.CompilerOptions

sealed external interface NamespaceStrategy {
    companion object
}

inline val NamespaceStrategy.Companion.ignore: NamespaceStrategy
    get() = unsafeCast("ignore")

inline val NamespaceStrategy.Companion.`object`: NamespaceStrategy
    get() = unsafeCast("object")

inline val NamespaceStrategy.Companion.`package`: NamespaceStrategy
    get() = unsafeCast("package")

sealed external interface ConflictResolutionStrategy {
    companion object
}

inline val ConflictResolutionStrategy.Companion.join: ConflictResolutionStrategy
    get() = unsafeCast("join")

inline val ConflictResolutionStrategy.Companion.replace: ConflictResolutionStrategy
    get() = unsafeCast("replace")

inline val ConflictResolutionStrategy.Companion.error: ConflictResolutionStrategy
    get() = unsafeCast("error")

sealed external interface InputResolutionStrategy {
    companion object
}

inline val InputResolutionStrategy.Companion.node: InputResolutionStrategy
    get() = unsafeCast("node")

inline val InputResolutionStrategy.Companion.plain: InputResolutionStrategy
    get() = unsafeCast("plain")

@JsExport
@JsPlainObject
external interface SchemaConfiguration {
    val inputRoots: Many<String>?

    /**
     * @TJS-type string
     * @$ref #/definitions/InputResolutionStrategy
     * */
    val inputResolutionStrategy: InputResolutionStrategy?

    val input: Many<String>?
    val output: String?

    val ignoreInput: Many<String>?
    val ignoreOutput: Many<String>?

    val libraryName: String?
    val libraryNameOutputPrefix: Boolean?
    val isolatedOutputPackage: Boolean?

    val plugins: Many<Any>?

    val injections: Many<Any>?

    val annotations: Many<Any>?

    val nameResolvers: Many<Any>?

    val inheritanceModifiers: Many<Any>?

    val mutabilityModifiers: Many<Any>?

    val varianceModifiers: Many<Any>?

    /**
     * @TJS-type object
     * @additionalProperties { "type": "string" }
     * */
    val moduleNameMapper: ReadonlyRecord<String, String>?

    /**
     * @TJS-type object
     * @additionalProperties { "type": "string" }
     * */
    val packageNameMapper: ReadonlyRecord<String, String>?

    /**
     * @TJS-type object
     * @additionalProperties { "type": "array", "items": { "type": "string" } }
     * */
    val importInjector: ReadonlyRecord<String, ReadonlyArray<String>>?

    /**
     * @TJS-type object
     * @additionalProperties { "anyOf": [ { "type": "string" }, { "type": "object", "additionalProperties": { "type": "string" } } ] }
     * */
    val importMapper: Any?

    /**
     * @TJS-type object
     * @additionalProperties { "$ref": "#/definitions/NamespaceStrategy" }
     * */
    val namespaceStrategy: ReadonlyRecord<String, NamespaceStrategy>?

    /**
     * @TJS-type object
     * @additionalProperties { "$ref": "#/definitions/ConflictResolutionStrategy" }
     * */
    val conflictResolutionStrategy: ReadonlyRecord<String, ConflictResolutionStrategy>?

    /**
     * @TJS-type object
     * @$ref https://json.schemastore.org/tsconfig#/definitions/compilerOptionsDefinition/properties/compilerOptions
     * @additionalProperties false
     * */
    val compilerOptions: Any?

    val disclaimer: String?
    val verbose: Boolean?
    val cwd: String?
}

@JsExport
@JsPlainObject
external interface PartialConfiguration {
    val inputRoots: Many<String>?

    val inputResolutionStrategy: InputResolutionStrategy?

    val input: Many<String>?
    val output: String?

    val ignoreInput: Many<String>?
    val ignoreOutput: Many<String>?

    val libraryName: String?
    val libraryNameOutputPrefix: Boolean?
    val isolatedOutputPackage: Boolean?

    val plugins: Many<JsPlugin>?

    val injections: Many<JsInjection>?

    val annotations: Many<Annotation>?

    val nameResolvers: Many<NameResolver>?

    val inheritanceModifiers: Many<InheritanceModifier>?

    val mutabilityModifiers: Many<MutabilityModifier>?

    val varianceModifiers: Many<VarianceModifier>?

    val moduleNameMapper: ReadonlyRecord<String, String>?

    val packageNameMapper: ReadonlyRecord<String, String>?

    val importInjector: ReadonlyRecord<String, ReadonlyArray<String>>?

    val importMapper: ReadonlyRecord<String, Rule>?

    val namespaceStrategy: ReadonlyRecord<String, NamespaceStrategy>?

    val conflictResolutionStrategy: ReadonlyRecord<String, ConflictResolutionStrategy>?

    val compilerOptions: CompilerOptions?

    val disclaimer: String?
    val verbose: Boolean?
    val cwd: String?
}

interface MutableConfiguration {
    var inputRoots: List<String>?
    var inputResolutionStrategy: InputResolutionStrategy?

    var input: List<String>?
    var output: String?

    var ignoreInput: List<String>?
    var ignoreOutput: List<String>?

    var libraryName: String?
    var libraryNameOutputPrefix: Boolean?
    var isolatedOutputPackage: Boolean?

    var plugins: List<Plugin>?

    var injections: List<Injection>?

    var annotations: List<Annotation>?

    var nameResolvers: List<NameResolver>?

    var inheritanceModifiers: List<InheritanceModifier>?

    var mutabilityModifiers: List<MutabilityModifier>?

    var varianceModifiers: List<VarianceModifier>?

    var moduleNameMapper: Map<String, String>?

    var packageNameMapper: Map<String, String>?

    var importInjector: Map<String, List<String>>?

    var importMapper: Map<String, Rule>?

    var namespaceStrategy: Map<String, NamespaceStrategy>?

    var conflictResolutionStrategy: Map<String, ConflictResolutionStrategy>?

    var compilerOptions: CompilerOptions?

    var disclaimer: String?
    var verbose: Boolean?
    var cwd: String?
}

@JsExport
interface Configuration {
    @JsExport.Ignore
    val inputRoots: List<String>
    @JsName("inputRoots")
    val jsInputRoots: ReadonlyArray<String>
    val inputResolutionStrategy: InputResolutionStrategy

    @JsExport.Ignore
    val input: List<String>
    @JsName("input")
    val jsInput: ReadonlyArray<String>
    @JsExport.Ignore
    val inputFileNames: List<String>
    @JsName("inputFileNames")
    val jsInputFileNames: ReadonlyArray<String>
    val output: String
    val outputFileName: String?

    @JsExport.Ignore
    val ignoreInput: List<String>
    @JsName("ignoreInput")
    val jsIgnoreInput: ReadonlyArray<String>
    @JsExport.Ignore
    val ignoreOutput: List<String>
    @JsName("ignoreOutput")
    val jsIgnoreOutput: ReadonlyArray<String>

    val libraryName: String
    val libraryNameOutputPrefix: Boolean
    val isolatedOutputPackage: Boolean

    @JsExport.Ignore
    val plugins: List<Plugin>
    @JsName("plugins")
    val jsPlugins: ReadonlyArray<JsPlugin>

    @JsExport.Ignore
    val injections: List<Injection>
    @JsName("injections")
    val jsInjections: ReadonlyArray<JsInjection>

    @JsExport.Ignore
    val annotations: List<Annotation>
    @JsName("annotations")
    val jsAnnotations: ReadonlyArray<Annotation>

    @JsExport.Ignore
    val nameResolvers: List<NameResolver>
    @JsName("nameResolvers")
    val jsNameResolvers: ReadonlyArray<NameResolver>

    @JsExport.Ignore
    val inheritanceModifiers: List<InheritanceModifier>
    @JsName("inheritanceModifiers")
    val jsInheritanceModifiers: ReadonlyArray<InheritanceModifier>

    @JsExport.Ignore
    val mutabilityModifiers: List<MutabilityModifier>
    @JsName("mutabilityModifiers")
    val jsMutabilityModifiers: ReadonlyArray<MutabilityModifier>

    @JsExport.Ignore
    val varianceModifiers: List<VarianceModifier>
    @JsName("varianceModifiers")
    val jsVarianceModifiers: ReadonlyArray<VarianceModifier>

    @JsExport.Ignore
    val moduleNameMapper: Map<String, String>
    @JsName("moduleNameMapper")
    val jsModuleNameMapper: ReadonlyRecord<String, String>
    @JsExport.Ignore
    val packageNameMapper: Map<String, String>
    @JsName("packageNameMapper")
    val jsPackageNameMapper: ReadonlyRecord<String, String>

    @JsExport.Ignore
    val importInjector: Map<String, List<String>>
    @JsName("importInjector")
    val jsImportInjector: ReadonlyRecord<String, ReadonlyArray<String>>
    @JsExport.Ignore
    val importMapper: Map<String, Rule>
    @JsName("importMapper")
    val jsImportMapper: ReadonlyRecord<String, Rule>

    @JsExport.Ignore
    val namespaceStrategy: Map<String, NamespaceStrategy>
    @JsName("namespaceStrategy")
    val jsNamespaceStrategy: ReadonlyRecord<String, NamespaceStrategy>

    @JsExport.Ignore
    val conflictResolutionStrategy: Map<String, ConflictResolutionStrategy>
    @JsName("conflictResolutionStrategy")
    val jsConflictResolutionStrategy: ReadonlyRecord<String, ConflictResolutionStrategy>

    val compilerOptions: CompilerOptions

    val disclaimer: String
    val verbose: Boolean
    val cwd: String
    val inputCwd: String
}
