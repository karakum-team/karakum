import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {escapeIdentifier, isValidIdentifier} from "../../utils/strings.js";
import {ifPresent, renderNullable} from "../render.js";
import {InheritanceModifierService, inheritanceModifierServiceKey} from "./InheritanceModifierPlugin.js";

export const convertPropertySignature = createSimplePlugin((node, context, render) => {
    if (!ts.isPropertySignature(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)

    checkCoverageService?.cover(node)

    const inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)

    const inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)

    const readonly = node.modifiers?.find(modifier => modifier.kind === ts.SyntaxKind.ReadonlyKeyword)
    readonly && checkCoverageService?.cover(readonly)

    node.questionToken && checkCoverageService?.cover(node.questionToken)

    const modifier = readonly
        ? "val "
        : "var "

    let name: string

    if (ts.isStringLiteral(node.name)) {
        if (!isValidIdentifier(node.name.text)) {
            // TODO: generate inline getter
            return null
        }
        name = escapeIdentifier(node.name.text)
    } else if (ts.isNumericLiteral(node.name)) {
        // TODO: generate inline getter
        return null
    } else {
        name = escapeIdentifier(render(node.name))
    }

    const isOptional = Boolean(node.questionToken)

    const type = renderNullable(node.type, isOptional, context, render)

    return `${ifPresent(inheritanceModifier, it => `${it} `)}${modifier}${name}: ${type}`
})
