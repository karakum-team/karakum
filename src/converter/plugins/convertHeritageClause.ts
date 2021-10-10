import ts from "typescript";
import {ConverterPlugin} from "../plugin";

export const convertHeritageClause: ConverterPlugin = (node, context, render) => {
    if (!ts.isHeritageClause(node)) return null
    context.cover(node)

    const types = node.types
        .map(type => render(type))
        .join(", ")

    return " : " + types
}
