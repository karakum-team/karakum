import ts from "typescript";
import {createSimplePlugin} from "../plugin";

export const convertUnionTypeHierarchy = createSimplePlugin((node, context, render) => {
    if (!ts.isUnionTypeNode(node)) return null

    // TODO: implement

    return `Any /* ${node.getText()} */`;
})
