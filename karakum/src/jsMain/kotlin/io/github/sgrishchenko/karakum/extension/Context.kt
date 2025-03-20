package io.github.sgrishchenko.karakum.extension

import js.objects.Record
import js.objects.recordOf
import js.symbol.Symbol

@OptIn(ExperimentalJsExport::class)
@JsExport
external interface Context {
    fun <T: Any> registerService(key: Symbol, service: T)
    fun <T: Any> lookupService(key: Symbol): T?
}

fun Context(): Context {
    val services: Record<Symbol, Any> = recordOf()

    return object : Context {
        override fun <T: Any> registerService(key: Symbol, service: T) {
            services[key] = service
        }

        override fun <T: Any> lookupService(key: Symbol): T? {
            @Suppress("UNCHECKED_CAST")
            return services[key] as T?
        }
    }
}
