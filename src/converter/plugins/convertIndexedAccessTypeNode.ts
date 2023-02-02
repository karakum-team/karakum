import ts, {NodeBuilderFlags} from "typescript";
import {createSimplePlugin} from "../plugin";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {setParentNodes} from "../../utils/setParentNodes";

export const convertIndexedAccessTypeNode = createSimplePlugin((node, context, render) => {
    if (!ts.isIndexedAccessTypeNode(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    checkCoverageService?.deepCover(node)

    const typeChecker = typeScriptService?.program?.getTypeChecker()
    if (typeChecker === undefined) throw new Error("IndexedAccessTypeNodePlugin can't work without TypeScriptService")

    const type = typeChecker.getTypeAtLocation(node)
    const typeNode = typeChecker.typeToTypeNode(type, undefined, NodeBuilderFlags.NoTruncation)
    const fixedTypeNode = typeNode && setParentNodes(typeNode)

    if (!fixedTypeNode) return `Any /* ${typeScriptService?.printNode(node)} */`;

    return render(typeNode)
})
