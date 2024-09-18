import {testGeneration} from "../utils.js";

testGeneration("top-level", import.meta.url, output => ({
    input: "lib/**",
    output,
    libraryName: "sandbox-top-level",
    granularity: "top-level",
    moduleNameMapper: {
        "^(?!sandbox-top-level/myFunction1$).*": "sandbox-top-level"
    },
    verbose: true,
}))
