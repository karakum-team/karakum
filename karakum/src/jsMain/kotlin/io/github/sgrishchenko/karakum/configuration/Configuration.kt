package io.github.sgrishchenko.karakum.configuration

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.util.Many
import io.github.sgrishchenko.karakum.util.Rule
import js.array.ReadonlyArray
import js.objects.ReadonlyRecord
import js.objects.Record
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

    val plugins: Many<Any>?

    val injections: Many<Any>?

    val annotations: Many<Any>?

    val nameResolvers: Many<Any>?

    val inheritanceModifiers: Many<Any>?

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
external interface PartialConfiguration : SchemaConfiguration {
    override val plugins: Many<Plugin>?

    override val injections: Many<Injection>?

    override val annotations: Many<Annotation>?

    override val nameResolvers: Many<NameResolver>?

    override val inheritanceModifiers: Many<InheritanceModifier>?

    override val varianceModifiers: Many<VarianceModifier>?

    override val importMapper: Record<String, Rule>?
    override val compilerOptions: CompilerOptions?
}

@JsPlainObject
external interface MutableConfiguration : PartialConfiguration {
    override var inputRoots: Many<String>?
    override var inputResolutionStrategy: InputResolutionStrategy?

    override var input: Many<String>
    override var output: String

    override var ignoreInput: Many<String>?
    override var ignoreOutput: Many<String>?

    override var libraryName: String?
    override var libraryNameOutputPrefix: Boolean?

    override var plugins: Many<Plugin>?

    override var injections: Many<Injection>?

    override var annotations: Many<Annotation>?

    override var nameResolvers: Many<NameResolver>?

    override var inheritanceModifiers: Many<InheritanceModifier>?

    override var varianceModifiers: Many<VarianceModifier>?

    override var moduleNameMapper: ReadonlyRecord<String, String>?

    override var packageNameMapper: ReadonlyRecord<String, String>?

    override var importInjector: ReadonlyRecord<String, ReadonlyArray<String>>?

    override var importMapper: Record<String, Rule>?

    override var namespaceStrategy: ReadonlyRecord<String, NamespaceStrategy>?

    override var conflictResolutionStrategy: ReadonlyRecord<String, ConflictResolutionStrategy>?

    override var compilerOptions: CompilerOptions?

    override var disclaimer: String?
    override var verbose: Boolean?
    override var cwd: String?
}

@JsExport
@JsPlainObject
external interface Configuration {
    val inputRoots: ReadonlyArray<String>
    val inputResolutionStrategy: InputResolutionStrategy

    val input: ReadonlyArray<String>
    val inputFileNames: ReadonlyArray<String>
    val output: String
    val outputFileName: String?

    val ignoreInput: ReadonlyArray<String>
    val ignoreOutput: ReadonlyArray<String>

    val libraryName: String
    val libraryNameOutputPrefix: Boolean

    val plugins: ReadonlyArray<Plugin>

    val injections: ReadonlyArray<Injection>

    val annotations: ReadonlyArray<Annotation>

    val nameResolvers: ReadonlyArray<NameResolver>

    val inheritanceModifiers: ReadonlyArray<InheritanceModifier>

    val varianceModifiers: ReadonlyArray<VarianceModifier>

    val moduleNameMapper: ReadonlyRecord<String, String>
    val packageNameMapper: ReadonlyRecord<String, String>

    val importInjector: ReadonlyRecord<String, ReadonlyArray<String>>
    val importMapper: ReadonlyRecord<String, Rule>

    val namespaceStrategy: ReadonlyRecord<String, NamespaceStrategy>

    val conflictResolutionStrategy: ReadonlyRecord<String, ConflictResolutionStrategy>

    val compilerOptions: CompilerOptions

    val disclaimer: String
    val verbose: Boolean
    val cwd: String
    val inputCwd: String
}
