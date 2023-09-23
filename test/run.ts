import process from "node:process"
import {run} from "node:test"
import {spec as SpecReporter} from "node:test/reporters";
import {pipeline} from "node:stream/promises";
import {glob} from "glob";

const isUpdate = process.argv.some(arg => arg === "-u" || arg === "--update")

if (isUpdate) {
    process.env['KARAKUM_TEST_UPDATE'] = JSON.stringify(true)
}

let fail = false

const source = run({
    concurrency: true,
    files: await glob("test/**/*.test.ts"),
}).once("test:fail", () => {
    fail = true
})

const reporter = new SpecReporter()

const destination = process.stdout

await pipeline(source, reporter, destination)

if (fail) throw new Error("Tests failed")
