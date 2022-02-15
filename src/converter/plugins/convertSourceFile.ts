import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {KOTLIN_KEYWORDS} from "../constants";
import {generateRelativeFileName} from "../../utils/fileName";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {ConfigurationService, configurationServiceKey} from "./ConfigurationPlugin";

export function convertSourceFile(sourceFileRoot: string) {
    return createSimplePlugin((node, context, render) => {
        if (!ts.isSourceFile(node)) return null

        const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
        const configurationService = context.lookupService<ConfigurationService>(configurationServiceKey)

        checkCoverageService?.cover(node)
        checkCoverageService?.cover(node.endOfFileToken)

        const relativeFileName = generateRelativeFileName(sourceFileRoot, node.fileName)

        const libraryName = configurationService?.configuration?.libraryName ?? ""

        const module = relativeFileName !== ""
            ? `${libraryName}/${relativeFileName}`
            : libraryName;

        const body = node.statements
            .map(statement => render(statement))
            .join("\n")

        const packageChunks = module.split("/")
        packageChunks.pop()

        const packageName = packageChunks
            .map(it => {
                if (KOTLIN_KEYWORDS.has(it)) {
                    return `\`${it}\``
                } else {
                    return it
                }
            })
            .join(".")

        return `
@file:JsModule("${module}")
@file:JsNonModule

package ${packageName}

${body}
    `
    })
}
