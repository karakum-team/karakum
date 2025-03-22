package io.github.sgrishchenko.karakum.util

@JsExport
fun capitalize(string: String): String {
    if (string.isEmpty()) return string
    return string[0].uppercaseChar() + string.slice(1..string.lastIndex)
}

@JsExport
fun camelize(string: String): String {
    return string
        .replace("([-_][A-Za-z])".toRegex()) {
            it.value
                .uppercase()
                .replace("-", "")
                .replace("_", "")
        }
        // remove leading and trailing delimiters
        .replace("[-_]".toRegex(), "")
}

fun isKebab(string: String): Boolean {
    return "[\\w$-]+".toRegex().matches(string)
}

@JsExport
fun isValidIdentifier(string: String): Boolean {
    return "[\\w$]+".toRegex().matches(string)
            && !"^\\d".toRegex().containsMatchIn(string)
}

@JsExport
fun escapeIdentifier(string: String): String {
    if (string in KOTLIN_KEYWORDS) {
        return "`${string}`"
    }

    if ("_+".toRegex().matches(string)) {
        return "`${string}`"
    }

    if ("^\\d".toRegex().containsMatchIn(string)) {
        return "`${string}`"
    }

    return string
}

@JsExport
@Deprecated("Prefer escapeIdentifier")
fun identifier(string: String): String {
    return escapeIdentifier(
        camelize(
            string.replace("\\W".toRegex(), "-")
        )
    )
}

@JsExport
@Deprecated("Prefer escapeIdentifier")
fun constIdentifier(string: String): String {
    return escapeIdentifier(
        string
            .replace("\\W".toRegex(), "_")
            .uppercase()
    )
}
