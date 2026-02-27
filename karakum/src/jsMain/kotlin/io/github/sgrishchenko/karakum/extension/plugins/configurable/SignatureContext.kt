package io.github.sgrishchenko.karakum.extension.plugins.configurable

import io.github.sgrishchenko.karakum.extension.Context
import io.github.sgrishchenko.karakum.extension.plugins.Signature

@JsExport
interface SignatureContext : Context {
    val signature: Signature
}