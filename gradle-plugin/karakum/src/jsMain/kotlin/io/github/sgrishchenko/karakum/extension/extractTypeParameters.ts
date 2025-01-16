import ts, {ConditionalTypeNode, Declaration} from "typescript";
import {traverse} from "../utils/traverse.js";
import {TypeScriptService, typeScriptServiceKey} from "./plugins/TypeScriptPlugin.js";
import {ConverterContext} from "./context.js";
import {Render} from "./Render.kt";

export type TypeParameterExtractionResult = [ts.Node, Declaration][]

export function extractTypeParameters(
    node: ts.Node,
    context: ConverterContext
): TypeParameterExtractionResult {
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    const result: TypeParameterExtractionResult = []

    const typeChecker = typeScriptService?.program.getTypeChecker()

    const containerNode = node

    traverse(containerNode, node => {
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

                const foundParent = typeScriptService?.findClosest(containerNode, node => node === typeParameterContainer)

                if (foundParent !== undefined && foundParent !== containerNode) {
                    result.push([node, declaration])
                }
            }
        }
    })

    return result
}

export function renderDeclaration(result: TypeParameterExtractionResult, render: Render): string {
    return result
        .map(([, declaration]) => render(declaration))
        .filter(Boolean)
        .join(", ")
}

export function renderReference(result: TypeParameterExtractionResult, render: Render): string {
    return result
        .map(([node]) => render(node))
        .filter(Boolean)
        .join(", ")
}
