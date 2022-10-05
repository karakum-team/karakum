import ts from "typescript";
import path from "path";
import {createSimplePlugin} from "../plugin";
import {KOTLIN_KEYWORDS} from "../constants";
import {snakeToCamelCase} from "../../utils/strings";
import {applyMapper, generateOutputFileName, generateRelativeFileName} from "../../utils/fileName";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {ConfigurationService, configurationServiceKey} from "./ConfigurationPlugin";

export function convertSourceFile(sourceFileRoot: string) {
    return createSimplePlugin((node, context, render) => {
        if (!ts.isSourceFile(node)) return null

        const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
        const configurationService = context.lookupService<ConfigurationService>(configurationServiceKey)

        checkCoverageService?.cover(node)
        checkCoverageService?.cover(node.endOfFileToken)

        const libraryName = configurationService?.configuration?.libraryName ?? ""
        const moduleNameMapper = configurationService?.configuration?.moduleNameMapper
        const packageNameMapper = configurationService?.configuration?.packageNameMapper

        const relativeFileName = generateRelativeFileName(sourceFileRoot, node.fileName)
        const mappedRelativeFileName = applyMapper(relativeFileName, moduleNameMapper)

        const moduleName = mappedRelativeFileName !== ""
            ? `${libraryName}/${mappedRelativeFileName}`
            : libraryName;

        const outputFileName = generateOutputFileName(sourceFileRoot, node.fileName)
        const mappedOutputFileName = snakeToCamelCase(applyMapper(outputFileName, packageNameMapper))

        let outputDirName = path.dirname(mappedOutputFileName)
        outputDirName = outputDirName === "." // handle root dir
            ? ""
            : outputDirName

        const preparedLibraryName = libraryName
            .replace(/[-\/]/g, ".")
            .replace(/@/g, "")

        const packageName = [preparedLibraryName, ...outputDirName.split("/")]
            .filter(it => it !== "")
            .map(it => {
                if (KOTLIN_KEYWORDS.has(it)) {
                    return `\`${it}\``
                } else {
                    return it
                }
            })
            .join(".")

        const body = node.statements
            .map(statement => render(statement))
            .join("\n")

        return `
@file:JsModule("${moduleName}")
@file:JsNonModule

package ${packageName}

${body}
    `
    })
}
