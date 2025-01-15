@file:Suppress("NOTHING_TO_INLINE")

package io.github.sgrishchenko.karakum.util

import js.objects.ReadonlyRecord
import js.objects.recordOf

// string | Record<string, string>
sealed external interface Rule

inline fun ruleOf(value: String) = value.unsafeCast<Rule>()

inline fun ruleOf(vararg values: Pair<String, String>) = recordOf(*values).unsafeCast<Rule>()

inline fun Rule.singleOrNull(): String? {
    return if (jsTypeOf(this) == "string") {
        this.unsafeCast<String?>()
    } else {
        null
    }
}

inline fun Rule.recordOrNull(): ReadonlyRecord<String, String>? {
    return if (jsTypeOf(this) == "string") {
        null
    } else {
        this.unsafeCast<ReadonlyRecord<String, String>?>()
    }
}
