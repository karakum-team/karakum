import ts, {ParameterDeclaration, SignatureDeclarationBase, TypeNode} from "typescript";
import {ConverterContext} from "../context";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {isNullableType, isNullableUnionType} from "./NullableUnionTypePlugin";

interface ParameterInfo {
    parameter: ParameterDeclaration,
    type: TypeNode | undefined,
    nullable: boolean
}

type Signature = ParameterInfo[]

export const prepareParameters = (node: SignatureDeclarationBase, context: ConverterContext) => {
    const currentSignatures = [node.parameters.map(it => ({
        parameter: it,
        type: it.type,
        nullable: false
    }))]

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)

    for (let parameterIndex = 0; parameterIndex < node.parameters.length; parameterIndex++) {
        for (let signatureIndex = 0; signatureIndex < currentSignatures.length; signatureIndex++) {
            const signature = currentSignatures[signatureIndex]
            const {parameter, type} = signature[parameterIndex]

            if (type && ts.isUnionTypeNode(type)) {
                checkCoverageService?.cover(type)

                const generatedSignatures: Signature[] = []

                const nullable = isNullableUnionType(type)

                for (const subtype of type.types) {
                    if (isNullableType(subtype)) {
                        checkCoverageService?.deepCover(type)
                        continue
                    }

                    const generatedSignature: Signature = [...signature]
                    const parameterInfo = {
                        parameter,
                        type: subtype,
                        nullable
                    }
                    generatedSignature.splice(parameterIndex, 1, parameterInfo)
                    generatedSignatures.push(generatedSignature)
                }

                currentSignatures.splice(signatureIndex, 1, ...generatedSignatures)
            }
        }
    }

    return currentSignatures
}
