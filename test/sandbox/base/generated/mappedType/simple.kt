
@file:JsModule("sandbox-base/mappedType/simple")

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.mappedType




@Suppress(
    "NAME_CONTAINS_ILLEGAL_CHARS",
    "NESTED_CLASS_IN_EXTERNAL_INTERFACE",
)
@JsName("""(/*union*/{one: 'one', two: 'two'}/*union*/)""")
sealed external interface Keys {
companion object {
val one: Keys
val two: Keys
}
}
        


external interface OptionsFlags {


@Suppress(
    "DEPRECATION",
    "NATIVE_INDEXER_KEY_SHOULD_BE_STRING_OR_NUMBER",
)
@nativeGetter
operator fun <Property : Keys> get(key: Property): Promise<Property>?
    


@Suppress(
    "DEPRECATION",
    "NATIVE_INDEXER_KEY_SHOULD_BE_STRING_OR_NUMBER",
)
@nativeSetter
operator fun <Property : Keys> set(key: Property, value: Promise<Property>?)
        
    
}
        


external interface ReadonlyOptionsFlags {


@Suppress(
    "DEPRECATION",
    "NATIVE_INDEXER_KEY_SHOULD_BE_STRING_OR_NUMBER",
)
@nativeGetter
operator fun <Property : Keys> get(key: Property): Promise<Property>?
    
    
}
        


external interface OptionsFlagsWithTypeLiteral {


@Suppress(
    "DEPRECATION",
    "NATIVE_INDEXER_KEY_SHOULD_BE_STRING_OR_NUMBER",
)
@nativeGetter
operator fun <Property : Keys> get(key: Property): Promise<Property>?
    


@Suppress(
    "DEPRECATION",
    "NATIVE_INDEXER_KEY_SHOULD_BE_STRING_OR_NUMBER",
)
@nativeSetter
operator fun <Property : Keys> set(key: Property, value: Promise<Property>?)
        
    
var three: String
}
        


external interface KeyWrapper<T> {
var key: T
}
    


external interface NamedOptionsFlags {


@Suppress(
    "DEPRECATION",
    "NATIVE_INDEXER_KEY_SHOULD_BE_STRING_OR_NUMBER",
)
@nativeGetter
operator fun <Property : Keys> get(key: KeyWrapper<Property>): Promise<Property>?
    


@Suppress(
    "DEPRECATION",
    "NATIVE_INDEXER_KEY_SHOULD_BE_STRING_OR_NUMBER",
)
@nativeSetter
operator fun <Property : Keys> set(key: KeyWrapper<Property>, value: Promise<Property>?)
        
    
}
        


external interface OptionalOptionsFlags {


@Suppress(
    "DEPRECATION",
    "NATIVE_INDEXER_KEY_SHOULD_BE_STRING_OR_NUMBER",
)
@nativeGetter
operator fun <Property : Keys> get(key: KeyWrapper<Property>): Promise<Property>?
    
    
}
        
    