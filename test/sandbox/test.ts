import {process} from "../../src";
import path from "path";

process({
    input: path.resolve(__dirname, "base/lib/**"),
    output: path.resolve(__dirname, "base/generated"),
    libraryName: "sandbox-base",
    verbose: true,
})
    .catch(error => console.error(error))

process({
    input: path.resolve(__dirname, "top-level-granularity/lib/**"),
    output: path.resolve(__dirname, "top-level-granularity/generated"),
    libraryName: "sandbox-top-level-granularity",
    granularity: "top-level",
    moduleNameMapper: {
        ".*": ""
    },
    verbose: true,
})
    .catch(error => console.error(error))
