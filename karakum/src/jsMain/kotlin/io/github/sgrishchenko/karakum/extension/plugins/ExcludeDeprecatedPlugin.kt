package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import typescript.getJSDocDeprecatedTag

val ExcludeDeprecatedPlugin = createPlugin { node, _, _ ->
    if (getJSDocDeprecatedTag(node) != null) "" else null
}
