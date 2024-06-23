import {Configuration} from "../configuration/configuration.js";
import {generateImports} from "./generateImports.js";
import {createPackageName} from "./package/createPackageName.js";
import {packageToOutputFileName} from "./package/packageToFileName.js";
import {removeUnusedImports} from "./import/removeUnusedImports.js";

export function createGeneratedFile(
    packageChunks: string[],
    fileName: string,
    imports: string[],
    body: string,
    configuration: Configuration,
) {
    const packageName = createPackageName(packageChunks)

    const outputFileName = packageToOutputFileName(packageChunks, fileName, configuration)

    const resultImports = removeUnusedImports(imports, body)
        .concat(generateImports(outputFileName, configuration))
        .filter(Boolean)
        .join("\n")

    const disclaimer = configuration.disclaimer

    const content = [
        disclaimer,
        `package ${packageName}`,
        resultImports,
        body,
    ]
        .filter(Boolean)
        .join("\n\n")

    return content.trim() + "\n"
}
