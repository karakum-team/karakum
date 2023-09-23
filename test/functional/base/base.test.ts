import {testGeneration} from "../utils.js";

testGeneration("base", import.meta.url, output => ({
    input: "lib/**",
    output,
    libraryName: "sandbox-base",
    verbose: true,
}))
