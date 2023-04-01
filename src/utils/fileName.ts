import {KOTLIN_KEYWORDS} from "../converter/constants";

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
    for (const [pattern, result] of Object.entries(mapper ?? {})) {
        const regexp = new RegExp(pattern)

        if (regexp.test(sourceFileName)) {
            return sourceFileName.replace(regexp, result)
        }
    }

    return sourceFileName
}

export function dirNameToPackage(dirName: string) {
    const preparedDirName = dirName === "." // handle root dir
        ? ""
        : dirName

    return preparedDirName.split("/")
        .filter(it => it !== "")
        .map(it => {
            if (KOTLIN_KEYWORDS.has(it)) {
                return `\`${it}\``
            } else {
                return it
            }
        })
        .join(".")
}
