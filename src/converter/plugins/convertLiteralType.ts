import ts from "typescript";
import {createSimplePlugin} from "../plugin";

export const convertLiteralType = createSimplePlugin((node, context, render) => {
    if (!ts.isLiteralTypeNode(node)) return null
    context.cover(node)

    return render(node.literal)
})
