
@file:JsModule("js-file-download")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package js.file.download



external fun fileDownload(data: String, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit

external fun fileDownload(data: ArrayBuffer, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit

external fun fileDownload(data: ArrayBufferView, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit

external fun fileDownload(data: Blob, filename: String, mime: String = definedExternally, bom: String = definedExternally): Unit
    