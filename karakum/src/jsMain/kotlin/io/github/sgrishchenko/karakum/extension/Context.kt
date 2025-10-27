package io.github.sgrishchenko.karakum.extension

import js.objects.Record
import js.objects.recordOf
import js.symbol.Symbol

@JsExport
external interface ContextKey<T : Any>

@JsExport
@Suppress("NOTHING_TO_INLINE")
inline fun <T : Any> ContextKey(): ContextKey<T> =
    Symbol().unsafeCast<ContextKey<T>>()

@JsExport
external interface Context {
    fun <T: Any> registerService(key: ContextKey<T>, service: T)
    fun <T: Any> lookupService(key: ContextKey<T>): T?
    fun <T: Any> requireService(key: ContextKey<T>): T
}

fun Context(): Context {
    val services: Record<ContextKey<*>, Any> = recordOf()

    return object : Context {
        override fun <T: Any> registerService(key: ContextKey<T>, service: T) {
            services[key] = service
        }

        override fun <T: Any> lookupService(key: ContextKey<T>): T? {
            @Suppress("UNCHECKED_CAST")
            return services[key] as T?
        }

        override fun <T : Any> requireService(key: ContextKey<T>): T {
            return requireNotNull(lookupService(key))
        }
    }
}
