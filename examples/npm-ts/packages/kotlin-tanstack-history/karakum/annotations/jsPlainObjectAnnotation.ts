import ts from "typescript"
import {createJsPlainObjectAnnotation} from "karakum"

export default createJsPlainObjectAnnotation({
    ignore: node => (
        ts.isInterfaceDeclaration(node)
        && node.name.text === "RouterHistory"
    )
})
