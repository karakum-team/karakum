import ts from "typescript";
import {ConverterPlugin} from "../plugin";

export const convertEnumMember: ConverterPlugin = (node, context, render) => {
    if (!ts.isEnumMember(node)) return null
    context.cover(node)

    // skip initializer
    node.initializer && context.cover(node.initializer)

    return render(node.name)
}
