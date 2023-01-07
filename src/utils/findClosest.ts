import {Node} from "typescript";

export function findClosest<T extends Node>(rootNode: Node, predicate: (node: Node) => node is T): T | undefined

export function findClosest(rootNode: Node, predicate: (node: Node) => boolean): Node | undefined

export function findClosest(rootNode: Node, predicate: (node: Node) => boolean): Node | undefined {
    if (predicate(rootNode)) return rootNode
    if (rootNode.parent === undefined) return undefined

    return findClosest(rootNode.parent, predicate)
}
