@file:OptIn(ExperimentalWasmJsInterop::class)

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

private suspend fun generateSchema(path: String, type: String, out: String) {
    val nodeExecutable = process.argv[0]

    val typescriptJsonSchema = import.meta.resolve("ts-json-schema-generator/bin/ts-json-schema-generator")
        .let { fileURLToPath(it) }

    exec("$nodeExecutable $typescriptJsonSchema --path $path --type $type --out $out --no-type-check --no-top-ref")
}

suspend fun main() {
    val karakum = import.meta.resolve("karakum")
        .let { fileURLToPath(it) }

    val typings = karakum
        .let { path.dirname(it) }
        .let { path.resolve(it, "karakum.d.mts") }

    val additionalTypings = karakum
        .let { path.dirname(it) }
        .let { path.resolve(it, "karakum-types.d.ts") }

    val outputPath = process.argv[2]

    val granularitySchemaFileName = path.resolve(outputPath, "granularity-schema.json")
    val namespaceStrategySchemaFileName = path.resolve(outputPath, "namespace-strategy-schema.json")
    val conflictResolutionStrategySchemaFileName = path.resolve(outputPath, "conflict-resolution-strategy-schema.json")
    val inputResolutionStrategySchemaFileName = path.resolve(outputPath, "input-resolution-strategy-schema.json")
    val configurationSchemaFileName = path.resolve(outputPath, "karakum-schema.json")

    generateSchema(additionalTypings, "Granularity", granularitySchemaFileName)
    generateSchema(additionalTypings, "NamespaceStrategy", namespaceStrategySchemaFileName)
    generateSchema(additionalTypings, "ConflictResolutionStrategy", conflictResolutionStrategySchemaFileName)
    generateSchema(additionalTypings, "InputResolutionStrategy", inputResolutionStrategySchemaFileName)
    generateSchema(typings, "SchemaConfiguration", configurationSchemaFileName)

    val granularitySchema: Schema = parse(readFile(granularitySchemaFileName, utf8))
    val namespaceStrategySchema: Schema = parse(readFile(namespaceStrategySchemaFileName, utf8))
    val conflictResolutionStrategySchema: Schema = parse(readFile(conflictResolutionStrategySchemaFileName, utf8))
    val inputResolutionStrategySchema: Schema = parse(readFile(inputResolutionStrategySchemaFileName, utf8))
    val configurationSchema: Schema = parse(readFile(configurationSchemaFileName, utf8))

    val definitions = recordOf(
        "Granularity" to Schema.copy(granularitySchema, `$schema` = undefined, definitions = undefined),
        "NamespaceStrategy" to Schema.copy(namespaceStrategySchema, `$schema` = undefined, definitions = undefined),
        "ConflictResolutionStrategy" to Schema.copy(conflictResolutionStrategySchema, `$schema` = undefined, definitions = undefined),
        "InputResolutionStrategy" to Schema.copy(inputResolutionStrategySchema, `$schema` = undefined, definitions = undefined),
    )

    val resultConfigurationSchema = Object.assign(
        Schema(`$schema` = configurationSchema.`$schema`),
        Schema(definitions = definitions),
        configurationSchema.apply {
            Reflect.deleteProperty(this, "\$schema")
            Reflect.deleteProperty(this, "definitions")

            Reflect.get(this, "properties")?.let { properties ->
                Reflect.ownKeys(properties).forEach { key ->
                    Reflect.get(properties, key)?.let { property ->
                        Reflect.set(property, "title", key)

                        Reflect.get(property, "type")?.let { type ->
                            if (type is Array<*> && type.size == 2 && type[1] == "null") {
                                Reflect.set(property, "type", type.first())
                            }
                        }
                    }
                }

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
                ).forEach { key ->
                    Reflect.get(properties, key)?.let { property ->
                        Reflect.deleteProperty(property, "\$ref")

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
                    Reflect.deleteProperty(granularity, "anyOf")

                    Reflect.set(granularity, "type", "string")
                    Reflect.set(granularity, "\$ref", "#/definitions/Granularity")
                }

                Reflect.get(properties, "moduleNameMapper")?.let { moduleNameMapper ->
                    Reflect.deleteProperty(moduleNameMapper, "anyOf")

                    Reflect.set(moduleNameMapper, "type", "object")
                    Reflect.set(moduleNameMapper, "additionalProperties", recordOf("type" to "string"))
                }

                Reflect.get(properties, "packageNameMapper")?.let { packageNameMapper ->
                    Reflect.deleteProperty(packageNameMapper, "anyOf")

                    Reflect.set(packageNameMapper, "type", "object")
                    Reflect.set(packageNameMapper, "additionalProperties", recordOf("type" to "string"))
                }

                Reflect.get(properties, "importInjector")?.let { importInjector ->
                    Reflect.deleteProperty(importInjector, "anyOf")

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
                    Reflect.deleteProperty(importMapper, "anyOf")

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
                    Reflect.deleteProperty(namespaceStrategy, "anyOf")

                    Reflect.set(namespaceStrategy, "type", "object")
                    Reflect.set(namespaceStrategy, "additionalProperties", recordOf("\$ref" to "#/definitions/NamespaceStrategy"))
                }

                Reflect.get(properties, "conflictResolutionStrategy")?.let { conflictResolutionStrategy ->
                    Reflect.deleteProperty(conflictResolutionStrategy, "anyOf")

                    Reflect.set(conflictResolutionStrategy, "type", "object")
                    Reflect.set(conflictResolutionStrategy, "additionalProperties", recordOf("\$ref" to "#/definitions/ConflictResolutionStrategy"))
                }

                Reflect.get(properties, "compilerOptions")?.let { compilerOptions ->
                    Reflect.deleteProperty(compilerOptions, "anyOf")

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
    rm(inputResolutionStrategySchemaFileName)
    writeFile(configurationSchemaFileName, stringify(resultConfigurationSchema, { _, value -> value }, 4))
}
