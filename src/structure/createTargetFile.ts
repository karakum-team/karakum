import {Configuration} from "../configuration/configuration";
import {generateImports} from "./generateImports";
import {StructureItem} from "./structure";
import {packageToOutputFileName} from "./package/packageToFileName";
import {createPackageName} from "./package/createPackageName";

export function createTargetFile(
    item: StructureItem,
    body: string,
    configuration: Configuration,
) {
    const {
        fileName,
        package: packageChunks,
        moduleName,
        qualifier,
        hasRuntime,
    } = item

    const granularity = configuration.granularity ?? "file"

    const packageName = createPackageName(packageChunks)

    const outputFileName = packageToOutputFileName(packageChunks, fileName, configuration)

    const imports = generateImports(outputFileName, configuration)

    const jsModule = hasRuntime ? `@file:JsModule("${moduleName}")` : ""
    const jsQualifier = hasRuntime && qualifier !== undefined ? `@file:JsQualifier("${qualifier}")` : ""
    const typeAliasSuppress = granularity === "top-level" ? "" : `
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
