import {Configuration} from "../configuration/configuration";
import {applyMapper, generateRelativeFileName} from "../utils/fileName";
import {generateImports} from "./generateImports";

export function createTargetFile(
    sourceFileRoot: string,
    sourceFileName: string,
    outputFileName: string,
    packageName: string,
    hasRuntime: boolean,
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
${hasRuntime ? `@file:JsModule("${moduleName}")\n` : ""}
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package ${packageName}

${imports}

${body}
    `
}
