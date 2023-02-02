import {Node} from "typescript";

export function setParentNodes(rootNode: Node) {
    rootNode.forEachChild(node => {
        (node as { parent: Node }).parent = rootNode
        setParentNodes(node)
    })

    return rootNode
}
