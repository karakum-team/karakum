import path from "path";
import {Configuration} from "../configuration/configuration";
import {KOTLIN_KEYWORDS} from "../converter/constants";
import {applyMapper, generateRelativeFileName} from "../utils/fileName";

export function createTargetFile(
    sourceFileRoot: string,
    sourceFileName: string,
    outputFileName: string,
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

    let outputDirName = path.dirname(outputFileName)
    outputDirName = outputDirName === "." // handle root dir
        ? ""
        : outputDirName

    const preparedLibraryName = libraryName
        .replace(/[-\/]/g, ".")
        .replace(/@/g, "")

    const packageName = [preparedLibraryName, ...outputDirName.split("/")]
        .filter(it => it !== "")
        .map(it => {
            if (KOTLIN_KEYWORDS.has(it)) {
                return `\`${it}\``
            } else {
                return it
            }
        })
        .join(".")

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
