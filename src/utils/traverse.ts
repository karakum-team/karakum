import {Node} from "typescript";

export function traverse(rootNode: Node, handler: (node: Node) => void) {
    handler(rootNode)

    rootNode.forEachChild(node => {
        traverse(node, handler)
    })
}
