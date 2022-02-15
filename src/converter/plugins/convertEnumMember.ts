import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertEnumMember = createSimplePlugin((node, context, render) => {
    if (!ts.isEnumMember(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    // skip initializer
    node.initializer && checkCoverageService?.cover(node.initializer)

    return render(node.name)
})
