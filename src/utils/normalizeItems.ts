export interface NormalizationResult<T> {
    items: T[],
    conflicts: Record<string, T[]>
}

export function normalizeItems<T>(
    items: T[],
    keySelector: (item: T) => string,
    merge: (key: string, item: T, other: T) => T | null,
): NormalizationResult<T> {
    const result: Map<string, T> = new Map()
    const conflicts: Map<string, T[]> = new Map()

    for (const item of items) {
        const key = keySelector(item)
        const existingItem = result.get(key)

        if (!existingItem) {
            result.set(key, item)
        } else {
            const mergedItem = merge(key, existingItem, item)

            if (mergedItem === null) {
                let existingConflict = conflicts.get(key)

                if (!existingConflict) {
                    existingConflict = []
                    conflicts.set(key, existingConflict)
                }

                existingConflict.push(item)
            } else {
                result.set(key, mergedItem)
            }
        }
    }

    return {
        items: Array.from(result.values()),
        conflicts: Object.fromEntries(conflicts.entries())
    }
}
