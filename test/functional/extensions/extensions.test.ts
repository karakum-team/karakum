import {testGeneration} from "../utils.js";

testGeneration("extensions", import.meta.url, output => ({
    input: "lib/**",
    output,
    libraryName: "extensions",
    granularity: "file",
    verbose: true,
    extensions: "./extensions.ts"
}))
