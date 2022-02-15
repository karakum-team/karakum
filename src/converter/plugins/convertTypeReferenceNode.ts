import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {convertNodeWithTypeArguments} from "./convertNodeWithTypeArguments";

export const convertTypeReferenceNode = createSimplePlugin((node, context, render) => {
    if (!ts.isTypeReferenceNode(node)) return null
    context.cover(node)

    return render(node.typeName) + convertNodeWithTypeArguments.render(node, context, render)
})
