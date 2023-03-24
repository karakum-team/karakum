# Simple usage

At first, initialize npm project:

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

And now we need to make the last step, let's go through namespace members and convert each of them. `render` callback,
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
