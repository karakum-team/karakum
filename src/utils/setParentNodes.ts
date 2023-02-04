import {Node} from "typescript";

export function setParentNodes<TNode extends Node>(rootNode: TNode) {
    rootNode.forEachChild(node => {
        (node as { parent: Node }).parent = rootNode
        setParentNodes(node)
    })

    return rootNode
}
