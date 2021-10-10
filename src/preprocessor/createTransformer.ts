import ts, {Node, TransformationContext, TransformerFactory, Visitor, VisitResult} from "typescript";

export function createTransformer<T extends Node>(
    visitor: (node: Node, context: TransformationContext) => VisitResult<Node>
): TransformerFactory<T> {
    return context => {
        const visit: Visitor = node => {
            const result = visitor(node, context)

            if (result) {
                return result
            } else {
                return ts.visitEachChild(node, child => visit(child), context);
            }
        };

        return node => ts.visitNode(node, visit);
    };
}
