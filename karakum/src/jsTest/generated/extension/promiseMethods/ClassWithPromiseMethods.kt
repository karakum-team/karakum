// Automatically generated - do not modify!

package extension.promiseMethods

external class ClassWithPromiseMethods {
@JsName("returnsPromise1")
fun returnsPromise1Async(): js.promise.Promise<String>

@seskar.js.JsAsync
suspend fun returnsPromise1(): String
@JsName("returnsPromise2")
fun returnsPromise2Async(param: String): js.promise.Promise<Boolean>

@seskar.js.JsAsync
suspend fun returnsPromise2(param: String): Boolean
@JsName("returnsPromise2")
fun returnsPromise2Async(param: Boolean): js.promise.Promise<Boolean>

@seskar.js.JsAsync
suspend fun returnsPromise2(param: Boolean): Boolean
/* should be excluded */


/* should be excluded */




/* should be excluded */

@JsName("returns-promise-3")
fun returnsPromise3Async(): js.promise.Promise<Boolean>

@seskar.js.JsAsync
suspend fun returnsPromise3(): Boolean
fun returnsPromiseIgnored(): js.promise.Promise<String>
@JsName("returnsCustomPromise")
fun returnsCustomPromiseAsync(): CustomPromise

@seskar.js.JsAsync
suspend fun returnsCustomPromise(): Any?
}
