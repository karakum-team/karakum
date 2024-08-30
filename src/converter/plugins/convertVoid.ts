import ts, {Node} from "typescript";
import {createSimplePlugin} from "../plugin.js";

function extractReturnType(node: Node) {
    // signatures
    if (ts.isCallSignatureDeclaration(node)) return node.type
    if (ts.isConstructSignatureDeclaration(node)) return node.type
    if (ts.isMethodSignature(node)) return node.type

    // function like
    if (ts.isFunctionDeclaration(node)) return node.type
    if (ts.isMethodDeclaration(node)) return node.type

    // types
    if (ts.isFunctionTypeNode(node)) return node.type
    if (ts.isConstructorTypeNode(node)) return node.type

    return undefined
}

export const convertVoid = createSimplePlugin(node => {
    if (node.kind !== ts.SyntaxKind.VoidKeyword) return null

    const returnType = extractReturnType(node.parent)

    if (returnType !== undefined) {
        return "Unit"
    }

    return "js.core.Void"
})
