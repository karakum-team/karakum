package io.github.sgrishchenko.karakum.util

import js.array.ReadonlyArray
import js.objects.JsPlainObject
import js.objects.ReadonlyRecord
import js.objects.recordOf

@JsPlainObject
external interface NormalizationResult<T> {
    val items: ReadonlyArray<T>
    val conflicts: ReadonlyRecord<String, ReadonlyArray<T>>
}

fun <T> normalizeItems(
    items: ReadonlyArray<T>,
    keySelector: (item: T) -> String,
    merge: (key: String, item: T, other: T) -> T?,
): NormalizationResult<T> {
    val result = mutableMapOf<String, T>()
    val conflicts = mutableMapOf<String, MutableList<T>>()

    for (item in items) {
        val key = keySelector(item)
        val existingItem = result[key]

        if (existingItem == null) {
            result[key] = item
        } else {
            val mergedItem = merge(key, existingItem, item)

            if (mergedItem == null) {
                var existingConflict = conflicts[key]

                if (existingConflict == null) {
                    existingConflict = mutableListOf(existingItem)
                    conflicts[key] = existingConflict
                }

                existingConflict += item
            } else {
                result[key] = mergedItem
            }
        }
    }

    return NormalizationResult(
        items = result.values.toTypedArray(),
        conflicts = recordOf(
            pairs = conflicts
                .map { it.key to it.value.toTypedArray() }
                .toTypedArray()
        )
    )
}
