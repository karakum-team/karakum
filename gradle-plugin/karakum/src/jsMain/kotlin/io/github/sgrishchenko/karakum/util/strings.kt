package io.github.sgrishchenko.karakum.util

@OptIn(ExperimentalJsExport::class)
@JsExport
fun capitalize(string: String): String {
    if (string.isEmpty()) return string
    return string[0].uppercaseChar() + string.slice(1..string.lastIndex)
}

@OptIn(ExperimentalJsExport::class)
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
        .replace("[-_]".toRegex(), "");
}

fun isKebab(string: String): Boolean {
    return "[\\w$-]+".toRegex().matches(string)
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun isValidIdentifier(string: String): Boolean {
    return "[\\w$]+".toRegex().matches(string)
            && !"^\\d".toRegex().containsMatchIn(string)
}

@OptIn(ExperimentalJsExport::class)
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

@OptIn(ExperimentalJsExport::class)
@JsExport
@Deprecated("Prefer escapeIdentifier")
fun identifier(string: String): String {
    return escapeIdentifier(
        camelize(
            string.replace("\\W".toRegex(), "-")
        )
    )
}

@OptIn(ExperimentalJsExport::class)
@JsExport
@Deprecated("Prefer escapeIdentifier")
fun constIdentifier(string: String): String {
    return escapeIdentifier(
        string
            .replace("\\W".toRegex(), "_")
            .uppercase()
    )
}
