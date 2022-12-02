import ts from "typescript";
import {createSimplePlugin} from "../plugin";

export const convertIntersectionType = createSimplePlugin((node, context, render) => {
    if (!ts.isIntersectionTypeNode(node)) return null

    return `Any /* ${node.getText()} */`;
})
