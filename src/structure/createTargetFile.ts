import {Configuration} from "../configuration/configuration";
import {generateImports} from "./generateImports";
import {OutputFileInfo} from "./generateOutputFileInfo";

export interface TargetFileInfo extends OutputFileInfo {
    qualifier: string | undefined
    hasRuntime: boolean
}

export function createTargetFile(
    targetFileInfo: TargetFileInfo,
    body: string,
    configuration: Configuration,
) {
    const {
        outputFileName,
        packageName,
        moduleName,
        qualifier,
        hasRuntime,
    } = targetFileInfo

    const imports = generateImports(outputFileName, configuration)

    const jsModule = hasRuntime ? `@file:JsModule("${moduleName}")` : ""
    const jsQualifier = hasRuntime && qualifier !== undefined ? `@file:JsQualifier("${qualifier}")` : ""
    const typeAliasSuppress = `
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)
    `.trim()

    const fileAnnotations = [
        jsModule,
        jsQualifier,
        typeAliasSuppress
    ]
        .filter(Boolean)
        .join("\n")

    return `
${fileAnnotations}

package ${packageName}

${imports}

${body}
    `
}
