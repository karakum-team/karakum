import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {convertNodeWithTypeArguments} from "./convertNodeWithTypeArguments";

export const convertExpressionWithTypeArguments = createSimplePlugin((node, context, render) => {
    if (!ts.isExpressionWithTypeArguments(node)) return null
    context.cover(node)

    return render(node.expression) + convertNodeWithTypeArguments.render(node, context, render)
})
