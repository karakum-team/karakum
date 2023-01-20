
@file:JsModule("sandbox-base/union/simple")
@file:JsNonModule

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
    "NAME_CONTAINS_ILLEGAL_CHARS",
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
    