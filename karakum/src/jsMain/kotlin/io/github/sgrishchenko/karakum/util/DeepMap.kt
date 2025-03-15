package io.github.sgrishchenko.karakum.util

import js.array.JsTuple2
import js.array.ReadonlyArray
import js.array.tupleOf

private class DeepMapEntry<K, V> {
    var value: V? = null
    var isInitialized = false
    val children = mutableMapOf<K, DeepMapEntry<K, V>>()

    fun entries(prefix: ReadonlyArray<K>): ReadonlyArray<JsTuple2<ReadonlyArray<K>, V>> {
        val children = children.flatMap { (key, value) ->
            value.entries(prefix + key).asIterable()
        }.toTypedArray()

        if (isInitialized) {
            @Suppress("UNCHECKED_CAST")
            val current = tupleOf(prefix, value as V)
            return arrayOf(current) + children
        } else {
            return children
        }
    }

    fun keys(prefix: ReadonlyArray<K>): ReadonlyArray<ReadonlyArray<K>> {
        return entries(prefix).map { (key) -> key }.toTypedArray()
    }

    fun values(prefix: ReadonlyArray<K>): ReadonlyArray<V> {
        return entries(prefix).map { (_, value) -> value }.toTypedArray()
    }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
class DeepMap<K, V> {
    private var root = DeepMapEntry<K, V>()

    fun clear() {
        root = DeepMapEntry()
    }

    fun delete(key: ReadonlyArray<K>) {
        var current = root

        for (keyItem in key) {
            val child = current.children[keyItem] ?: return
            current = child
        }

        current.value = null
        current.isInitialized = false
    }

    @JsExport.Ignore
    operator fun minusAssign(key: ReadonlyArray<K>) = delete(key)

    operator fun get(key: ReadonlyArray<K>): V? {
        var current = root

        for (keyItem in key) {
            val child = current.children[keyItem] ?: return null
            current = child
        }

        return current.value
    }

    fun has(key: ReadonlyArray<K>): Boolean {
        var current = root

        for (keyItem in key) {
            val child = current.children[keyItem] ?: return false
            current = child
        }

        return current.isInitialized
    }

    @JsExport.Ignore
    operator fun contains(key: ReadonlyArray<K>): Boolean = has(key)

    operator fun set(key: ReadonlyArray<K>, value: V) {
        var current = root

        for (keyItem in key) {
            var child = current.children[keyItem]
            if (child == null) {
                child = DeepMapEntry()
                current.children[keyItem] = child
            }
            current = child
        }

        current.value = value
        current.isInitialized = true
    }

    @JsExport.Ignore
    operator fun plusAssign(pair: Pair<ReadonlyArray<K>, V>) = set(pair.first, pair.second)

    fun entries() = root.entries(emptyArray())

    fun keys() = root.keys(emptyArray())

    fun values() = root.values(emptyArray())
}
