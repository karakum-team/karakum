import {process} from "../../src";
import path from "path";

process({
    input: path.resolve(__dirname, "base/lib/**"),
    output: path.resolve(__dirname, "base/generated"),
    libraryName: "sandbox-base",
    verbose: true,
})
    .catch(error => console.error(error))

process({
    input: path.resolve(__dirname, "top-level/lib/**"),
    output: path.resolve(__dirname, "top-level/generated"),
    libraryName: "sandbox-top-level",
    granularity: "top-level",
    moduleNameMapper: {
        ".*": "sandbox-top-level"
    },
    verbose: true,
})
    .catch(error => console.error(error))

process({
    input: path.resolve(__dirname, "namespace/lib/**"),
    output: path.resolve(__dirname, "namespace/generated"),
    libraryName: "sandbox-namespace",
    granularity: "top-level",
    verbose: true,
    packageNameMapper: {
        "will/be/mapped/andthis": "was/mapped/nested",
        "will/be/mapped": "was/mapped/main"
    },
    namespaceStrategy: {
        "package-namespace": "package",
        "IgnoreNamespace": "ignore",
        "will-be-mapped": "package",
    }
})
    .catch(error => console.error(error))
