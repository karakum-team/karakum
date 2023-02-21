import ts, {SyntaxKind} from "typescript";
import {createSimplePlugin, SimpleConverterPlugin} from "../../plugin";
import {ifPresent} from "../../render";
import {CheckCoverageService, checkCoverageServiceKey} from "../CheckCoveragePlugin";
import {convertParameterDeclarations} from "../convertParameterDeclaration";

export const convertCallableInterfaceDeclaration = createSimplePlugin((node, context, render) => {
    if (
        ts.isInterfaceDeclaration(node)
        && node.members.filter(it => ts.isCallSignatureDeclaration(it)).length > 1
    ) {
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
            ?.join(", ")

        const members = node.members
            .map(member => (
                ts.isCallSignatureDeclaration(member)
                    ? convertCallSignature(member, context, render)
                    : render(node)
            ))
            .join("\n")

        return `
sealed class ${name}${ifPresent(typeParameters, it => `<${it}>`)}${ifPresent(heritageClauses, it => ` : ${it}`)} {
${members}
}
        `
    }

    return null
})

export const convertCallSignature: SimpleConverterPlugin = (node, context, render) => {
    if (!ts.isCallSignatureDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const typeParameters = node.typeParameters
        ?.map(typeParameter => render(typeParameter))
        ?.join(", ")

    const returnType = node.type && render(node.type)

    return convertParameterDeclarations(node, context, render, {
        strategy: "inline",
        template: (parameters, signature) => {
            const parameterNames = signature
                .map(parameter => render(parameter.parameter.name))
                .join(", ")

            return `
@Suppress("NOTHING_TO_INLINE")
inline operator fun ${ifPresent(typeParameters, it => `<${it}>`)} invoke(${parameters})${ifPresent(returnType, it => `: ${it}`)} = asDynamic()(${parameterNames})
            `
        }
    })
}
