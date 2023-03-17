# Karakum: TypeScript to Kotlin declarations converter

## Description

When you work with some technology today, usually you rely on an ecosystem and frameworks in that technology.
Frontend development is not an exception, [npm](https://www.npmjs.com/) contains tones of useful libraries and
frameworks. Kotlin/JS definitely has means for integration and interoperation with those libraries, but it is a huge and
complicated task to write [external declarations](https://kotlinlang.org/docs/js-interop.html#external-modifier) for
existing libraries. [Karakum](https://github.com/karakum-team/karakum) is a tool for automatic conversion of existing
TypeScript declarations to Kotlin external
declaration. It helps to speed up and simplify adoption of existing npm libraries in your Kotlin/JS project.

## Motivation

There is a [kotlin-wrappers](https://github.com/JetBrains/kotlin-wrappers) project, where we try to make Kotlin
declarations for popular npm libraries that are used in such projects as [Space](https://www.jetbrains.com/space/) and
[FlySto](https://www.flysto.net/home). Writing Kotlin declarations by hand is complicated task because of text reasons:

* Libraries can be huge, you may have to learn tones of documentation to build quality Kotlin declarations for them.
* It is slow, it can take months (even years) to deliver some typings for a library.
* Existing libraries are being developed, when you convert some library, you need to stay in tune with releases of that
  library and actualize your declaration according to changes there.

That is why we decided to create some tool to make the development and maintenance of kotlin-wrappers simpler.

## Comparison with existing solutions

You may know that there already is a tool to solve similar problems: [Ducat](https://github.com/Kotlin/dukat). So why do
we need to invent a bicycle? There are some differences between Karakum and Ducat, I will try to list them.

### Flexibility

Karakum is designed to be flexible. Lest consider next code in TypeScript:

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
(but it can't modify or extend it), but using the existing solution, I'm always in sync with an actual version of
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

When we execute it, we will receive something like that in `generated/jsFileDownload.kt`

```kotlin
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
so we can just skip this namespace declaration. Now we can write a simple plugin for Karakum to customize this behaviour.
Let's create `karakum/plugins/convertNamespace.js` file in our project and write next code there:

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
with embedded namespaces and to handle only plain namespaces:

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
@file:JsModule("js-file-download")

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package js.file.download

external fun fileDownload(data: String, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit

external fun fileDownload(data: ArrayBuffer, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit

external fun fileDownload(data: ArrayBufferView, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit

external fun fileDownload(data: Blob, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit
```

If you want to know more about Karakum or see examples of more complex projects, you can visit next places:

* [Remix Router](https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-remix-run-router)
* [React Router](https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-react-router)
* [React Router DOM](https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-react-router-dom)

## Interesting problems

```kotlin
// TODO
```
