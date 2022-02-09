
external interface FirstParentInterface  {
var firstField: String
var secondField: Double
}
    

external interface SecondParentInterface  {
var thirdField: String
var fourthField: Double
}
    

external interface ChildInterface  : FirstParentInterface, SecondParentInterface {
var otherField: Boolean
}
    