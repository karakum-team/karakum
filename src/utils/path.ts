import path from "node:path";

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