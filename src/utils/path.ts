import path from "node:path";
import url from "node:url";

export function toPosix(input: string) {
    return path.sep === path.win32.sep
        ? input.replaceAll(path.win32.sep, path.posix.sep)
        : input
}

export function toAbsolute(input: string, cwd: string) {
    const absolutePath = path.isAbsolute(input)
        ? input
        : path.join(cwd, input)

    return toPosix(absolutePath)
}

export function toModuleName(input: string) {
    return path.sep === path.win32.sep
        ? url.pathToFileURL(path.normalize(input)).toString()
        : input
}
