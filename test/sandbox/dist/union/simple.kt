
@file:JsModule("sandbox/union/simple")
@file:JsNonModule

package sandbox.union

typealias A = /* { brand: "one" } */
typealias B = /* { brand: "two" } */
typealias C = /* { brand: "three" } */
typealias AorBorC = Any /* A | B | C */
typealias AorB = Any /* A | B */
    