import ts from "typescript"
import {JsPlainObjectAnnotation} from "karakum"

export default JsPlainObjectAnnotation({
    ignore: node => (
        ts.isInterfaceDeclaration(node)
        && node.name.text === "RouterHistory"
    )
})
