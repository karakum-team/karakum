import ts from "typescript";
import {createSimplePlugin} from "../plugin";

export const convertIndexedAccessTypeNode = createSimplePlugin((node, context, render) => {
    if (!ts.isIndexedAccessTypeNode(node)) return null

    return `Any /* ${node.getText()} */`;
})
