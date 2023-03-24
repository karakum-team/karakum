# Karakum: TypeScript to Kotlin declarations converter

## Description

When you work with some technology today, usually you rely on an ecosystem and frameworks in that technology.
Frontend development is not an exception, [npm](https://www.npmjs.com/) contains tones of useful libraries and
frameworks. Kotlin/JS definitely has means for integration and interoperation with those libraries, but it is a huge and
complicated task to write [external declarations](https://kotlinlang.org/docs/js-interop.html#external-modifier) for
existing libraries. [Karakum](https://github.com/karakum-team/karakum) is a tool for automatic conversion of existing
TypeScript declarations to Kotlin external declaration.
It helps to speed up and simplify adoption of existing npm libraries in your Kotlin/JS project.

If you want to play around with Karakum, you can do it
with [the usage example guide](https://github.com/karakum-team/karakum/blob/master/docs/guides/Basic_usage.md).
Also, Karakum is already used for conversion of some libraries, and those libraries are already used in production.
Here is the list:

* [Remix Router](https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-remix-run-router)
* [React Router](https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-react-router)
* [React Router DOM](https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-react-router-dom)

If you wondered to do something in Kotlin/JS, but you couldn't because you did not have a needed declaration for
some npm library, now you can try to generate missing declarations using Karakum.

## Motivation

There is a [Kotlin Wrappers](https://github.com/JetBrains/kotlin-wrappers) project, where we try to make Kotlin
declarations for popular npm libraries that are used in such projects as [Space](https://www.jetbrains.com/space/) and
[FlySto](https://www.flysto.net/home). Writing Kotlin declarations by hand is complicated task because of next reasons:

* Libraries can be huge, you may have to learn tones of documentation to build quality Kotlin declarations for them.
* It is slow, it can take months (even years) to deliver some typings for a library.
* Existing libraries are being developed, when you convert some library, you need to stay in tune with releases of that
  library and actualize your declaration according to changes there.

That is why we decided to create some tool to make the development and maintenance of Kotlin Wrappers simpler.

## Comparison with existing solutions

You may know that there already is a tool to solve similar problems: [Ducat](https://github.com/Kotlin/dukat). So why do
we need to invent a bicycle? There are some differences between Karakum and Ducat, I will try to list them.

### Flexibility

Karakum is designed to be flexible. Let's consider next code in TypeScript:

```typescript
export declare function Outlet(props: OutletProps): ReactElement | null;
```

If we have no context, we can suppose that equivalent code in Kotlin is something like this:

```kotlin
external fun Outlet(props: OutletProps): ReactElement?
```

But experienced developers may recognize that actually a React component was declared in TypeScript code above.
So as a Kotlin/JS developer, you may want to convert this TypeScript code to this declaration:

```kotlin
external val Outlet: FC<OutletProps>
```

As we can see, the final result of conversion can depend on the context and problem specific of the library you convert.
That is why Karakum is implemented as a pluggable solution. Almost all parts in Karakum have extension points, so you
can customize your conversion and achieve your concrete goals.

### Lightness

If we dig deep and explore Ducat, we can see that it is a compiler, a big independent complete compiler.
The decision to be a compiler has upsides and downsides. The biggest upside, I can see, is that you have full control
of [AST](https://en.wikipedia.org/wiki/Abstract_syntax_tree) that means you literally can construct language
structures and manipulate them as you want, you can collect your own metadata and store it directly in AST nodes
and so on and so forth. But from my prospective, there are downsides, and here is a list of them:

* **Complexity:** To be a compiler, you need to do a lot of work. As a result, you probably need a big team to support
  it.
* **Isolation:** TypeScript is a complex, really complex language because it always evolves. If you have
  your own compiler for it, you need to update it every time TypeScript releases a new version. Moreover, if you
  implement some type checking mechanics, your implementation can be not 100% synchronized with the original
  implementation, so you may face with surprising results in your solution.

Karakum is not a compiler, it is a tool on top of the existing TypeScript compiler. So it still has access to AST
(but it can't modify or extend it), but using the existing solution, it is always in sync with an actual version of
TypeScript language.

### Other advantages

The next couple of things are quite small but neat improvements that the Kotlin Wrappers team was interested in,
and that also was added to Karakum:

* **Package control:** you can define what Kotlin package will be used for generated declarations.
* **File structure:** Karakum can generate output declaration files per TypeScript file or per top-level entity.
* **JS docs:** Karakum tries to preserve it for generated declarations.
* **Imports:** if you convert some library that depends on another converted library, you can explain it to Karakum
  to generate correct imports.

## Advanced features

Already now Karakum can do interesting and useful code manipulations to provide solid type definitions for a converted
library.

### Type literals

In TypeScript, you're able to create types ad-hoc, just in place.
This syntax construction is called a type literal.
Let's consider next example:

```typescript
interface MyInterface {
    myField: { someOption: string }
}
```

It is impossible to convert this type to Kotlin as is, but we can extract type literal to a separate type:

```kotlin
external interface MyInterfaceMyField {
    val someOption: String
}

external interface MyInterface {
    val myField: MyInterfaceMyField
}
```

For now, it was quite simple, but there are more complex situations, let's modify our example:

```typescript
interface MyInterface<T> {
    myField: { someOption: T }
}
```

So now we have to analyze `{ someOption: T }` AST node, find all type references that linked with generic of the outer
type, and declare these generics for a new extracted type:

```kotlin
external interface MyInterfaceMyField<T> {
    val someOption: T
}

external interface MyInterface<T> {
    val myField: MyInterfaceMyField<T>
}
```

It is similar to how IntelliJ IDEA extracts a some block of code to separate function.
This mechanism is added to Karakum now, so it helps a lot in such situations.

### Indexed access types

In TypeScript, there is a feature that allows us to refer to a type of some object field:

```typescript
interface MyInterface {
    myField: string
}

interface AnotherInterface {
    myField: MyInterface["myField"]
}
```

Here we try to say: "I want to declare `AnotherInterface` and I want `myField` be the same type as field `myField`
in `MyInterface`".
Of course, it is impossible to declare exactly the same in Kotlin, but what we can do with it?
TypeScript compiler has a special Type Checker API that can extract type information from an AST node.
Also, Type Checker can build AST node from any type that was found during type checking.
So we can extract a type from `MyInterface["myField"]`, than we can construct AST and convert it to Kotlin using
existing Karakum plugins.
As a result, we will have something like this after conversion:

```kotlin
external interface MyInterface {
    val myField: String
}

external interface AnotherInterface {
    val myField: String
}
```

Yes, we lost the link between `MyInterface` and `AnotherInterface`,
but at least we have valid Kotlin code and probably correct types.

## Conclusion

As you can notice, TypeScript and Kotlin are similar and quite different languages at the same time.
The conversion between these two languages can't be performed ideally (at least for now).
But we can try to explore this area and figure out what can be done and what can't.
Some problems are already solved, some problems still are challenging but can be solved.
But the results of this work already help [FlySto](https://www.flysto.net/home) development team
to build their solution, and, I believe, it may help with adoption of Kotlin/JS in many other projects.
