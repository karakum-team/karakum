# Open issues

## Union types

Union types are always a pain for the Kotlin type system.
Sometimes we can do something about it, for example, if we have a function with a union type parameter,
we can break this union type down to separate types and add an overload with these types.
For the following TypeScript code:

```typescript
declare function myFunction(param: string | number)
```

it may look like this in Kotlin

```kotlin
external fun myFunction(param: String)

external fun myFunction(param: Double)
```

But unfortunately, most of the time it's not possible to solve the problem with unions this way.
The following code isn't related to function overloading:

```typescript
interface MyInterface {
    myField: string | number
}

declare const MY_CONST: string | number
```

For such places we have to use `Any` type in Kotlin.
But there is an area where union types can be emulated.
Let's consider the following code:

```typescript
interface A {
    a: string
}

interface B {
    b: string
}

type C = A | B
```

In Kotlin, we can emulate this using inheritance:

```kotlin
external interface C

external interface A : C {
    val a: String
}

external interface A : B {
    val b: String
}
```

Karakum can't do this for you right now, but it seems it would be a good improvement.

## Declaration merging

TypeScript [can merge declarations](https://www.typescriptlang.org/docs/handbook/declaration-merging.html) from separate
AST nodes in a single type entity.
It is similar to how [Kotlin extensions](https://kotlinlang.org/docs/extensions.html) works.
Now Karakum can't recognize such declarations to generate a single declaration from several TypeScript AST nodes,
but it can be implemented using dedicated Type Checker API.

## Type predicates

If you have a custom checker function for some JS structure, you can express it in TypeScript using
[Type predicates](https://www.typescriptlang.org/docs/handbook/2/narrowing.html#using-type-predicates).
In Kotlin, we can emulate it
using [Contracts](https://github.com/Kotlin/KEEP/blob/master/proposals/kotlin-contracts.md),
but it is not so simple.
Kotlin contracts are implemented now as the first expression in a function body, but external functions don't have a body.
Also, Kotlin doesn't allow us to write contracts for external classes and interfaces.
Of course, the first expression limitation of contracts can be hacked using a thin wrapper,
and the compiler check for external interface can be suppressed:

```kotlin
@Suppress(
    "CANNOT_CHECK_FOR_EXTERNAL_INTERFACE",
)
fun isBinaryExpression(node: Node): Boolean {
    contract {
        returns(true) implies (node is BinaryExpression)
    }

    return typescript.raw.isBinaryExpression(node)
}
```

But right now Karakum can't do this for you, maybe at some point in the future it will become smart enough to do it.

## Integration with the standard library

It would be nice if Karakum could generate imports for such entities as `ArrayBuffer` or `Promise` automatically,
but it is harder than it may seem. Technically, you can declare functions, classes or interfaces with names that 
clash with the names in the ECMAScript standard environment. The following trick is possible: we can check if some 
entity is imported in a file. If it's not, it is probably some standard thing, and we can try to find an import for it in
[kotlin-js](https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-js) or
[kotlin-browser](https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-browser).
Unfortunately, it doesn't work this way in Karakum right now, but it is possible to implement.
