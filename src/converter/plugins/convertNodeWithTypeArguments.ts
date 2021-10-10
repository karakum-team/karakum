import {NodeWithTypeArguments} from "typescript";
import {ifPresent} from "../render";
import {ConverterPlugin} from "../plugin";

export const convertNodeWithTypeArguments: ConverterPlugin<NodeWithTypeArguments> = (node, context, render) =>  {
    const typeArguments = node.typeArguments
        ?.map(typeArgument => render(typeArgument))
        ?.join(", ")

    return ifPresent(typeArguments, it => `<${it}>`)
}
