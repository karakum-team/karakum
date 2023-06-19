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

function escapeIdentifier(string: string) {
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
