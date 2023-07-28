import process from "node:process"
import {run} from "node:test"
import {spec as SpecReporter} from "node:test/reporters";
import {pipeline} from "node:stream/promises";
import {glob} from "glob";

const source = run({
    concurrency: true,
    timeout: 10000,
    files: await glob("test/**/*.test.ts"),
})

const reporter = new SpecReporter()

const destination = process.stdout

await pipeline(source, reporter, destination)
