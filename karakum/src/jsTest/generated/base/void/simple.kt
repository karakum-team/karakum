// Automatically generated - do not modify!

@file:JsModule("sandbox-base/void/simple")

package sandbox.base.void

import js.promise.Promise

external interface InterfaceWithVoidSignatures {
operator fun  invoke(): Unit
/* new (): void; */
fun someMethod(): Unit
}

external interface TypeWithVoidSignatures {
operator fun get(key: String): js.core.Void?

operator fun set(key: String, value: js.core.Void?)
}

external class ClassWithVoidDeclarations {
fun someMethod(): Unit
val someValue: js.core.Void
}

external fun functionWithVoidReturnType(): Unit

external fun functionWithPromiseVoidReturnType(): Promise<js.core.Void>

typealias functionTypeWithVoidReturnType = () -> Unit

typealias constructorTypeWithVoidReturnType = js.function.ConstructorFunction<js.array.Tuple, Unit>
