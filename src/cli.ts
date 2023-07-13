#!/usr/bin/env node

import yargs from 'yargs'
import {hideBin} from 'yargs/helpers'
import {process as mainProcess} from "./process.js";

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

mainProcess(argv)
    .catch(error => console.error(error))
