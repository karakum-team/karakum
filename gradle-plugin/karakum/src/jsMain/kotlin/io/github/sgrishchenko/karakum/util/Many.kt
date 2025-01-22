@file:Suppress("NOTHING_TO_INLINE")

package io.github.sgrishchenko.karakum.util

import js.array.ReadonlyArray

// T | T[]
sealed external interface Many<out T>

inline fun <T> manyOf(value: T) = value.unsafeCast<Many<T>>()

inline fun <T> manyOf(vararg values: T) = values.unsafeCast<Many<T>>()

inline fun <T> Many<T>.toArray(): ReadonlyArray<T> {
    @Suppress("USELESS_IS_CHECK")
    val result = if (this is ReadonlyArray<*>) {
        this
    } else {
        arrayOf(this)
    }

    @Suppress("UNCHECKED_CAST")
    return result as ReadonlyArray<T>
}
