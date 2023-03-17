# Karakum: TypeScript to Kotlin declarations converter

## Description

When you work with some technology today, usually you rely on an ecosystem and frameworks in that technology.
Frontend development is not an exception, [npm](https://www.npmjs.com/) contains tones of useful libraries and
frameworks. Kotlin/JS definitely has means for integration and interoperation with those libraries, but it is a huge and
complicated task to write [external declarations](https://kotlinlang.org/docs/js-interop.html#external-modifier) for
existing libraries. [Karakum](https://github.com/karakum-team/karakum) is a tool for automatic conversion of existing
TypeScript declarations to Kotlin external declaration.
It helps to speed up and simplify adoption of existing npm libraries in your Kotlin/JS project.

## Motivation

There is a [kotlin-wrappers](https://github.com/JetBrains/kotlin-wrappers) project, where we try to make Kotlin
declarations for popular npm libraries that are used in such projects as [Space](https://www.jetbrains.com/space/) and
[FlySto](https://www.flysto.net/home). Writing Kotlin declarations by hand is complicated task because of next reasons:

* Libraries can be huge, you may have to learn tones of documentation to build quality Kotlin declarations for them.
* It is slow, it can take months (even years) to deliver some typings for a library.
* Existing libraries are being developed, when you convert some library, you need to stay in tune with releases of that
  library and actualize your declaration according to changes there.

That is why we decided to create some tool to make the development and maintenance of kotlin-wrappers simpler.

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
(but it can't modify or extend it), but using the existing solution, it always in sync with an actual version of
TypeScript language.

### Other advantages

The next couple of things are quite small but neat improvements that the kotlin-wrappers team was interested in,
and that also was added to Karakum:

* **Package control:** you can define what Kotlin package will be used for generated declarations.
* **File structure:** Karakum can generate output declaration files per TypeScript file or per top-level entity.
* **JS docs:** Karakum tries to preserve it for generated declarations.
* **Imports:** if you convert some library that depends on another converted library, you can explain it to Karakum
  to generate correct imports.

## Usage example

If you want to play around with Karakum, you can use it like a command line tool. At first, initialize npm project:

```shell
npm init -y
```

Next install Karakum and TypeScript as a dev dependencies:

```shell
npm install karakum typescript -D
```

Now we can install the library we want to convert, for simplicity I propose to install
[js-file-download](https://github.com/kennethjiang/js-file-download):

```shell
npm install js-file-download
```

Now it is time to the configuration, let's create `karakum.config.json` file and write this down in it:

```json
{
  "input": "node_modules/js-file-download/js-file-download.d.ts",
  "output": "generated",
  "libraryName": "js-file-download"
}
```

After that, we can run Karakum to generate Kotlin declarations for us:

```shell
npx karakum --config karakum.config.json
```

When we execute it, we will receive something like that in `generated/jsFileDownload.kt`:

```kotlin
// @formatter:off
@file:JsModule("js-file-download/js-file-download")

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package js.file.download

external object String {
    external fun fileDownload(data: String, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit
    
    external fun fileDownload(data: ArrayBuffer, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit
    
    external fun fileDownload(data: ArrayBufferView, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit
    
    external fun fileDownload(data: Blob, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit
}
// @formatter:on 
```

But there are a couple of problems with this generated file. First of all, we have a strange JS module
`js-file-download/js-file-download`. It was generated this way because we wrote `"libraryName": "js-file-download"`
in our configuration and the file we converted also is named `js-file-download.d.ts`. By default, Karakum preserves
an original file structure of converted TypeScript files. It may be convenient if we're converting a huge library,
and we want to provide an opportunity for a user to import only JS files he/she really uses. But in our case it is
an overkill, so we can to use just `js-file-download` as name for our JS module, to achieve it just add these lines to
`karakum.config.json`:

```diff
{
  "input": "node_modules/js-file-download/js-file-download.d.ts",
  "output": "generated",
  "libraryName": "js-file-download",
+  "moduleNameMapper": {
+    ".*": ""
+  }
}
```

This instructs Karakum to remove all symbols (regex `.*`) after `libraryName` in JS module.

Another problem is more interesting. We can see some strange construction in generated code:

```kotlin
external object String {
    // ...
}
```

It is the representation of this code block in original typings for `js-file-download`:

```typescript
declare module 'js-file-download' {
// ...
}
```

Namespaces (or modules) in TypeScript are used to colocate some logic. In Kotlin, there is no an analog for TypeScript
namespaces, but we can't just ignore them during conversion. Imagine this situation:

```typescript
declare class MyClass {
}

declare namespace MyNamespace {
    class MyClass {
    }
}
```

As you can see `MyClass` and `MyNamespace.MyClass` are completely different declarations, so if we ignore namespaces
during conversion, we will receive something like this in Kotlin:

```kotlin
external class MyClass

external class MyClass
```

It is obviously an incorrect code, that is why Karakum tries to emulate TypeScript namespaces using Kotlin objects.
But in our case, the declared namespace is actually global, it just says to Typescript that there is JS module
`js-file-download`. But we already declared this piece of information in `@file:JsModule("js-file-download")`,
so we can just skip this namespace declaration. Now we can write a simple plugin for Karakum to customize this
behaviour. Let's create `karakum/plugins/convertNamespace.js` file in our project and write next code there:

```javascript
const ts = require("typescript");

module.exports = (node, context, render) => {
    return null
}
```

Here is the declaration of simplest Karakum plugin. In Karakum, plugins are applied to each AST node one by one,
so if a particular plugin doesn't know what to do with passed AST node, it can just return `null` to say:
"It is not my area of responsibility to handle this type of AST nodes, just try to use some another plugin to convert
it". Right now our plugin does know nothing about any AST node. To handle some type of nodes, let's use
Typescript to select some node by its kind:

```diff
const ts = require("typescript");

module.exports = (node, context, render) => {
+    if (ts.isModuleDeclaration(node)) {
+
+    }

  return null
}
```

In TypeScript, a body of namespace can be another namespace or list of body members. I propose not to consider the case
with embedded namespaces and to handle only flat namespaces:

```diff
const ts = require("typescript");

module.exports = (node, context, render) => {
-   if (ts.isModuleDeclaration(node)) {
+   if (ts.isModuleDeclaration(node) && ts.isModuleBlock(node.body)) {

  }

  return null
}
```

And now we need to make a last step, let's go through namespace members and convert each of them. `render` callback,
that we have as the last parameter in our plugin function is some kind of continuation, it receives AST node, passes it
through the plugin chain and returns a string as the result of conversion. Knowing that, we can write this code:

```diff
const ts = require("typescript");

module.exports = (node, context, render) => {
  if (ts.isModuleDeclaration(node) && ts.isModuleBlock(node.body)) {
+     return node.body.statements
+         .map(statement => render(statement))
+         .join("\n")
  }

  return null
}
```

Now we can run Karakum again, and we should receive something like this:

```kotlin
// @formatter:off
@file:JsModule("js-file-download")

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package js.file.download

external fun fileDownload(data: String, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit

external fun fileDownload(data: ArrayBuffer, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit

external fun fileDownload(data: ArrayBufferView, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit

external fun fileDownload(data: Blob, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit
// @formatter:on 
```

If you want to know more about Karakum or see examples of more complex projects, you can visit next places:

* [Remix Router](https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-remix-run-router)
* [React Router](https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-react-router)
* [React Router DOM](https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-react-router-dom)

## Interesting problems

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

It is similar to how IntelliJ IDEA extracts some block of code to separate function.
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

## Open issues

Union types are always a pain for the Kotlin type system.
Sometimes we can do something with it, for example, if we have a function with a union type parameter,
we can break this union type down to separate types and add overload with these types.
For next TypeScript code:

```typescript
declare function myFunction(param: string | number)
```

it may look like this in Kotlin

```kotlin
external fun myFunction(param: String)

external fun myFunction(param: Double)
```

But unfortunately, most of the time it's not possible to solve the problem with unions this way.
Next code isn't related to function overloading:

```typescript
interface MyInterface {
    myField: string | number
}

declare const MY_CONST: string | number
```

For such places we have to use `Any` type in Kotlin.
But there is an area where union types can be emulated.
Let's consider next code:

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

### Declaration merging

TypeScript [can merge declarations](https://www.typescriptlang.org/docs/handbook/declaration-merging.html) from separate
AST nodes in a single type entity.
It is similar to how [Kotlin extensions](https://kotlinlang.org/docs/extensions.html) works.
Now Karakum can't recognize such declarations and generate single declaration from few TypeScript AST nodes,
but it can be implemented using dedicated Type Checker API.

### Type predicates

If you have a custom checker function for some JS structure, you can express it in Typescript using
[Type predicates](https://www.typescriptlang.org/docs/handbook/2/narrowing.html#using-type-predicates).
In Kotlin, we can emulate it
using [Contracts](https://github.com/Kotlin/KEEP/blob/master/proposals/kotlin-contracts.md),
but it is not so simple.
Kotlin contracts are implemented now as the first expression in a function body, but external functions don't have body.
Also, Kotlin doesn't allow us to write contracts for external classes and interfaces.
Of course, the first expression limitation of contracts can be hacked using a thin wrapper,
and compiler check for external interface can be suppressed:

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

## Conclusion

As you can notice, TypeScript and Kotlin are similar and quite different languages at the same time.
The conversion between these two languages can't be performed ideally (at least for now).
But we can try to explore this area and figure out what can be done and what can't.
Some problems are already solved, some problems still are challenging but can be solved.
But the results of this work already help [FlySto](https://www.flysto.net/home) development team
to build their solution, and, I believe, it may help with adoption of Kotlin/JS in many other projects.
