package io.github.sgrishchenko.karakum.extension

import typescript.Node

typealias VarianceModifier = (node: Node, context: Context) -> String?
