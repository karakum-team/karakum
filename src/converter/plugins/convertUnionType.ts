import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin";

export const convertUnionType = createSimplePlugin((node, context, render) => {
    if (!ts.isUnionTypeNode(node)) return null

    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    return `Any /* ${typeScriptService?.printNode(node)} */`;
})
