import {Configuration} from "../configuration/configuration.js";
import {generateImports} from "./generateImports.js";
import {StructureItem} from "./structure.js";
import {packageToOutputFileName} from "./package/packageToFileName.js";
import {createPackageName} from "./package/createPackageName.js";
import {removeUnusedImports} from "./import/removeUnusedImports.js";

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
        imports,
    } = item

    const {granularity, disclaimer} = configuration

    const packageName = createPackageName(packageChunks)

    const outputFileName = packageToOutputFileName(packageChunks, fileName, configuration)

    const resultImports = removeUnusedImports(imports, body)
        .concat(generateImports(outputFileName, configuration))
        .filter(Boolean)
        .join("\n")

    const jsModule = hasRuntime ? `@file:JsModule("${moduleName}")` : ""
    const jsQualifier = hasRuntime && qualifier ? `@file:JsQualifier("${qualifier}")` : ""
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

    const content = [
        disclaimer,
        fileAnnotations,
        `package ${packageName}`,
        resultImports,
        body,
    ]
        .filter(Boolean)
        .join("\n\n")

    return content.trim() + "\n"
}
