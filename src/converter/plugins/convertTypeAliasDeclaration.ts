import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {ifPresent} from "../render";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertTypeAliasDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isTypeAliasDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const name = render(node.name)

    const typeParameters = node.typeParameters
        ?.map(typeParameter => render(typeParameter))
        ?.join(", ")

    const type = render(node.type)

    return `typealias ${name}${ifPresent(typeParameters, it => `<${it}>`)} = ${type}`
})
