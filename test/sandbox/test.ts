import path from "node:path";
import url from "node:url";
import {generate} from "../../src/generate.js";

const dirname = path.dirname(url.fileURLToPath(import.meta.url));

generate({
    input: "base/lib/**",
    output: "base/generated",
    libraryName: "sandbox-base",
    verbose: true,
    cwd: dirname,
})
    .catch(error => console.error(error))

generate({
    input: "top-level/lib/**",
    output: "top-level/generated",
    libraryName: "sandbox-top-level",
    granularity: "top-level",
    moduleNameMapper: {
        ".*": "sandbox-top-level"
    },
    verbose: true,
    cwd: dirname,
})
    .catch(error => console.error(error))

generate({
    input: "namespace/lib/**",
    output: "namespace/generated",
    libraryName: "sandbox-namespace",
    granularity: "top-level",
    packageNameMapper: {
        "will/be/mapped/andthis": "was/mapped/nested",
        "will/be/mapped": "was/mapped/main"
    },
    moduleNameMapper: {
        "will-be-mapped#AndThis": "was-mapped#Nested"
    },
    namespaceStrategy: {
        "package-namespace": "package",
        "IgnoreNamespace": "ignore",
        "will-be-mapped": "package",
    },
    verbose: true,
    cwd: dirname,
})
    .catch(error => console.error(error))

generate({
    input: "bundle/lib/**",
    output: "bundle/generated/customBundle.kt",
    libraryName: "sandbox-bundle",
    granularity: "bundle",
    verbose: true,
    cwd: dirname,
})
    .catch(error => console.error(error))
