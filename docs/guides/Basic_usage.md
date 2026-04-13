# Basic usage

First, initialize an npm project:

```shell
npm init -y
```

Then, install Karakum and TypeScript as dev dependencies:

```shell
npm install karakum typescript -D
```

Now you can install the library you want to convert. As an example, install
[js-file-download](https://github.com/kennethjiang/js-file-download):

```shell
npm install js-file-download
```

Now it is time to configure Karakum. To do this create `karakum.config.json` file and write the following:

```json
{
  "libraryName": "js-file-download",
  "input": "js-file-download.d.ts",
  "output": "generated"
}
```

After that, you can run Karakum to generate some Kotlin declarations:

```shell
npx karakum --config karakum.config.json
```

After running it, you should see something like this in `generated/fileDownload.kt`:

```kotlin
// @formatter:off
// Automatically generated - do not modify!

@file:JsModule("js-file-download")

package js.file.download

external fun fileDownload(data: String, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit

external fun fileDownload(data: js.buffer.ArrayBuffer, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit

external fun fileDownload(data: js.buffer.ArrayBufferView, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit

external fun fileDownload(data: web.blob.Blob, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit
// @formatter:on 
```

This example demonstrates that Karakum can produce output with minimal configuration, though the result has a one flaw:
`ArrayBufferView` is referenced without passing the type parameter.
To fix these issues, you can inspect existing [examples](../../examples).
Also, if you want to write Karakum plugins in Kotlin, [Gradle example](../../examples/gradle) may be helpful.
