import {testGeneration} from "../utils.js";

testGeneration("bundle", import.meta.url, output => ({
    input: "lib/**",
    output: `${output}/customBundle.kt`,
    libraryName: "sandbox-bundle",
    granularity: "bundle",
    verbose: true,
}))
