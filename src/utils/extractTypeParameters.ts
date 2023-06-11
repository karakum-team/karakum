import ts, {Program, ConditionalTypeNode} from "typescript";
import {traverse} from "./traverse";
import {findClosest} from "./findClosest";

export function extractTypeParameters(node: ts.Node, program: Program | undefined): string[] {
    const result: string[] = []

    const typeChecker = program?.getTypeChecker()

    traverse(node, node => {
        if (ts.isIdentifier(node)) {
            const symbol = typeChecker?.getSymbolAtLocation(node)
            const typeParameterDeclarations = (symbol?.declarations ?? [])
                .filter(declaration => ts.isTypeParameterDeclaration(declaration))

            for (const declaration of typeParameterDeclarations) {
                let typeParameterContainer = declaration.parent

                if (ts.isInferTypeNode(declaration.parent)) {
                    const conditionalType = findClosest(
                        declaration.parent,
                        (node): node is ConditionalTypeNode => ts.isConditionalTypeNode(node)
                    )

                    if (conditionalType != undefined) {
                        typeParameterContainer = conditionalType.trueType
                    }
                }

                const foundParent = findClosest(node, node => node === typeParameterContainer)

                if (foundParent !== undefined) {
                    result.push(node.text)
                }
            }
        }
    })

    return result
}
