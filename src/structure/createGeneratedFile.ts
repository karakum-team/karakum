import {Configuration} from "../configuration/configuration";
import {generateImports} from "./generateImports";

export function createGeneratedFile(
    outputFileName: string,
    packageName: string,
    body: string,
    configuration: Configuration,
) {
    const imports = generateImports(outputFileName, configuration)

    return `
package ${packageName}

${imports}

${body}
    `
}
