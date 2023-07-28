import {testGeneration} from "../utils.js";

testGeneration("bundle", import.meta.url, {
    input: "lib/**",
    output: "build",
    libraryName: "sandbox-base",
    verbose: true,
})
