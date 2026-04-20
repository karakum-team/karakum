import ts, {type Node} from "typescript"
import {convertParameterDeclarations, ifPresent, type Context, type Render} from "karakum"

export default function (node: Node, context: Context, render: Render<Node>) {
    if (!ts.isPropertySignature(node)) return null

    const interfaceNode = node.parent
    if (!interfaceNode) return null
    if (!ts.isInterfaceDeclaration(interfaceNode)) return null
    if (interfaceNode.name.text !== "RouterHistory") return null

    const type = node.type
    if (!type) return null
    if (!ts.isFunctionTypeNode(type)) return null

    const name = render(node.name)

    const typeParameters = type.typeParameters
        ?.map(it => render(it))
        ?.join(", ")

    const returnType = render(type.type)

    return convertParameterDeclarations(
        type, context, render,
        {
            strategy: "function",
            template: parameters =>
                `fun ${ifPresent(typeParameters, it => `<${it}> `)}${name}(${parameters})${ifPresent(returnType, it => `: ${it}`)}`
        }
    )
}
