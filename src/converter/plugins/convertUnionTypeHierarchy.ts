import ts from "typescript";
import {ConverterPlugin} from "../plugin";

export const convertUnionTypeHierarchy: ConverterPlugin = (node, context, render) => {
    if (!ts.isUnionTypeNode(node)) return null

    // TODO: implement

    return `Any /* ${node.getText()} */`;
}
