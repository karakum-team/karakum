package io.github.sgrishchenko.karakum.configuration

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.util.Many
import io.github.sgrishchenko.karakum.util.Rule
import js.array.ReadonlyArray
import js.objects.JsPlainObject
import js.objects.ReadonlyRecord
import js.objects.Record
import seskar.js.JsValue
import typescript.CompilerOptions
import typescript.Node

sealed external interface Granularity {
    companion object {
        @JsValue("bundle")
        val bundle: Granularity

        @JsValue("file")
        val file: Granularity

        @JsValue("top-level")
        val topLevel: Granularity
    }
}

sealed external interface NamespaceStrategy {
    companion object {
        @JsValue("ignore")
        val ignore: NamespaceStrategy

        @JsValue("object")
        val `object`: NamespaceStrategy

        @JsValue("package")
        val `package`: NamespaceStrategy
    }
}

sealed external interface ConflictResolutionStrategy {
    companion object {
        @JsValue("join")
        val join: ConflictResolutionStrategy

        @JsValue("replace")
        val replace: ConflictResolutionStrategy

        @JsValue("error")
        val error: ConflictResolutionStrategy
    }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
// TODO: @JsPlainObject
external interface SchemaConfiguration {
    val inputRoots: Many<String>?

    val input: Many<String>
    val output: String

    val ignoreInput: Many<String>?
    val ignoreOutput: Many<String>?

    val libraryName: String?
    val libraryNameOutputPrefix: Boolean?

    /**
     * @TJS-type string
     * @$ref #/definitions/Granularity
     * */
    val granularity: Granularity?

    val extensions: String?

    val plugins: Many<String>?

    val injections: Many<String>?

    val annotations: Many<String>?

    val nameResolvers: Many<String>?

    val inheritanceModifiers: Many<String>?

    val varianceModifiers: Many<String>?

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

@OptIn(ExperimentalJsExport::class)
@JsExport
// TODO: @JsPlainObject
external interface PartialConfiguration : SchemaConfiguration {
    override val importMapper: Record<String, Rule>?
    override val compilerOptions: CompilerOptions?
}

external interface MutableConfiguration : PartialConfiguration {
    override var inputRoots: Many<String>?

    override var input: Many<String>
    override var output: String

    override var ignoreInput: Many<String>?
    override var ignoreOutput: Many<String>?

    override var libraryName: String?
    override var libraryNameOutputPrefix: Boolean?

    override var granularity: Granularity?

    override var extensions: String?

    override var plugins: Many<String>?

    override var injections: Many<String>?

    override var annotations: Many<String>?

    override var nameResolvers: Many<String>?

    override var inheritanceModifiers: Many<String>?

    override var varianceModifiers: Many<String>?

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

// TODO: ticket for JsPlainObject
//@OptIn(ExperimentalJsExport::class)
//@JsExport
@JsPlainObject
external interface Configuration {
    val inputRoots: ReadonlyArray<String>

    val input: ReadonlyArray<String>
    val inputFileNames: ReadonlyArray<String>
    val output: String
    val outputFileName: String?

    val ignoreInput: ReadonlyArray<String>
    val ignoreOutput: ReadonlyArray<String>

    val libraryName: String
    val libraryNameOutputPrefix: Boolean

    val granularity: Granularity

    val plugins: ReadonlyArray<ConverterPlugin<Node>>

    val injections: ReadonlyArray<Injection<Node, Node>>

    val annotations: ReadonlyArray<Annotation<Node>>

    val nameResolvers: ReadonlyArray<NameResolver<Node>>

    val inheritanceModifiers: ReadonlyArray<InheritanceModifier<Node>>

    val varianceModifiers: ReadonlyArray<VarianceModifier<Node>>

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
}
