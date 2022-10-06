import ts, {SyntaxKind} from "typescript";
import {createSimplePlugin} from "../plugin";
import {ifPresent} from "../render";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertTypeAliasDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isTypeAliasDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const exportModifier = node.modifiers?.find(it => it.kind === SyntaxKind.ExportKeyword)
    exportModifier && checkCoverageService?.cover(exportModifier)

    const declareModifier = node.modifiers?.find(it => it.kind === SyntaxKind.DeclareKeyword)
    declareModifier && checkCoverageService?.cover(declareModifier)

    const name = render(node.name)

    const typeParameters = node.typeParameters
        ?.map(typeParameter => render(typeParameter))
        ?.join(", ")

    if (ts.isTypeLiteralNode(node.type)) {
        checkCoverageService?.cover(node.type)

        const members = node.type.members
            .map(member => render(member))
            .join("\n")

        return `
external interface ${name}${ifPresent(typeParameters, it => `<${it}> `)} {
${members}
}
        `
    }

    const type = render(node.type)

    return `typealias ${name}${ifPresent(typeParameters, it => `<${it}>`)} = ${type}`
})
