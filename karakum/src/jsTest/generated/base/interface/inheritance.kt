// Automatically generated - do not modify!

@file:JsModule("sandbox-base/interface/inheritance")

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

open external class ParentClass {

}

external class ChildClass : ParentClass, ChildInterface {
override var firstField: String
override var secondField: Double
override var thirdField: String
override var fourthField: Double
override var otherField: Boolean
}
