import ts from "typescript"
import {JsPlainObjectAnnotation} from "karakum"

export default new JsPlainObjectAnnotation({
    ignore: node => (
        ts.isInterfaceDeclaration(node)
        && node.name.text === "RouterHistory"
    )
})
