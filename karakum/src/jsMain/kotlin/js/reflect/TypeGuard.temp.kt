// TODO: remove

@file:Suppress(
    "INVISIBLE_REFERENCE",
)

package js.reflect

import kotlin.contracts.contract

@PublishedApi
internal external interface TypeGuardInternal2<in V : JsAny?> {
    operator fun invoke(value: V): Boolean
}

@kotlin.internal.LowPriorityInOverloadResolution
inline operator fun <V : JsAny, T : JsAny> TypeGuard<V, T>.invoke(
    value: V?,
): Boolean {
    contract {
        returns(true) implies (value is T)
    }

    return value != null
            && unsafeCast<TypeGuardInternal2<V>>(this).invoke(value)
}
