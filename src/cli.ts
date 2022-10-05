import yargs from 'yargs'
import {hideBin} from 'yargs/helpers'
import {process as mainProcess} from ".";
import {Configuration} from "./configuration/configuration";

const argv = yargs(hideBin(process.argv))
    .config("config")
    .parseSync()

// TODO: Specify all config params explicitly
mainProcess(argv as unknown as Configuration)
