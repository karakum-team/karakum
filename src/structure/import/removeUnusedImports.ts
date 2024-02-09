function removeComments(body: string): string {
    return body
        .replace(/\/\/.*/g, '')
        .replace(/\/\*.*?\*\//gs, '')
}

export function removeUnusedImports(imports: string[], body: string): string[] {
    const cleanedBody = removeComments(body)

    return imports.filter(importItem => {
        if (importItem.includes("unhandled import")) return true

        const matcher = importItem.includes(" as ")
            ? /.+ as (.+)$/
            : /.+\.(.+)$/

        const match = importItem.match(matcher)

        if (!match) throw new Error(`Incorrect import: ${importItem}`)

        const [, name] = match

        return new RegExp(`\\b${name}\\b`).test(cleanedBody)
    })
}
