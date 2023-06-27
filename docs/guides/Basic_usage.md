# Basic usage

First, initialize an npm project:

```shell
npm init -y
```

Then, install Karakum and TypeScript as dev dependencies:

```shell
npm install karakum typescript -D
```

Now we can install the library we want to convert, for the sake of simplicity let's install
[js-file-download](https://github.com/kennethjiang/js-file-download):

```shell
npm install js-file-download
```

Now it is time to configure Karakum, let's create `karakum.config.json` file and write the following:

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

When we execute it, we will see something like this in `generated/jsFileDownload.kt`:

```kotlin
// @formatter:off
@file:JsModule("js-file-download/js-file-download")

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package js.file.download

external object String /* 'js-file-download' */ {
    external fun fileDownload(data: String, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit
    
    external fun fileDownload(data: ArrayBuffer, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit
    
    external fun fileDownload(data: ArrayBufferView, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit
    
    external fun fileDownload(data: Blob, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit
}
// @formatter:on 
```

Alas, there are a couple of problems with this generated file. First of all, we have a strange JS module
`js-file-download/js-file-download`. It was generated this way because we wrote `"libraryName": "js-file-download"`
in our configuration and the file we converted is also named `js-file-download.d.ts`. By default, Karakum preserves
the original file structure of converted TypeScript files. It may be convenient if we're converting a huge library,
and we want to provide an opportunity for a user to import only JS files he/she really uses. But in our case it is
an overkill, so we can just use `js-file-download` as the name for our JS module. To achieve this, add these lines to
`karakum.config.json`:

```diff
{
  "input": "node_modules/js-file-download/js-file-download.d.ts",
  "output": "generated",
  "libraryName": "js-file-download",
+  "moduleNameMapper": {
+    ".*": "js-file-download"
+  }
}
```

This instructs Karakum to replace all symbols in JS module names (regex `.*`) with `js-file-download`.

Another problem is more interesting. We can see some strange construct in the generated code:

```kotlin
external object String /* 'js-file-download' */ {
    // ...
}
```

It is the representation of the following code block in original typings for `js-file-download`:

```typescript
declare module 'js-file-download' {
// ...
}
```

Namespaces (or modules) in TypeScript are used to co-locate some logic. In Kotlin, there is no equivalent for TypeScript
namespaces, but we can't just ignore them during the conversion. Imagine this situation:

```typescript
declare class MyClass {
}

declare namespace MyNamespace {
    class MyClass {
    }
}
```

As you can see, `MyClass` and `MyNamespace.MyClass` are completely different declarations, so if we ignore namespaces
during conversion, we will get something like this in Kotlin:

```kotlin
external class MyClass

external class MyClass
```

It is obviously incorrect code, that is why Karakum tries to emulate TypeScript namespaces using Kotlin objects.
But in our case, the declared namespace is actually global, it just tells TypeScript that there is a JS module
`js-file-download`. Since we already declared this piece of information in `@file:JsModule("js-file-download")`,
we can just skip this namespace declaration. Karakum has the special configuration option for namespace ignoring:

```diff
{
  "input": "node_modules/js-file-download/js-file-download.d.ts",
  "output": "generated",
  "libraryName": "js-file-download",
  "moduleNameMapper": {
    ".*": "js-file-download"
  },
+  "namespaceStrategy": {
+    "js-file-download": "ignore"
+  }
}
```

Now we can run Karakum again, and we should get something like this:

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
