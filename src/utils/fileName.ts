export function commonPrefix(...sources: string[][]): string[] {
    const [first, second, ...rest] = sources

    if (first == undefined || first.length === 0) return []
    if (second == undefined || second.length === 0) return first

    const length = Math.min(first.length, second.length)

    const common: string[] = []

    for (let i = 0; i < length; i++) {
        if (first[i] != second[i]) {
            break
        } else {
            common.push(first[i])
        }
    }

    return commonPrefix(common, ...rest)
}

export function applyMapper(sourceFileName: string, mapper: Record<string, string> | undefined) {
    let currentFileName = sourceFileName

    for (const [pattern, result] of Object.entries(mapper ?? {})) {
        const regexp = new RegExp(pattern)

        if (regexp.test(currentFileName)) {
            currentFileName = currentFileName.replace(regexp, result)
        }
    }

    return currentFileName
}
