import {PartialExtensions} from "../../../src/configuration/loadExtensions.js";
import ts from "typescript";

export default {
    plugins: [
        node => {
            if (
                ts.isTypeReferenceNode(node)
                && ts.isIdentifier(node.typeName)
                && node.typeName.text === "Error"
            ) {
                return "js.errors.JsError"
            }
            if (
                ts.isExpressionWithTypeArguments(node)
                && ts.isIdentifier(node.expression)
                && node.expression.text === "Error"
            ) {
                return "js.errors.JsError"
            }
            return null
        }
    ]
} satisfies PartialExtensions
