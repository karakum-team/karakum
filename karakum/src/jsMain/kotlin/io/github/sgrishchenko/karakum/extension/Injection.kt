package io.github.sgrishchenko.karakum.extension

import js.array.ReadonlyArray
import seskar.js.JsNative
import seskar.js.JsValue
import typescript.Node

sealed external interface InjectionType {
    companion object {
        @JsValue("MEMBER")
        val MEMBER: InjectionType

        @JsValue("STATIC_MEMBER")
        val STATIC_MEMBER: InjectionType

        @JsValue("PARAMETER")
        val PARAMETER: InjectionType

        @JsValue("TYPE_PARAMETER")
        val TYPE_PARAMETER: InjectionType

        @JsValue("HERITAGE_CLAUSE")
        val HERITAGE_CLAUSE: InjectionType
    }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
external interface InjectionContext : Context {
    val type: InjectionType
}

@OptIn(ExperimentalJsExport::class)
@JsExport
external interface Injection : Plugin {
    fun inject(node: Node, context: InjectionContext, render: Render<Node>): ReadonlyArray<String>?
}

external interface SimpleInjection {
    @JsNative
    operator fun invoke(node: Node, context: InjectionContext, next: Render<Node>): ReadonlyArray<String>?
}

fun createSimpleInjection(
    inject: (node: Node, context: InjectionContext, next: Render<Node>) -> ReadonlyArray<String>?,
) = createSimpleInjection(inject.unsafeCast<SimpleInjection>())

fun createSimpleInjection(
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
