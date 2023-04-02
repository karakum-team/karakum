import {Configuration} from "../configuration/configuration";
import {generateImports} from "./generateImports";
import {OutputStructureItem} from "./structure";
import {packageToOutputFileName} from "./package/packageToFileName";
import {createPackageName} from "./package/createPackageName";

export function createTargetFile(
    item: OutputStructureItem,
    configuration: Configuration,
) {
    const {
        moduleName,
        qualifier,
        hasRuntime,
        body
    } = item

    const packageName = createPackageName(item.package)

    const outputFileName = packageToOutputFileName(item.package, item.fileName, configuration)

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
