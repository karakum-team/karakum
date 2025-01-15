package io.github.sgrishchenko.karakum.configuration

import io.github.sgrishchenko.karakum.extension.Annotation
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
    @JsValue("bundle")
    val bundle: Granularity

    @JsValue("file")
    val file: Granularity

    @JsValue("topLevel")
    val topLevel: Granularity
}

sealed external interface NamespaceStrategy {
    @JsValue("ignore")
    val ignore: NamespaceStrategy

    @JsValue("object")
    val `object`: NamespaceStrategy

    @JsValue("package")
    val `package`: NamespaceStrategy
}

sealed external interface ConflictResolutionStrategy {
    @JsValue("join")
    val join: ConflictResolutionStrategy

    @JsValue("replace")
    val replace: ConflictResolutionStrategy

    @JsValue("error")
    val error: ConflictResolutionStrategy
}

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
    val importInjector: ReadonlyRecord<String, Array<ReadonlyArray<String>>>?

    /**
     * @TJS-type object
     * @additionalProperties { "anyOf": [ { "type": "string" }, { "type": "object", "additionalProperties": { "type": "string" } } ] }
     * */
    val importMapper: Record<String, Rule>?

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

external interface PartialConfiguration : SchemaConfiguration {
    override val compilerOptions: CompilerOptions?
}

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

    val plugins: ConverterPlugin[]

    val injections: Injection[]

    val annotations: ReadonlyArray<Annotation<Node>>

    val nameResolvers: NameResolver[]

    val inheritanceModifiers: InheritanceModifier[]

    val varianceModifiers: VarianceModifier[]

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
