import {Configuration} from "../configuration/configuration";

export function generateImports(
    outputFileName: string,
    configuration: Configuration,
) {
    const importInjector = configuration.importInjector

    let importSources: string[] = []

    for (const [pattern, imports] of Object.entries(importInjector ?? {})) {
        const regexp = new RegExp(pattern)

        if (regexp.test(outputFileName)) {
            importSources = importSources.concat(imports)
            break
        }
    }

    return importSources
        .map(it => `import ${it}`)
        .join("\n")
}
