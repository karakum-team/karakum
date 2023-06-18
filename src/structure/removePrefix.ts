export function removePrefix(sourceFileName: string, prefixes: string[]) {
    for (const prefix of prefixes) {
        if (sourceFileName.startsWith(prefix)) {
            return sourceFileName.replace(prefix, "")
        }
    }

    return sourceFileName
}
