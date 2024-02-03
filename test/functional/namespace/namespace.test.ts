import {testGeneration} from "../utils.js";

testGeneration("namespace", import.meta.url, output => ({
    input: "lib/**",
    output,
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
        "package-namespace.ObjectNamespace": "object",
        "package-namespace": "package",
        "IgnoreNamespace": "ignore",
        "will-be-mapped": "package",

        "import-provider": "package",
        "other-import-provider": "package",
        "import-consumer": "package",
    },
    importMapper: {
        "^import-provider": "import.provider",
        "other-import-provider": {
            "default": "other.import.provider.x",
            "\\*": "",
            ".+": "other.import.provider.",
        }
    },
    verbose: true,
}))
