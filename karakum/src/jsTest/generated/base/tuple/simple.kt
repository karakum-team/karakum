// Generated by Karakum - do not modify it manually!

@file:JsModule("sandbox-base/tuple/simple")

package sandbox.base.tuple

typealias SimpleEmptyTuple = js.array.Tuple

typealias SimpleTuple1 = js.array.Tuple1<String>

typealias SimpleTuple2 = js.array.Tuple2<String, Double>

typealias SimpleTuple3 = js.array.Tuple3<String, Double, Boolean>

external interface TupleWithRest<A : js.array.ReadonlyArray<Any?>> {
var tuple: js.array.VariadicTuple /* [
    string,
    number,
    ...A,
    boolean
] */
}

typealias TupleWithNames = js.array.Tuple3</* first: */ String, /* second: */ Double, /* third: */ Boolean>
