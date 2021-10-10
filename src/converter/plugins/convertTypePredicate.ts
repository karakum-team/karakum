import ts from "typescript";
import {ConverterPlugin} from "../plugin";

export const convertTypePredicate: ConverterPlugin = (node, context, render) => {
    if (!ts.isTypePredicateNode(node)) return null
    context.deepCover(node)

    // TODO: support contracts

    return "Boolean"
}
