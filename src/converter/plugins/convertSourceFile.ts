import ts from "typescript";
import {ConverterPlugin} from "../plugin";
import {KOTLIN_KEYWORDS} from "../constants";

export const convertSourceFile: ConverterPlugin = (node, context, render) => {
    if (!ts.isSourceFile(node)) return null
    context.cover(node)
    context.cover(node.endOfFileToken)

    const body = node.statements
        .map(statement => render(statement))
        .join("\n")

    const packageChunks = context.module.split("/")
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
@file:JsModule("${context.module}")
@file:JsNonModule

package ${packageName}

${body}
    `
}
