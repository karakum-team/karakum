import ts, {SyntaxKind} from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {escapeIdentifier} from "../../utils/strings";
import {renderNullable} from "../render";

export const convertPropertySignature = createSimplePlugin((node, context, render) => {
    if (!ts.isPropertySignature(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)

    checkCoverageService?.cover(node)

    const readonly = node.modifiers?.find(modifier => modifier.kind === SyntaxKind.ReadonlyKeyword)
    readonly && checkCoverageService?.cover(readonly)

    node.questionToken && checkCoverageService?.cover(node.questionToken)

    const modifier = readonly
        ? "val "
        : "var "

    const name = escapeIdentifier(render(node.name))

    const isOptional = Boolean(node.questionToken)

    const type = renderNullable(node.type, isOptional, context, render)

    return `${modifier}${name}: ${type}`
})
