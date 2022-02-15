import {ConverterPlugin, createSimplePlugin} from "../plugin";
import {Node} from "typescript";
import {Render} from "../render";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export function convertPrimitive<TNode extends Node = Node>(
    predicate: (node: Node) => node is TNode,
    render: Render<TNode>
): ConverterPlugin

export function convertPrimitive(
    predicate: (node: Node) => boolean,
    render: Render
): ConverterPlugin

export function convertPrimitive(
    predicate: (node: Node) => boolean,
    render: Render
): ConverterPlugin {
    return createSimplePlugin((node, context) => {
        if (!predicate(node)) return null

        const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
        checkCoverageService?.cover(node)

        return render(node)
    })
}
