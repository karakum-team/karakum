package io.github.sgrishchenko.karakum.structure

import io.github.sgrishchenko.karakum.configuration.Configuration
import io.github.sgrishchenko.karakum.configuration.ConflictResolutionStrategy
import io.github.sgrishchenko.karakum.extension.DerivedFile
import io.github.sgrishchenko.karakum.extension.GeneratedFile
import io.github.sgrishchenko.karakum.structure.`package`.packageToOutputFileName
import io.github.sgrishchenko.karakum.util.normalizeItems
import io.github.sgrishchenko.karakum.util.toPosix
import js.array.ReadonlyArray
import js.objects.Object
import js.objects.ReadonlyRecord
import kotlinx.js.JsPlainObject
import node.path.path

@JsPlainObject
external interface TargetFile : StructureItem {
    val body: String
}

private fun resolveConflictResolutionStrategy(
    outputFileName: String,
    configuration: Configuration,
): ConflictResolutionStrategy? {
    val conflictResolutionStrategy = configuration.conflictResolutionStrategy

    for ((pattern, strategy) in Object.entries(conflictResolutionStrategy)) {
        val regexp = pattern.toRegex()

        if (regexp.containsMatchIn(outputFileName)) {
            return strategy
        }
    }

    return null
}

private fun <T> handleConflicts(
    conflicts: ReadonlyRecord<String, ReadonlyArray<T>>,
    type: String,
    printItemInfo: (item: T) -> String,
) {
    if (Object.keys(conflicts).isNotEmpty()) {
        for ((outputFileName, items) in Object.entries(conflicts)) {
            console.error("Conflicting $type File: ${toPosix(outputFileName)}")

            for ((index, item) in items.withIndex()) {
                console.error("--- Item #${index + 1} ---")
                console.error(printItemInfo(item))
            }
        }

        error("There are conflicts in file names")
    }
}

private fun resolveTargetFileConflicts(
    targetFiles: ReadonlyArray<TargetFile>,
    configuration: Configuration,
): ReadonlyArray<TargetFile> {
    val normalizedTargetFiles = normalizeItems(
        targetFiles,
        { item ->
            packageToOutputFileName(
                item.`package`,
                item.fileName,
                configuration
            )
        },
        merge@{ outputFileName, item, other ->
            if (item.body == "") {
                return@merge other
            }

            if (other.body == "") {
                return@merge item
            }

            val conflictResolutionStrategy = resolveConflictResolutionStrategy(outputFileName, configuration)
                ?: ConflictResolutionStrategy.join

            if (conflictResolutionStrategy == ConflictResolutionStrategy.error) {
                return@merge null
            }

            if (conflictResolutionStrategy == ConflictResolutionStrategy.replace) {
                // target files cannot be replaced
                return@merge null
            }

            if (conflictResolutionStrategy == ConflictResolutionStrategy.join) {
                if (
                    item.moduleName !== other.moduleName
                    || item.qualifier !== other.qualifier
                ) {
                    // items are incompatible
                    return@merge null
                }

                return@merge TargetFile(
                    fileName = item.fileName,
                    `package` = item.`package`,
                    moduleName = item.moduleName,
                    qualifier = item.qualifier,
                    hasRuntime = item.hasRuntime || other.hasRuntime,
                    imports = item.imports,

                    body = "${item.body}\n\n${other.body}",
                )
            }

            error("Unknown conflict resolution strategy: $conflictResolutionStrategy")
        }
    )

    handleConflicts(normalizedTargetFiles.conflicts, "Target") { item ->
        val module = "Module: ${item.moduleName}"
        val qualifier = if (item.qualifier != null) "\nQualifier: ${item.qualifier}" else ""
        "${module}${qualifier}\nContent:\n${item.body}"
    }

    return normalizedTargetFiles.items
}

private fun resolvePrimaryFileConflicts(
    primaryFiles: ReadonlyArray<GeneratedFile>,
    configuration: Configuration
): ReadonlyArray<GeneratedFile> {
    val normalizedPrimaryFiles = normalizeItems(
        primaryFiles,
        { item -> item.fileName },
        merge@{ outputFileName, item, other ->
            if (item.body == "") {
                return@merge other
            }

            if (other.body == "") {
                return@merge item
            }

            val conflictResolutionStrategy = resolveConflictResolutionStrategy(outputFileName, configuration)
                ?: ConflictResolutionStrategy.error

            if (conflictResolutionStrategy == ConflictResolutionStrategy.error) {
                return@merge null
            }

            if (conflictResolutionStrategy == ConflictResolutionStrategy.replace) {
                // primary files cannot be replaced
                return@merge null
            }

            if (conflictResolutionStrategy == ConflictResolutionStrategy.join) {
                // primary files cannot be merged
                return@merge null
            }

            error("Unknown conflict resolution strategy: $conflictResolutionStrategy")
        }
    )

    handleConflicts(normalizedPrimaryFiles.conflicts, "Primary") { item ->
        "Content:\n${item.body}"
    }

    return normalizedPrimaryFiles.items
}

private fun resolveDerivedFilesConflicts(
    primaryFiles: ReadonlyArray<DerivedFile>,
    configuration: Configuration,
): ReadonlyArray<DerivedFile> {
    val normalizedDerivedFiles = normalizeItems(
        primaryFiles,
        { item ->
            packageToOutputFileName(
                item.`package`,
                item.fileName,
                configuration
            )
        },
        merge@{ outputFileName, item, other ->
            if (item.body == "") {
                return@merge other
            }

            if (other.body == "") {
                return@merge item
            }

            val conflictResolutionStrategy = resolveConflictResolutionStrategy(outputFileName, configuration)
                ?: ConflictResolutionStrategy.join

            if (conflictResolutionStrategy == ConflictResolutionStrategy.error) {
                return@merge null
            }

            if (conflictResolutionStrategy == ConflictResolutionStrategy.replace) {
                if (item.body !== other.body) return@merge null
                return@merge other
            }

            if (conflictResolutionStrategy == ConflictResolutionStrategy.join) {
                return@merge DerivedFile(
                    fileName = item.fileName,
                    `package` = item.`package`,
                    imports = item.imports,

                    body = "${item.body}\n\n${other.body}",
                )
            }

            error("Unknown conflict resolution strategy: $conflictResolutionStrategy")
        }
    )

    handleConflicts(normalizedDerivedFiles.conflicts, "Derived") { item ->
        "Content:\n${item.body}"
    }

    return normalizedDerivedFiles.items
}

private fun resolveCompoundFilesConflicts(
    compoundFiles: ReadonlyArray<GeneratedFile>,
    configuration: Configuration,
): ReadonlyArray<GeneratedFile> {
    val normalizedCompoundFiles = normalizeItems(
        compoundFiles,
        { item -> item.fileName },
        merge@{ outputFileName, item, other ->
            if (item.body === "") {
                return@merge other
            }

            if (other.body === "") {
                return@merge item
            }

            val conflictResolutionStrategy = resolveConflictResolutionStrategy(outputFileName, configuration)
                ?: ConflictResolutionStrategy.join

            if (conflictResolutionStrategy == ConflictResolutionStrategy.error) {
                return@merge null
            }

            if (conflictResolutionStrategy == ConflictResolutionStrategy.replace) {
                // auxiliary file can't replace primary file
                return@merge GeneratedFile(
                    fileName = item.fileName,
                    body = "${item.body}${other.body}",
                )
            }

            if (conflictResolutionStrategy == ConflictResolutionStrategy.join) {
                return@merge GeneratedFile(
                    fileName = item.fileName,
                    body = "${item.body}${other.body}",
                )
            }

            error("Unknown conflict resolution strategy: $conflictResolutionStrategy")
        }
    )

    handleConflicts(normalizedCompoundFiles.conflicts, "Compound") { item ->
        "Content:\n${item.body}"
    }

    return normalizedCompoundFiles.items
}

fun resolveConflicts(
    targetFiles: ReadonlyArray<TargetFile>,
    derivedFiles: ReadonlyArray<DerivedFile>,
    generatedFiles: ReadonlyArray<GeneratedFile>,
    configuration: Configuration,
): ReadonlyArray<GeneratedFile> {
    val output = configuration.output

    val normalizedTargetFiles = resolveTargetFileConflicts(targetFiles, configuration)

    val primaryFiles = normalizedTargetFiles
        .map { item ->
            val outputFileName = packageToOutputFileName(
                item.`package`,
                item.fileName,
                configuration
            )

            val fileName = path.resolve(output, outputFileName)
            val body = createTargetFile(item, item.body, configuration)

            GeneratedFile(
                fileName = fileName,
                body = body,
            )
        }
        .plus(generatedFiles)
        .toTypedArray()

    val normalizedPrimaryFiles = resolvePrimaryFileConflicts(primaryFiles, configuration)

    val primaryFileNames = normalizedPrimaryFiles.map { it.fileName }.toSet()

    val normalizedDerivedFiles = resolveDerivedFilesConflicts(derivedFiles, configuration)

    val standaloneDerivedFiles = mutableListOf<GeneratedFile>()
    val auxiliaryDerivedFiles = mutableListOf<GeneratedFile>()

    for (item in normalizedDerivedFiles) {
        val outputFileName = packageToOutputFileName(
            item.`package`,
            item.fileName,
            configuration
        )

        val fileName = path.resolve(output, outputFileName)

        if (fileName in primaryFileNames) {
            auxiliaryDerivedFiles += GeneratedFile(
                fileName = fileName,
                body = item.body,
            )
        } else {
            val body = createGeneratedFile(
                item.`package`,
                item.fileName,
                item.imports,
                item.body,
                configuration
            )

            standaloneDerivedFiles += GeneratedFile(
                fileName = fileName,
                body = body,
            )
        }
    }

    val compoundFiles = primaryFiles + auxiliaryDerivedFiles

    val normalizedCompoundFiles = resolveCompoundFilesConflicts(compoundFiles, configuration)

    for (item in normalizedCompoundFiles) {
        console.log("Target file: ${toPosix(item.fileName)}")
    }

    for (item in standaloneDerivedFiles) {
        console.log("Generated file: ${toPosix(item.fileName)}")
    }

    return normalizedCompoundFiles + standaloneDerivedFiles
}
