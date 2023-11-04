import {Configuration} from "../configuration/configuration.js";
import {generateImports} from "./generateImports.js";
import {createPackageName} from "./package/createPackageName.js";
import {packageToOutputFileName} from "./package/packageToFileName.js";

export function createGeneratedFile(
    packageChunks: string[],
    fileName: string,
    body: string,
    configuration: Configuration,
) {
    const packageName = createPackageName(packageChunks)

    const outputFileName = packageToOutputFileName(packageChunks, fileName, configuration)

    const imports = generateImports(outputFileName, configuration)

    const disclaimer = configuration.disclaimer

    return `
${disclaimer}package ${packageName}

${imports}

${body}
    `.trim() + "\n"
}
