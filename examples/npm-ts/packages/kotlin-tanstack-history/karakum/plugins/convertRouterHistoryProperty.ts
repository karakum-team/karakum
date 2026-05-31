import ts, {type Node} from "typescript"
import {convertParameterDeclarations, ifPresent, type Context, type Render, type Abortable} from "karakum"

export default async function (node: Node, context: Context, render: Render<Node>, options: Abortable) {
    if (!ts.isPropertySignature(node)) return null

    const interfaceNode = node.parent
    if (!interfaceNode) return null
    if (!ts.isInterfaceDeclaration(interfaceNode)) return null
    if (interfaceNode.name.text !== "RouterHistory") return null

    const type = node.type
    if (!type) return null
    if (!ts.isFunctionTypeNode(type)) return null

    const name = await render.run(node.name, options)

    const asyncTypeParameters = Promise.all(
        type.typeParameters?.map(it => render.run(it, options)) ?? []
    )
    const typeParameters = (await asyncTypeParameters).join(", ")

    const returnType = await render.run(type.type, options)

    return convertParameterDeclarations(
        type, context, render,
        {
            strategy: "function",
            template: parameters =>
                `fun ${ifPresent(typeParameters, it => `<${it}> `)}${name}(${parameters})${ifPresent(returnType, it => `: ${it}`)}`
        }
    )
}
