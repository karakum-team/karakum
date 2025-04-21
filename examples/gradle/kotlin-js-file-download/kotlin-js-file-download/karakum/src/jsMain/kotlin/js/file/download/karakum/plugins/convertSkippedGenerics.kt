package js.file.download.karakum.plugins

import arrow.core.raise.nullable
import io.github.sgrishchenko.karakum.extension.createPlugin
import typescript.isIdentifier
import typescript.isTypeReferenceNode

val convertSkippedGenerics = createPlugin { node, _, render ->
    nullable {
        ensure(isTypeReferenceNode(node))

        val typeName = node.typeName
        ensure(isIdentifier(typeName))
        ensure(typeName.text == "ArrayBufferView")

        ensure(node.typeArguments == null)

        "${render(node.typeName)}<*>"
    }
}
