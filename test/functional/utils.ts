import test from "node:test";
import assert from "node:assert/strict";
import path from "node:path";
import url from "node:url";
import fs from "node:fs/promises";
import {PartialConfiguration} from "../../src/configuration/configuration.js";
import {generate} from "../../src/index.js";

async function readDir(dirName: string) {
    return (await fs.readdir(dirName, {withFileTypes: true, recursive: true}))
        .filter(it => it.isFile())
        .map(it => path.resolve(it.path, it.name))
}

export function testGeneration(name: string, fileUrl: string, configuration: PartialConfiguration) {
    test(name, async context => {
        const dirName = path.dirname(url.fileURLToPath(fileUrl))

        await generate({
            cwd: dirName,
            ...configuration,
        })

        const actualOutputDirName = path.resolve(dirName, "build")
        const expectedOutputDirName = path.resolve(dirName, "generated")

        const actualFileNames = await readDir(actualOutputDirName)
        const expectedFileNames = await readDir(expectedOutputDirName)

        assert.deepEqual(
            new Set(actualFileNames.map(it => path.relative(actualOutputDirName, it))),
            new Set(expectedFileNames.map(it => path.relative(expectedOutputDirName, it))),
        )

        await Promise.all(actualFileNames.map(actualFileName => {
            const relativeFileName = path.relative(actualOutputDirName, actualFileName)
            const expectedFileName = path.resolve(expectedOutputDirName, relativeFileName)

            return context.test(relativeFileName, async () => {
                const actualFile = await fs.readFile(actualFileName, "utf8")
                const expectedFile = await fs.readFile(expectedFileName, "utf8")

                assert.equal(actualFile, expectedFile)
            })
        }))
    });
}
