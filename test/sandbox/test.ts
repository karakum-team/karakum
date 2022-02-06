import {process} from "../../src";
import path from "path";

process({
    input: path.resolve(__dirname, "lib/**"),
    output: path.resolve(__dirname, "dist"),
})
