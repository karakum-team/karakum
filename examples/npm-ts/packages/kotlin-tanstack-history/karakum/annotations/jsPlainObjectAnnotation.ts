import ts from "typescript"
import {createJsPlainObjectAnnotation} from "karakum"

export default createJsPlainObjectAnnotation({
    ignore: async node => (
        ts.isInterfaceDeclaration(node)
        && node.name.text === "RouterHistory"
    )
})
