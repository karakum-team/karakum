import {Configuration} from "../configuration/configuration.js";
import {generateImports} from "./generateImports.js";
import {createPackageName} from "./package/createPackageName.js";
import {packageToOutputFileName} from "./package/packageToFileName.js";

export function createGeneratedFile(
    packageChunks: string[],
    fileName: string,
    imports: string[],
    body: string,
    configuration: Configuration,
) {
    const packageName = createPackageName(packageChunks)

    const outputFileName = packageToOutputFileName(packageChunks, fileName, configuration)

    const resultImports = imports
        .concat(generateImports(outputFileName, configuration))
        .filter(Boolean)
        .join("\n")

    const disclaimer = configuration.disclaimer

    return `
${disclaimer}package ${packageName}

${resultImports}

${body}
    `.trim() + "\n"
}
