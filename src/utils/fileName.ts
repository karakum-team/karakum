import path from "path";

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

function generateOutputFileName(prefix: string, sourceFileName: string) {
    return sourceFileName
        .replace(prefix, "")
        .replace(/\.d\.ts$/, ".kt")
        .replace(/\.ts$/, ".kt")
}

export function generateRelativeFileName(prefix: string, sourceFileName: string) {
    return sourceFileName
        .replace(prefix, "")
        .replace(/\.d\.ts$/, "")
        .replace(/\.ts$/, "")
}

export function generateTargetFileName(
    prefix: string,
    output: string,
    sourceFileName: string,
    singlePackage: boolean | undefined,
) {
    let targetFileName: string

    if (singlePackage) {
        targetFileName = path.resolve(output, generateOutputFileName(prefix, path.basename(sourceFileName)))
    } else {
        targetFileName = path.resolve(output, generateOutputFileName(prefix, sourceFileName))
    }

    return targetFileName
}
