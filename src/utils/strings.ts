export function capitalize(string: string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

export function snakeToCamelCase(string: string) {
    return string.replace(/([-_][a-z])/g, group =>
        group
            .toUpperCase()
            .replace('-', '')
            .replace('_', '')
    );
}

export function identifier(string: string) {
    return snakeToCamelCase(
        string.replace(/\W/g, "-")
    )
}
