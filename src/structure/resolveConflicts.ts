import path from "node:path";
import {Configuration, ConflictResolutionStrategy} from "../configuration/configuration.js";
import {DerivedFile, GeneratedFile} from "../converter/generated.js";
import {normalizeItems} from "../utils/normalizeItems.js";
import {toPosix} from "../utils/path.js";
import {StructureItem} from "./structure.js";
import {packageToOutputFileName} from "./package/packageToFileName.js";
import {createTargetFile} from "./createTargetFile.js";
import {createGeneratedFile} from "./createGeneratedFile.js";

export interface TargetFile extends StructureItem {
    body: string
}

function resolveConflictResolutionStrategy(
    outputFileName: string,
    configuration: Configuration,
): ConflictResolutionStrategy | undefined {
    const {conflictResolutionStrategy} = configuration

    for (const [pattern, strategy] of Object.entries(conflictResolutionStrategy)) {
        const regexp = new RegExp(pattern)

        if (regexp.test(outputFileName)) {
            return strategy;
        }
    }

    return undefined
}

function handleConflicts<T>(
    conflicts: Record<string, T[]>,
    type: string,
    printItemInfo: (item: T) => string,
) {
    if (Object.keys(conflicts).length) {
        for (const [outputFileName, items] of Object.entries(conflicts)) {
            console.error(`Conflicting ${type} File: ${toPosix(outputFileName)}`);

            for (const index in items) {
                const item = items[index]

                console.error(`--- Item #${Number(index) + 1} ---`);
                console.error(printItemInfo(item))
            }
        }

        throw new Error("There are conflicts in file names")
    }
}

function resolveTargetFileConflicts(targetFiles: TargetFile[], configuration: Configuration): TargetFile[] {
    const normalizedTargetFiles = normalizeItems(
        targetFiles,
        (item) => packageToOutputFileName(
            item.package,
            item.fileName,
            configuration
        ),
        (outputFileName, item, other) => {
            const conflictResolutionStrategy = resolveConflictResolutionStrategy(outputFileName, configuration) ?? "join"

            if (conflictResolutionStrategy === "error") {
                return null
            }

            if (conflictResolutionStrategy === "replace") {
                return other
            }

            if (conflictResolutionStrategy === "join") {
                if (
                    item.moduleName !== other.moduleName
                    || item.qualifier !== other.qualifier
                ) {
                    // items are incompatible
                    return null
                }

                return {
                    ...item,
                    hasRuntime: item.hasRuntime || other.hasRuntime,
                    body: `${item.body}\n\n${other.body}`,
                }
            }

            throw new Error(`Unknown namespace strategy: ${conflictResolutionStrategy}`)
        }
    )

    handleConflicts(normalizedTargetFiles.conflicts, "Target", item => {
        const module = `Module: ${item.moduleName}`
        const qualifier = item.qualifier ? `\nQualifier: ${item.qualifier}` : ""
        return `${module}${qualifier}\nContent:\n${item.body}`
    })

    return normalizedTargetFiles.items
}

function resolvePrimaryFileConflicts(primaryFiles: GeneratedFile[], configuration: Configuration): GeneratedFile[] {
    const normalizedPrimaryFiles = normalizeItems(
        primaryFiles,
        (item) => item.fileName,
        (outputFileName, _, other) => {
            const conflictResolutionStrategy = resolveConflictResolutionStrategy(outputFileName, configuration) ?? "error"

            if (conflictResolutionStrategy === "error") {
                return null
            }

            if (conflictResolutionStrategy === "replace") {
                return other
            }

            if (conflictResolutionStrategy === "join") {
                // primary files can not be merged
                return null
            }

            throw new Error(`Unknown namespace strategy: ${conflictResolutionStrategy}`)
        }
    )

    handleConflicts(normalizedPrimaryFiles.conflicts, "Primary", item => {
        return `Content:\n${item.body}`
    })

    return normalizedPrimaryFiles.items
}

function resolveDerivedFilesConflicts(primaryFiles: DerivedFile[], configuration: Configuration): DerivedFile[] {
    const normalizedDerivedFiles = normalizeItems(
        primaryFiles,
        (item) => packageToOutputFileName(
            item.package,
            item.fileName,
            configuration
        ),
        (outputFileName, item, other) => {
            const conflictResolutionStrategy = resolveConflictResolutionStrategy(outputFileName, configuration) ?? "join"

            if (conflictResolutionStrategy === "error") {
                return null
            }

            if (conflictResolutionStrategy === "replace") {
                return other
            }

            if (conflictResolutionStrategy === "join") {
                return {
                    ...item,
                    body: `${item.body}\n\n${other.body}`,
                }
            }

            throw new Error(`Unknown namespace strategy: ${conflictResolutionStrategy}`)
        }
    )

    handleConflicts(normalizedDerivedFiles.conflicts, "Derived", item => {
        return `Content:\n${item.body}`
    })

    return normalizedDerivedFiles.items
}

function resolveCompoundFilesConflicts(
    compoundFiles: GeneratedFile[],
    configuration: Configuration,
): GeneratedFile[] {
    const normalizedCompoundFiles = normalizeItems(
        compoundFiles,
        (item) => item.fileName,
        (outputFileName, item, other) => {
            const conflictResolutionStrategy = resolveConflictResolutionStrategy(outputFileName, configuration) ?? "join"

            if (conflictResolutionStrategy === "error") {
                return null
            }

            if (conflictResolutionStrategy === "replace") {
                // auxiliary file can't replace primary file
                return {
                    ...item,
                    body: `${item.body}${other.body}`,
                }
            }

            if (conflictResolutionStrategy === "join") {
                return {
                    ...item,
                    body: `${item.body}${other.body}`,
                }
            }

            throw new Error(`Unknown namespace strategy: ${conflictResolutionStrategy}`)
        }
    )

    handleConflicts(normalizedCompoundFiles.conflicts, "Compound", item => {
        return `Content:\n${item.body}`
    })

    return normalizedCompoundFiles.items
}

export function resolveConflicts(
    targetFiles: TargetFile[],
    derivedFiles: DerivedFile[],
    generatedFiles: GeneratedFile[],
    configuration: Configuration,
): GeneratedFile[] {
    const {output} = configuration

    const normalizedTargetFiles = resolveTargetFileConflicts(targetFiles, configuration)

    const primaryFiles: GeneratedFile[] = [
        ...normalizedTargetFiles.map(item => {
            const outputFileName = packageToOutputFileName(
                item.package,
                item.fileName,
                configuration
            )

            const fileName = path.resolve(output, outputFileName)
            const body = createTargetFile(item, item.body, configuration)

            return {
                fileName,
                body,
            }
        }),
        ...generatedFiles,
    ]

    const normalizedPrimaryFiles = resolvePrimaryFileConflicts(primaryFiles, configuration)

    const primaryFileNames = new Set(normalizedPrimaryFiles.map(item => item.fileName))

    const normalizedDerivedFiles = resolveDerivedFilesConflicts(derivedFiles, configuration)

    const standaloneDerivedFiles: GeneratedFile[] = []
    const auxiliaryDerivedFiles: GeneratedFile[] = []

    for (const item of normalizedDerivedFiles) {
        const outputFileName = packageToOutputFileName(
            item.package,
            item.fileName,
            configuration
        )

        const fileName = path.resolve(output, outputFileName)

        if (primaryFileNames.has(fileName)) {
            auxiliaryDerivedFiles.push({
                fileName,
                body: item.body
            })
        } else {
            const body = createGeneratedFile(
                item.package,
                item.fileName,
                item.imports,
                item.body,
                configuration
            )

            standaloneDerivedFiles.push({
                fileName,
                body,
            })
        }
    }

    const compoundFiles = [
        ...primaryFiles,
        ...auxiliaryDerivedFiles,
    ]

    const normalizedCompoundFiles = resolveCompoundFilesConflicts(compoundFiles, configuration)

    for (const item of normalizedCompoundFiles) {
        console.log(`Target file: ${toPosix(item.fileName)}`)
    }

    for (const item of standaloneDerivedFiles) {
        console.log(`Generated file: ${toPosix(item.fileName)}`)
    }

    return [
        ...normalizedCompoundFiles,
        ...standaloneDerivedFiles,
    ]
}
