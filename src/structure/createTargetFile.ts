import {Configuration} from "../configuration/configuration";
import {applyMapper, generateRelativeFileName} from "../utils/fileName";

export function createTargetFile(
    sourceFileRoot: string,
    sourceFileName: string,
    outputFileName: string,
    packageName: string,
    body: string,
    configuration: Configuration,
) {
    const libraryName = configuration.libraryName ?? ""
    const moduleNameMapper = configuration.moduleNameMapper
    const importInjector = configuration.importInjector

    const relativeFileName = generateRelativeFileName(sourceFileRoot, sourceFileName)
    const mappedRelativeFileName = applyMapper(relativeFileName, moduleNameMapper)

    const moduleName = mappedRelativeFileName !== ""
        ? `${libraryName}/${mappedRelativeFileName}`
        : libraryName;

    let importSources: string[] = []

    for (const [pattern, imports] of Object.entries(importInjector ?? {})) {
        const regexp = new RegExp(pattern)

        if (regexp.test(outputFileName)) {
            importSources = importSources.concat(imports)
            break
        }
    }

    const imports = importSources
        .map(it => `import ${it}`)
        .join("\n")


    return `
@file:JsModule("${moduleName}")
@file:JsNonModule

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package ${packageName}

${imports}

${body}
    `
}
