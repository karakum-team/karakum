import ts, {Node, StringLiteral} from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {camelize, isKebab, isValidIdentifier} from "../../utils/strings.js";

export function createKebabAnnotation(node: Node) {
    if (ts.isStringLiteral(node) && node.text === "") {
        return `@JsName("")`
    }

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

export function convertMemberNameLiteral(node: StringLiteral) {
    if (node.text === "") {
        return "`_`"
    } else if (isValidIdentifier(node.text)) {
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

        if (ts.isNumericLiteral(node)) return `\`${node.text}\``

        return convertMemberNameLiteral(node)
    }

    return null
})
