import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {ifPresent} from "../render.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {VarianceModifierService, varianceModifierServiceKey} from "./VarianceModifierPlugin.js";

export const convertTypeParameterDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isTypeParameterDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const varianceModifierService = context.lookupService<VarianceModifierService>(varianceModifierServiceKey)

    const name = render(node.name)

    const varianceModifier = varianceModifierService?.resolveVarianceModifier(node, context)
    const constraintType = node.constraint && render(node.constraint)
    const defaultType = node.default && render(node.default)

    return `${ifPresent(varianceModifier, it => `${it} `)}${name}${ifPresent(constraintType, it => ` : ${it}`)}${ifPresent(defaultType, it => ` /* default is ${it} */`)}`
})
