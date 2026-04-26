package io.github.sgrishchenko.karakum.configuration

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.util.Rule
import js.array.ReadonlyArray
import js.objects.ReadonlyRecord
import js.objects.toRecord
import kotlinx.js.JsPlainObject
import typescript.CompilerOptions
import kotlin.String

@JsExport
@JsName("Configuration")
@JsPlainObject
external interface JsConfiguration {
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
    val isolatedOutputPackage: Boolean

    val plugins: ReadonlyArray<JsPlugin>

    val injections: ReadonlyArray<JsInjection>

    val annotations: ReadonlyArray<Annotation>

    val nameResolvers: ReadonlyArray<NameResolver>

    val inheritanceModifiers: ReadonlyArray<InheritanceModifier>

    val mutabilityModifiers: ReadonlyArray<MutabilityModifier>

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

fun Configuration.toJsConfiguration(): JsConfiguration =
    JsConfiguration(
        inputRoots = inputRoots.toTypedArray(),
        inputResolutionStrategy = inputResolutionStrategy,

        input = input.toTypedArray(),
        inputFileNames = inputFileNames.toTypedArray(),
        output = output,
        outputFileName = outputFileName,

        ignoreInput = ignoreInput.toTypedArray(),
        ignoreOutput = ignoreOutput.toTypedArray(),

        libraryName = libraryName,
        libraryNameOutputPrefix = libraryNameOutputPrefix,
        isolatedOutputPackage = isolatedOutputPackage,

        plugins = plugins
            .map { it.toJsPlugin() }
            .toTypedArray(),

        injections = injections
            .map { it.toJsInjection() }
            .toTypedArray(),

        annotations = annotations.toTypedArray(),

        nameResolvers = nameResolvers.toTypedArray(),

        inheritanceModifiers = inheritanceModifiers.toTypedArray(),

        mutabilityModifiers = mutabilityModifiers.toTypedArray(),

        varianceModifiers = varianceModifiers.toTypedArray(),

        moduleNameMapper = moduleNameMapper.toRecord(),
        packageNameMapper = packageNameMapper.toRecord(),

        importInjector = importInjector
            .mapValues { (_, value) -> value.toTypedArray() }
            .toRecord(),
        importMapper = importMapper.toRecord(),

        namespaceStrategy = namespaceStrategy.toRecord(),

        conflictResolutionStrategy = conflictResolutionStrategy.toRecord(),

        compilerOptions = compilerOptions,

        disclaimer = disclaimer,
        verbose = verbose,
        cwd = cwd,
        inputCwd = inputCwd,
    )
