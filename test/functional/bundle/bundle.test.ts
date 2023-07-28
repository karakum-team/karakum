import {testGeneration} from "../utils.js";

testGeneration("bundle", import.meta.url, {
    input: "lib/**",
    output: "build/customBundle.kt",
    libraryName: "sandbox-bundle",
    granularity: "bundle",
    verbose: true,
})
