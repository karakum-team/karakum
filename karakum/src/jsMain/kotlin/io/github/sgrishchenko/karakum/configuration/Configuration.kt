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

    val annotations: Many<JsAnnotation>?

    val nameResolvers: Many<JsNameResolver>?

    val inheritanceModifiers: Many<JsInheritanceModifier>?

    val mutabilityModifiers: Many<JsMutabilityModifier>?

    val varianceModifiers: Many<JsVarianceModifier>?

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

interface Configuration {
    val inputRoots: List<String>
    val inputResolutionStrategy: InputResolutionStrategy

    val input: List<String>
    val inputFileNames: List<String>
    val output: String
    val outputFileName: String?

    val ignoreInput: List<String>
    val ignoreOutput: List<String>

    val libraryName: String
    val libraryNameOutputPrefix: Boolean
    val isolatedOutputPackage: Boolean

    val plugins: List<Plugin>

    val injections: List<Injection>

    val annotations: List<Annotation>

    val nameResolvers: List<NameResolver>

    val inheritanceModifiers: List<InheritanceModifier>

    val mutabilityModifiers: List<MutabilityModifier>

    val varianceModifiers: List<VarianceModifier>

    val moduleNameMapper: Map<String, String>
    val packageNameMapper: Map<String, String>

    val importInjector: Map<String, List<String>>
    val importMapper: Map<String, Rule>

    val namespaceStrategy: Map<String, NamespaceStrategy>

    val conflictResolutionStrategy: Map<String, ConflictResolutionStrategy>

    val compilerOptions: CompilerOptions

    val disclaimer: String
    val verbose: Boolean
    val cwd: String
    val inputCwd: String
}
