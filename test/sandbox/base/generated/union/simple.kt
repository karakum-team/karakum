
@file:JsModule("sandbox-base/union/simple")

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.union




external interface A {
var brand: String
}
    


external interface B {
var brand: String
}
    


external interface C {
var brand: String
}
    

typealias AorBorC = Any /* A | B | C */

typealias AorB = Any /* A | B */
    