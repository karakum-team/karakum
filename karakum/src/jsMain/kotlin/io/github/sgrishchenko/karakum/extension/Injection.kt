package io.github.sgrishchenko.karakum.extension

import js.array.ReadonlyArray
import js.reflect.unsafeCast
import typescript.Node

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

@JsExport
external interface Injection : Plugin {
    fun inject(node: Node, context: InjectionContext, render: Render<Node>): ReadonlyArray<String>?
}

typealias SimpleInjection = (node: Node, context: InjectionContext, next: Render<Node>) -> ReadonlyArray<String>?

fun createInjection(
    inject: SimpleInjection,
): Injection {
    return object : Injection {
        override fun setup(context: Context) = Unit

        override fun traverse(node: Node, context: Context) = Unit

        override fun render(node: Node, context: Context, next: Render<Node>) = null

        override fun inject(node: Node, context: InjectionContext, render: Render<Node>) = inject(node, context, render)

        override fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
    }
}
