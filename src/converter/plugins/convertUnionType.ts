import ts from "typescript";
import {createSimplePlugin} from "../plugin";

export const convertUnionType = createSimplePlugin((node, context, render) => {
    if (!ts.isUnionTypeNode(node)) return null

    return `Any /* ${node.getText()} */`;
})
