import ts from "typescript";
import {ConverterPlugin} from "../plugin";
import {convertNodeWithTypeArguments} from "./convertNodeWithTypeArguments";

export const convertExpressionWithTypeArguments: ConverterPlugin = (node, context, render) => {
    if (!ts.isExpressionWithTypeArguments(node)) return null
    context.cover(node)

    return render(node.expression) + convertNodeWithTypeArguments(node, context, render)
}
