import process from "node:process"
import {run} from "node:test"
import {spec as SpecReporter} from "node:test/reporters";
import {pipeline} from "node:stream/promises";
import {glob} from "glob";

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
