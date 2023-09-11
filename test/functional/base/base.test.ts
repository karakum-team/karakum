import {testGeneration} from "../utils.js";

testGeneration("base", import.meta.url, {
    input: "lib/**",
    output: "build",
    libraryName: "sandbox-base",
    verbose: true,
})
