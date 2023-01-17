
@file:JsModule("sandbox-base/interface/inheritance")
@file:JsNonModule

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
    "NAME_CONTAINS_ILLEGAL_CHARS",
)

package sandbox.base.`interface`




external interface FirstParentInterface {
var firstField: String
var secondField: Double
}
    


external interface SecondParentInterface {
var thirdField: String
var fourthField: Double
}
    


external interface ChildInterface : FirstParentInterface, SecondParentInterface {
var otherField: Boolean
}
    
    