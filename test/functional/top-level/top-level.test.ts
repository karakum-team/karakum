import {testGeneration} from "../utils.js";

testGeneration("bundle", import.meta.url, {
    input: "lib/**",
    output: "build",
    libraryName: "sandbox-top-level",
    granularity: "top-level",
    moduleNameMapper: {
        ".*": "sandbox-top-level"
    },
    verbose: true,
})
