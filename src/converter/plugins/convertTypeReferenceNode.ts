import ts from "typescript";
import {ConverterPlugin} from "../plugin";
import {convertNodeWithTypeArguments} from "./convertNodeWithTypeArguments";

export const convertTypeReferenceNode: ConverterPlugin = (node, context, render) => {
    if (!ts.isTypeReferenceNode(node)) return null
    context.cover(node)

    return render(node.typeName) + convertNodeWithTypeArguments(node, context, render)
}
