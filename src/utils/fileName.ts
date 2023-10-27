export function commonPrefix(...sources: string[][]): string[] {
    let [common = []] = sources

    for (let i = 1; i < sources.length; i++) {
        const current = sources[i]
        const nextCommon: string[] = []

        for (let j = 0; j < common.length; j++) {
            if (common[j] != current[j]) {
                break
            } else {
                nextCommon.push(common[j])
            }
        }

        common = nextCommon
    }

    return common
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
