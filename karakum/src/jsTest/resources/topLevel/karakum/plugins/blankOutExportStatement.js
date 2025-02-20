import ts from "typescript"

export default (node) => {
    const sourceFileName = node.getSourceFile()?.fileName ?? "generated.d.ts"

    if (
        sourceFileName.endsWith("/myFunction1.d.ts")
        && ts.isExportAssignment(node)
    ) {
        return ""
    }

    return null
}
