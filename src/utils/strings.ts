export function capitalize(string: string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

export function camelize(string: string) {
    return string.replace(/([-_][a-z])/g, group =>
        group
            .toUpperCase()
            .replace('-', '')
            .replace('_', '')
    );
}

export function identifier(string: string) {
    return camelize(
        string.replace(/\W/g, "-")
    )
}

export function constIdentifier(string: string) {
    return string
        .replace(/\W/g, "_")
        .toUpperCase()
}
