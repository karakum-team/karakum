import {NodeWithTypeArguments} from "typescript";
import {ifPresent} from "../render.js";
import {createSimplePlugin} from "../plugin.js";

export const convertNodeWithTypeArguments = createSimplePlugin<NodeWithTypeArguments>((node, context, render) =>  {
    const typeArguments = node.typeArguments
        ?.map(typeArgument => render(typeArgument))
        ?.filter(Boolean)
        ?.join(", ")

    return ifPresent(typeArguments, it => `<${it}>`)
})
