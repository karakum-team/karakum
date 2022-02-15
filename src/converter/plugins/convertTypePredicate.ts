import ts from "typescript";
import {createSimplePlugin} from "../plugin";

export const convertTypePredicate = createSimplePlugin((node, context, render) => {
    if (!ts.isTypePredicateNode(node)) return null
    context.deepCover(node)

    // TODO: support contracts

    return "Boolean"
})
