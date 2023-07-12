import {KOTLIN_KEYWORDS} from "./constants.js";

export function capitalize(string: string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

export function camelize(string: string) {
    return string.replace(/([-_][A-Za-z])/g, group =>
        group
            .toUpperCase()
            .replace("-", "")
            .replace("_", "")
    );
}

export function isValidIdentifier(string: string) {
    return /^[\w$]+$/.test(string)
}

export function escapeIdentifier(string: string) {
    if (KOTLIN_KEYWORDS.has(string)) {
        return `\`${string}\``
    }

    if (/^\d/.test(string)) {
        return `\`${string}\``
    }

    return string
}

export function identifier(string: string) {
    return escapeIdentifier(
        camelize(
            string.replace(/\W/g, "-")
        )
    )
}

export function constIdentifier(string: string) {
    return escapeIdentifier(
        string
            .replace(/\W/g, "_")
            .toUpperCase()
    )
}
