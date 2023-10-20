import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";
import {isPossiblyNullableType} from "./NullableUnionTypePlugin.js";

export const convertUnionType = createSimplePlugin((node, context, render) => {
    if (!ts.isUnionTypeNode(node)) return null

    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    const isNullable = isPossiblyNullableType(node, context)

    return `Any${isNullable ? "?" : ""} /* ${typeScriptService?.printNode(node)} */`;
})
