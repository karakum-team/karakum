package io.github.sgrishchenko.karakum.schema

import js.import.import
import js.json.parse
import js.json.stringify
import js.objects.Object
import js.objects.ReadonlyRecord
import js.objects.recordOf
import js.reflect.Reflect
import kotlinx.js.JsPlainObject
import node.buffer.BufferEncoding.Companion.utf8
import node.fs.readFile
import node.fs.rm
import node.fs.writeFile
import node.path.path
import node.process.process
import node.url.fileURLToPath

@JsPlainObject
external interface Schema {
    val `$schema`: String?
    val definitions: ReadonlyRecord<String, Any>?
}

private suspend fun generateSchema(outputFileName: String, typings: String, entity: String) {
    val nodeExecutable = process.argv[0]

    val typescriptJsonSchema = import.meta.resolve("typescript-json-schema/bin/typescript-json-schema")
        .let { fileURLToPath(it) }

    exec("$nodeExecutable $typescriptJsonSchema --titles --noExtraProps --ignoreErrors --out $outputFileName $typings $entity")
}

suspend fun main() {
    val karakum = import.meta.resolve("karakum")
        .let { fileURLToPath(it) }

    val typings = karakum
        .let { path.dirname(it) }
        .let { path.resolve(it, "karakum.d.ts") }

    val additionalTypings = karakum
        .let { path.dirname(it) }
        .let { path.resolve(it, "karakum-types.d.ts") }

    val outputPath = process.argv[2]

    val granularitySchemaFileName = path.resolve(outputPath, "granularity-schema.json")
    val namespaceStrategySchemaFileName = path.resolve(outputPath, "namespace-strategy-schema.json")
    val conflictResolutionStrategySchemaFileName = path.resolve(outputPath, "conflict-resolution-strategy-schema.json")
    val configurationSchemaFileName = path.resolve(outputPath, "karakum-schema.json")

    generateSchema(granularitySchemaFileName, additionalTypings, "Granularity")
    generateSchema(namespaceStrategySchemaFileName, additionalTypings, "NamespaceStrategy")
    generateSchema(conflictResolutionStrategySchemaFileName, additionalTypings, "ConflictResolutionStrategy")
    generateSchema(configurationSchemaFileName, typings, "SchemaConfiguration")

    val granularitySchema: Schema = parse(readFile(granularitySchemaFileName, utf8))
    val namespaceStrategySchema: Schema = parse(readFile(namespaceStrategySchemaFileName, utf8))
    val conflictResolutionStrategySchema: Schema = parse(readFile(conflictResolutionStrategySchemaFileName, utf8))
    val configurationSchema: Schema = parse(readFile(configurationSchemaFileName, utf8))

    val definitions = recordOf(
        "Granularity" to Schema.copy(granularitySchema, `$schema` = undefined),
        "NamespaceStrategy" to Schema.copy(namespaceStrategySchema, `$schema` = undefined),
        "ConflictResolutionStrategy" to Schema.copy(conflictResolutionStrategySchema, `$schema` = undefined),
    )

    val resultConfigurationSchema = Object.assign(
        Schema(`$schema` = configurationSchema.`$schema`),
        Schema(definitions = definitions),
        configurationSchema.apply {
            Reflect.deleteProperty(this, "\$schema")

            Reflect.get(this, "properties")?.let { properties ->
                arrayOf(
                    "inputRoots",
                    "input",
                    "ignoreInput",
                    "ignoreOutput",
                    "plugins",
                    "injections",
                    "annotations",
                    "nameResolvers",
                    "inheritanceModifiers",
                    "varianceModifiers",
                ).forEach {
                    Reflect.get(properties, it)?.let { property ->
                        val anyOf = arrayOf(
                            recordOf("type" to "string"),
                            recordOf(
                                "type" to "array",
                                "items" to recordOf("type" to "string")
                            )
                        )

                        Reflect.set(property, "anyOf", anyOf)
                    }
                }

                Reflect.get(properties, "granularity")?.let { granularity ->
                    Reflect.set(granularity, "type", "string")
                    Reflect.set(granularity, "\$ref", "#/definitions/Granularity")
                }

                Reflect.get(properties, "moduleNameMapper")?.let { moduleNameMapper ->
                    Reflect.set(moduleNameMapper, "type", "object")
                    Reflect.set(moduleNameMapper, "additionalProperties", recordOf("type" to "string"))
                }

                Reflect.get(properties, "packageNameMapper")?.let { packageNameMapper ->
                    Reflect.set(packageNameMapper, "type", "object")
                    Reflect.set(packageNameMapper, "additionalProperties", recordOf("type" to "string"))
                }

                Reflect.get(properties, "importInjector")?.let { importInjector ->
                    val additionalProperties = recordOf(
                        "type" to "array",
                        "items" to recordOf(
                            "type" to "string"
                        )
                    )

                    Reflect.set(importInjector, "type", "object")
                    Reflect.set(importInjector, "additionalProperties", additionalProperties)
                }

                Reflect.get(properties, "importMapper")?.let { importMapper ->
                    val additionalProperties = recordOf(
                        "anyOf" to arrayOf(
                            recordOf("type" to "string"),
                            recordOf(
                                "type" to "object",
                                "additionalProperties" to recordOf("type" to "string")
                            )
                        )
                    )

                    Reflect.set(importMapper, "type", "object")
                    Reflect.set(importMapper, "additionalProperties", additionalProperties)
                }

                Reflect.get(properties, "namespaceStrategy")?.let { namespaceStrategy ->
                    Reflect.set(namespaceStrategy, "type", "object")
                    Reflect.set(namespaceStrategy, "additionalProperties", recordOf("\$ref" to "#/definitions/NamespaceStrategy"))
                }

                Reflect.get(properties, "conflictResolutionStrategy")?.let { conflictResolutionStrategy ->
                    Reflect.set(conflictResolutionStrategy, "type", "object")
                    Reflect.set(conflictResolutionStrategy, "additionalProperties", recordOf("\$ref" to "#/definitions/ConflictResolutionStrategy"))
                }

                Reflect.get(properties, "compilerOptions")?.let { compilerOptions ->
                    Reflect.set(compilerOptions, "type", "object")
                    Reflect.set(compilerOptions, "\$ref", "https://json.schemastore.org/tsconfig#/definitions/compilerOptionsDefinition/properties/compilerOptions")
                    Reflect.set(compilerOptions, "additionalProperties", false)
                }
            }
        }
    )

    rm(granularitySchemaFileName)
    rm(namespaceStrategySchemaFileName)
    rm(conflictResolutionStrategySchemaFileName)
    writeFile(configurationSchemaFileName, stringify(resultConfigurationSchema, { _, value -> value }, 4))
}
