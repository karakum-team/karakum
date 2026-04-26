package io.github.sgrishchenko.karakum.extension

import io.github.sgrishchenko.karakum.util.currentAbortable
import js.array.ReadonlyArray
import js.coroutines.promise
import js.promise.Promise
import js.promise.await
import js.reflect.unsafeCast
import typescript.Node
import web.abort.Abortable
import web.abort.asCoroutineScope

sealed external interface InjectionType {
    companion object
}

inline val InjectionType.Companion.MEMBER: InjectionType
    get() = unsafeCast("MEMBER")

inline val InjectionType.Companion.STATIC_MEMBER: InjectionType
    get() = unsafeCast("STATIC_MEMBER")

inline val InjectionType.Companion.PARAMETER: InjectionType
    get() = unsafeCast("PARAMETER")

inline val InjectionType.Companion.TYPE_PARAMETER: InjectionType
    get() = unsafeCast("TYPE_PARAMETER")

inline val InjectionType.Companion.HERITAGE_CLAUSE: InjectionType
    get() = unsafeCast("HERITAGE_CLAUSE")

@JsExport
external interface InjectionContext : Context {
    val type: InjectionType
}

interface Injection : Plugin {
    suspend fun inject(node: Node, context: InjectionContext, render: Render<Node>): ReadonlyArray<String>?
}

@JsExport
@JsName("Injection")
external interface JsInjection : JsPlugin {
    fun inject(node: Node, context: InjectionContext, render: Render<Node>, options: Abortable): Promise<ReadonlyArray<String>?>
}

typealias SimpleInjection = suspend (node: Node, context: InjectionContext, next: Render<Node>) -> ReadonlyArray<String>?

typealias SimpleJsInjection = (node: Node, context: InjectionContext, next: Render<Node>, options: Abortable) -> Promise<ReadonlyArray<String>?>

fun createInjection(
    inject: SimpleInjection,
): Injection {
    return object : Injection {
        override suspend fun setup(context: Context) = Unit

        override suspend fun traverse(node: Node, context: Context) = Unit

        override suspend fun render(node: Node, context: Context, next: Render<Node>) = null

        override suspend fun inject(node: Node, context: InjectionContext, render: Render<Node>) = inject(node, context, render)

        override suspend fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
    }
}

fun createJsInjection(
    inject: SimpleJsInjection,
): JsInjection {
    return object : JsInjection {
        override fun setup(context: Context, options: Abortable) = Promise.resolve(Unit)

        override fun traverse(node: Node, context: Context, options: Abortable) = Promise.resolve(Unit)

        override fun render(node: Node, context: Context, next: Render<Node>, options: Abortable) = Promise.resolve(null)

        override fun inject(node: Node, context: InjectionContext, render: Render<Node>, options: Abortable) =
            inject(node, context, render, options)

        override fun generate(context: Context, render: Render<Node>, options: Abortable) =
            Promise.resolve(emptyArray<GeneratedFile>())
    }
}

fun JsInjection.toInjection(): Injection {
    val jsInjection = this
    val plugin = toPlugin()

    return object : Injection, Plugin by plugin {
        override suspend fun inject(
            node: Node,
            context: InjectionContext,
            render: Render<Node>,
        ): ReadonlyArray<String>? =
            jsInjection.inject(node, context, render, currentAbortable()).await()
    }
}

fun Injection.toJsInjection(): JsInjection {
    val injection = this
    val jsPlugin = toJsPlugin()

    return object : JsInjection, JsPlugin by jsPlugin {
        override fun inject(
            node: Node,
            context: InjectionContext,
            render: Render<Node>,
            options: Abortable,
        ): Promise<ReadonlyArray<String>?> =
            options.asCoroutineScope().promise {
                injection.inject(node, context, render)
            }
    }
}
