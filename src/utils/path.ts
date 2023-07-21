import path from "node:path";
import url from "node:url";

export function toPosix(input: string) {
    return path.sep === path.win32.sep
        ? path.toNamespacedPath(input).replaceAll(path.win32.sep, path.posix.sep)
        : input
}

export function toAbsolute(input: string, cwd: string) {
    return path.isAbsolute(input)
        ? input
        : toPosix(path.join(cwd, input))
}

export function toModuleName(input: string) {
    return path.sep === path.win32.sep
        ? url.pathToFileURL(path.normalize(input)).toString()
        : input
}