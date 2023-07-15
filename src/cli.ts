#!/usr/bin/env node

import yargs from "yargs"
import process from "node:process"
import {hideBin} from "yargs/helpers"
import {generate} from "./generate.js";

const argv = yargs(hideBin(process.argv))
    .option("input", {
        type: "string",
        array: true,
        demandOption: true,
        description: "Input glob patterns",
    })
    .option("output", {
        type: "string",
        demandOption: true,
        description: "Output directory or file"
    })
    .config("config")
    .parserConfiguration({
        "dot-notation": false
    })
    .parseSync()

await generate(argv)
