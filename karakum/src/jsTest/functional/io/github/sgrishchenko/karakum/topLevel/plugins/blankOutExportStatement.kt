package io.github.sgrishchenko.karakum.topLevel.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import io.github.sgrishchenko.karakum.util.getSourceFileOrNull
import typescript.isExportAssignment

val blankOutExportStatement = createSimplePlugin plugin@{ node, _, _ ->
    val sourceFileName = node.getSourceFileOrNull()?.fileName ?: "generated.d.ts"

    if (
        sourceFileName.endsWith("/myFunction1.d.ts")
        && isExportAssignment(node)
    ) {
        return@plugin ""
    }

    null
}
