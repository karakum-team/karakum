import ts, {ConditionalTypeNode} from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";
import {isPossiblyNullableType} from "./NullableUnionTypePlugin.js";

export const convertConditionalType = createSimplePlugin((node, context, render) => {
    if (!ts.isConditionalTypeNode(node)) return null

    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    const isNullableTrue = isPossiblyNullableType(node.trueType, context)
    const isNullableFalse = isPossiblyNullableType(node.falseType, context)
    const isNullable = isNullableTrue || isNullableFalse

    return `Any${isNullable ? "?" : ""} /* ${typeScriptService?.printNode(node)} */`;
})
