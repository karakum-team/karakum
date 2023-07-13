import {process} from "../../src/process.js";
import path from "node:path";
import url from "node:url";

const dirname = path.dirname(url.fileURLToPath(import.meta.url));

process({
    input: path.resolve(dirname, "base/lib/**"),
    output: path.resolve(dirname, "base/generated"),
    libraryName: "sandbox-base",
    verbose: true,
})
    .catch(error => console.error(error))

process({
    input: path.resolve(dirname, "top-level/lib/**"),
    output: path.resolve(dirname, "top-level/generated"),
    libraryName: "sandbox-top-level",
    granularity: "top-level",
    moduleNameMapper: {
        ".*": "sandbox-top-level"
    },
    verbose: true,
})
    .catch(error => console.error(error))

process({
    input: path.resolve(dirname, "namespace/lib/**"),
    output: path.resolve(dirname, "namespace/generated"),
    libraryName: "sandbox-namespace",
    granularity: "top-level",
    verbose: true,
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
    }
})
    .catch(error => console.error(error))
