import ts, {Node} from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {camelize, isKebab, isValidIdentifier} from "../../utils/strings.js";

export function createKebabAnnotation(node: Node) {
    if (
        ts.isStringLiteral(node)
        && !isValidIdentifier(node.text)
        && isKebab(node.text)
        && isValidIdentifier(camelize(node.text))
    ) {
        return `@JsName("${node.text}")`
    }

    return ""
}

export const convertMemberName = createSimplePlugin((node, context, render) => {
    if (
        (
            ts.isStringLiteral(node)
            || ts.isNumericLiteral(node)
        )
        && node.parent
        && (
            (ts.isPropertyDeclaration(node.parent) && node.parent.name === node)
            || (ts.isPropertySignature(node.parent) && node.parent.name === node)

            || (ts.isMethodDeclaration(node.parent) && node.parent.name === node)
            || (ts.isMethodSignature(node.parent) && node.parent.name === node)

            || (ts.isGetAccessor(node.parent) && node.parent.name === node)
            || (ts.isSetAccessor(node.parent) && node.parent.name === node)
        )
    ) {
        const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
        checkCoverageService?.cover(node)

        if (isValidIdentifier(node.text)) {
            return node.text
        } else if (
            isKebab(node.text)
            && isValidIdentifier(camelize(node.text))
        ) {
            return camelize(node.text)
        } else {
            return `\`${node.text}\``
        }
    }

    return null
})
