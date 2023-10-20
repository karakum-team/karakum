
@file:JsModule("sandbox-base/union/nullable")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.union



typealias X1 = String

typealias Y1 = Double

typealias Z1 = Any /* X1 | Y1 */

typealias X2 = String?

typealias Y2 = Double

typealias Z2 = Any? /* X2 | Y2 */
    