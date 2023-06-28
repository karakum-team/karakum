import {Configuration} from "../configuration/configuration";
import {generateImports} from "./generateImports";
import {createPackageName} from "./package/createPackageName";
import {packageToOutputFileName} from "./package/packageToFileName";

export function createGeneratedFile(
    packageChunks: string[],
    fileName: string,
    body: string,
    configuration: Configuration,
) {
    const packageName = createPackageName(packageChunks)

    const outputFileName = packageToOutputFileName(packageChunks, fileName, configuration)

    const imports = generateImports(outputFileName, configuration)

    return `
package ${packageName}

${imports}

${body}
    `
}
