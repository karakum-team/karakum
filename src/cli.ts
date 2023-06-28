#!/usr/bin/env node

import yargs from 'yargs'
import {hideBin} from 'yargs/helpers'
import {process as mainProcess} from ".";
import {PartialConfiguration} from "./configuration/configuration";

const argv = yargs(hideBin(process.argv))
    .config("config")
    .parserConfiguration({
        "dot-notation": false
    })
    .parseSync()

// TODO: Specify all config params explicitly
mainProcess(argv as unknown as PartialConfiguration)
    .catch(error => console.error(error))
