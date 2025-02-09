package io.github.sgrishchenko.karakum.extension

import js.objects.Record
import js.objects.recordOf
import js.symbol.Symbol

@OptIn(ExperimentalJsExport::class)
@JsExport
external interface ConverterContext {
    fun <T: Any> registerService(key: Symbol, service: T)
    fun <T: Any> lookupService(key: Symbol): T?
}

fun ConverterContext(): ConverterContext {
    val services: Record<Symbol, Any> = recordOf()

    return object : ConverterContext {
        override fun <T: Any> registerService(key: Symbol, service: T) {
            services[key] = service
        }

        override fun <T: Any> lookupService(key: Symbol): T? {
            @Suppress("UNCHECKED_CAST")
            return services[key] as T?
        }
    }
}
