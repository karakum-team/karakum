import {Node} from "typescript";

export type Render<TNode extends Node = Node> = (node: TNode) => string

export function ifPresent(part: string | undefined, render: (part: string) => string) {
    return part ? render(part) : ""
}
