package io.github.sgrishchenko.karakum.extension

import typescript.Node

typealias MutabilityModifier = (node: Node, context: Context) -> String?
