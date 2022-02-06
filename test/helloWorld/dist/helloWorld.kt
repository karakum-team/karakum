
        external interface ExampleInterface   {
            var firstField: String
var secondField: Double
        }
    

        external interface HelloWorldInterface   {
            var firstField: String
var secondField: Double
var thirdField: ExampleInterface
fun firstMethod(firstParam: String, secondParam: Double): Unit
fun secondMethod(firstParam: String, secondParam: Double): Boolean
        }
    
external fun helloWorld(firstParam: String, secondParam: Double): HelloWorldInterface