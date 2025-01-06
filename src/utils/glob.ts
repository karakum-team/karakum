import {glob as nodeGlob} from "node:fs/promises";
import path, {matchesGlob} from "node:path";
import {toPosix} from "./path.js";

export interface GlobOptions {
    cwd: string,
    ignore?: string[],
}

export async function glob(patterns: string[], options: GlobOptions) {
    const fileNames = await Array.fromAsync(
        nodeGlob(patterns, {
            cwd: options.cwd,
            withFileTypes: true,
            exclude: it => {
                const fileName = path.resolve(it.parentPath, it.name)

                return (options.ignore ?? [])
                    .some(it => matchesGlob(fileName, it))
            }
        })
    )

    return fileNames.map(it => toPosix(path.resolve(it.parentPath, it.name)))
}
