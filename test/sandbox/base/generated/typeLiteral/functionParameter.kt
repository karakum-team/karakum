
@file:JsModule("sandbox-base/typeLiteral/functionParameter")
@file:JsNonModule

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
    "NAME_CONTAINS_ILLEGAL_CHARS",
)

package sandbox.base.typeLiteral



external fun createMemoryRouter(routes: Array<RouteObject>, opts: CreateMemoryRouterOpts = definedExternally): RemixRouter


external class MyClass  {
constructor (options: MyClassOptions)
fun method(options: MyClassMethodOptions): String
}
    


external interface ShouldRevalidateFunction {
fun  invoke(args: ShouldRevalidateFunctionArgs): Boolean
}
    

typealias BlockerFunction = (args: BlockerFunctionArgs) -> Boolean
    
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
        


external interface ShouldRevalidateFunctionArgs {
var currentUrl: URL
}
        


external interface BlockerFunctionArgs {
var currentLocation: Location
var nextLocation: Location
var historyAction: HistoryAction
}
        