import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {ifPresent} from "../render.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";

export const convertTypeParameterDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isTypeParameterDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const name = render(node.name)

    const constraintType = node.constraint && render(node.constraint)
    const defaultType = node.default && render(node.default)

    return `${name}${ifPresent(constraintType, it => ` : ${it}`)}${ifPresent(defaultType, it => ` /* default is ${it} */`)}`
})
