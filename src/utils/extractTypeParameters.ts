import ts, {ConditionalTypeNode} from "typescript";
import {traverse} from "./traverse.js";
import {TypeScriptService, typeScriptServiceKey} from "../converter/plugins/TypeScriptPlugin.js";
import {ConverterContext} from "../converter/context.js";

export function extractTypeParameters(node: ts.Node, context: ConverterContext): string[] {
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    const result: string[] = []

    const typeChecker = typeScriptService?.program.getTypeChecker()

    traverse(node, node => {
        if (ts.isIdentifier(node)) {
            const symbol = typeChecker?.getSymbolAtLocation(node)
            const typeParameterDeclarations = (symbol?.declarations ?? [])
                .filter(declaration => ts.isTypeParameterDeclaration(declaration))

            for (const declaration of typeParameterDeclarations) {
                let typeParameterContainer = typeScriptService?.getParent(declaration)

                if (typeParameterContainer && ts.isInferTypeNode(typeParameterContainer)) {
                    const conditionalType = typeScriptService?.findClosest(
                        typeParameterContainer,
                        (node): node is ConditionalTypeNode => ts.isConditionalTypeNode(node)
                    )

                    if (conditionalType != undefined) {
                        typeParameterContainer = conditionalType.trueType
                    }
                }

                const foundParent = typeScriptService?.findClosest(node, node => node === typeParameterContainer)

                if (foundParent !== undefined) {
                    result.push(node.text)
                }
            }
        }
    })

    return result
}
