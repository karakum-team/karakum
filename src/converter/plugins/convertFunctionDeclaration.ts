import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {ifPresent} from "../render.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {convertParameterDeclarations} from "./convertParameterDeclaration.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";
import {NamespaceInfoService, namespaceInfoServiceKey} from "./NamespaceInfoPlugin.js";

export const convertFunctionDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isFunctionDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const exportModifier = node.modifiers?.find(it => it.kind === ts.SyntaxKind.ExportKeyword)
    exportModifier && checkCoverageService?.cover(exportModifier)

    const declareModifier = node.modifiers?.find(it => it.kind === ts.SyntaxKind.DeclareKeyword)
    declareModifier && checkCoverageService?.cover(declareModifier)

    // skip body
    node.body && checkCoverageService?.deepCover(node.body)

    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    const namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)

    const name = (node.name && render(node.name)) ?? "Anonymous"

    const namespace = typeScriptService?.findClosest(node, ts.isModuleDeclaration)

    let externalModifier = "external "

    if (namespace !== undefined && namespaceInfoService?.resolveNamespaceStrategy(namespace) === "object") {
        externalModifier = ""
    }

    const typeParameters = node.typeParameters
        ?.map(typeParameter => render(typeParameter))
        ?.filter(Boolean)
        ?.join(", ")

    const returnType = node.type && render(node.type)

    return convertParameterDeclarations(node, context, render, {
        strategy: "function",
        template: parameters => {
            return `${externalModifier}fun ${ifPresent(typeParameters, it => `<${it}> `)}${name}(${parameters})${ifPresent(returnType, it => `: ${it}`)}`
        }
    })
})
