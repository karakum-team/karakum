import {Configuration} from "../configuration/configuration";
import {applyMapper, generateRelativeFileName} from "../utils/fileName";
import {generateImports} from "./generateImports";

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

    const relativeFileName = generateRelativeFileName(sourceFileRoot, sourceFileName)
    const mappedRelativeFileName = applyMapper(relativeFileName, moduleNameMapper)

    const moduleName = mappedRelativeFileName !== ""
        ? `${libraryName}/${mappedRelativeFileName}`
        : libraryName;

    const imports = generateImports(outputFileName, configuration)

    return `
@file:JsModule("${moduleName}")
@file:JsNonModule

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
    "NAME_CONTAINS_ILLEGAL_CHARS",
)

package ${packageName}

${imports}

${body}
    `
}
