import ts, {SyntaxKind} from "typescript";
import {createSimplePlugin} from "../plugin";
import {ifPresent} from "../render";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertInterfaceDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isInterfaceDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const exportModifier = node.modifiers?.find(it => it.kind === SyntaxKind.ExportKeyword)
    exportModifier && checkCoverageService?.cover(exportModifier)

    const name = render(node.name)

    const typeParameters = node.typeParameters
        ?.map(typeParameter => render(typeParameter))
        ?.join(", ")

    const heritageClauses = node.heritageClauses
        ?.map(heritageClause => render(heritageClause))
        ?.join(" ")

    const members = node.members
        .map(member => render(member))
        .join("\n")

    return `
external interface ${name}${ifPresent(typeParameters, it => `<${it}> `)}${heritageClauses ?? ""} {
${members}
}
    `
})
