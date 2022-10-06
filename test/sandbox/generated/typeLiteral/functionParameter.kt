
@file:JsModule("sandbox/typeLiteral/functionParameter")
@file:JsNonModule

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.typeLiteral



external fun createMemoryRouter(routes: Array<RouteObject>, opts: CreateMemoryRouterOpts = definedExternally): RemixRouter


external class MyClass  {
constructor (options: MyClassOptions)
fun method(options: MyClassMethodOptions): String
}
    
    
external interface CreateMemoryRouterOpts {
var basename: String?
var hydrationData: HydrationState?
var initialEntries: Array<String>?
var initialIndex: Double?
}
        


external interface MyClassOptions {
var first: String
var second: Double
}
        


external interface MyClassMethodOptions {
var third: Boolean
var fourth: Array<String>
}
        