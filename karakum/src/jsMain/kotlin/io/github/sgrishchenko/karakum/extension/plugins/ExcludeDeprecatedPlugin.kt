package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin

val ExcludeDeprecatedPlugin = createPlugin { node, _, _ ->
    if (isDeprecated(node)) "" else null
}
