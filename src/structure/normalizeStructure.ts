import {StructureItem} from "./structure.js";
import {packageToFileName} from "./package/packageToFileName.js";

export function normalizeStructure<T extends StructureItem>(items: T[], merge: (item: T, other: T) => T) {
    const result: Map<string, T> = new Map()

    for (const item of items) {
        const fileName = packageToFileName(item.package, item.fileName)

        const existingItem = result.get(fileName)

        if (!existingItem) {
            result.set(fileName, item)
        } else {
            const mergedItem = merge({
                ...existingItem,
                hasRuntime: existingItem.hasRuntime || item.hasRuntime,
            }, item)

            result.set(fileName, mergedItem)
        }
    }

    return Array.from(result.values())
}
