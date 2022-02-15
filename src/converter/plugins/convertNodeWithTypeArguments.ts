import {NodeWithTypeArguments} from "typescript";
import {ifPresent} from "../render";
import {createSimplePlugin} from "../plugin";

export const convertNodeWithTypeArguments = createSimplePlugin<NodeWithTypeArguments>((node, context, render) =>  {
    const typeArguments = node.typeArguments
        ?.map(typeArgument => render(typeArgument))
        ?.join(", ")

    return ifPresent(typeArguments, it => `<${it}>`)
})
