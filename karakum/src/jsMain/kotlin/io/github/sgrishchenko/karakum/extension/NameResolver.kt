package io.github.sgrishchenko.karakum.extension

import typescript.Node

typealias NameResolver = (node: Node, context: Context) -> String?
