// Automatically generated - do not modify!

@file:JsModule("sandbox-base/conditionalType/simple")

package sandbox.base.conditionalType

typealias TestConditional<S> = Any /* S extends () => infer P ? Promise<P> : Promise<void> */

typealias TestNullableConditional<S> = Any? /* S extends () => infer P ? Promise<P> | null : Promise<void> */
