import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {ifPresent} from "../render.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {convertStringUnionType, isStringUnionType} from "./StringUnionTypePlugin.js";
import {convertTypeLiteral} from "./TypeLiteralPlugin.js";
import {convertInheritedTypeLiteral, isInheritedTypeLiteral} from "./InheritedTypeLiteralPlugin.js";
import {convertMappedType} from "./MappedTypePlugin.js";
import {convertParameterDeclarations} from "./convertParameterDeclaration.js";

export const convertTypeAliasDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isTypeAliasDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const exportModifier = node.modifiers?.find(it => it.kind === ts.SyntaxKind.ExportKeyword)
    exportModifier && checkCoverageService?.cover(exportModifier)

    const declareModifier = node.modifiers?.find(it => it.kind === ts.SyntaxKind.DeclareKeyword)
    declareModifier && checkCoverageService?.cover(declareModifier)

    const name = render(node.name)

    const typeParameters = node.typeParameters
        ?.map(typeParameter => render(typeParameter))
        ?.filter(Boolean)
        ?.join(", ")

    if (ts.isTypeLiteralNode(node.type)) {
        return convertTypeLiteral(node.type, name, typeParameters, true, context, render)
    }

    if (ts.isMappedTypeNode(node.type)) {
        return convertMappedType(node.type, name, typeParameters, true, context, render)
    }

    if (isStringUnionType(node.type, context)) {
        return convertStringUnionType(node.type, name, true, context, render).declaration
    }

    if (isInheritedTypeLiteral(node.type)) {
        return convertInheritedTypeLiteral(node.type, name, typeParameters, true, context, render)
    }

    if (
        ts.isFunctionTypeNode(node.type)
        && node.type.typeParameters
    ) {
        checkCoverageService?.cover(node.type)

        const mergedTypeParameters = [
            ...node.typeParameters ?? [],
            ...node.type.typeParameters,
        ]
            .map(typeParameter => render(typeParameter))
            .filter(Boolean)
            .join(", ")

        const returnType = render(node.type.type)

        const type = convertParameterDeclarations(node.type, context, render, {
            strategy: "lambda",
            template: parameters => {
                return `(${parameters}) -> ${returnType}`
            },
        })

        return `typealias ${name}${ifPresent(mergedTypeParameters, it => `<${it}>`)} = ${type}`
    }

    const type = render(node.type)

    return `typealias ${name}${ifPresent(typeParameters, it => `<${it}>`)} = ${type}`
})
