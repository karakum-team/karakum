class DeepMapEntry<K, V> {
    value: V | undefined
    isInitialized: boolean = false
    children: Map<K, DeepMapEntry<K, V>> = new Map()

    entries(prefix: K[]): [K[], V][] {
        const children = [...this.children.entries()]
            .flatMap(([key, value]) => {
                return value.entries([...prefix, key])
            })

        if (this.isInitialized) {
            const current: [K[], V] = [prefix, this.value as V]
            return [current, ...children]
        } else {
            return children
        }
    }

    keys(prefix: K[]): K[][] {
        return this.entries(prefix).map(([key]) => key)
    }

    values(prefix: K[]): V[] {
        return this.entries(prefix).map(([, value]) => value)
    }
}

export class DeepMap<K, V> {
    private root: DeepMapEntry<K, V> = new DeepMapEntry()

    clear(): void {
        this.root = new DeepMapEntry()
    }

    delete(key: K[]): void {
        let current: DeepMapEntry<K, V> = this.root

        for (const keyItem of key) {
            const child = current.children.get(keyItem)
            if (child === undefined) return
            current = child
        }

        current.value = undefined
        current.isInitialized = false
    }

    get(key: K[]): V | undefined {
        let current: DeepMapEntry<K, V> = this.root

        for (const keyItem of key) {
            const child = current.children.get(keyItem)
            if (child === undefined) return
            current = child
        }

        return current.value
    }

    has(key: K[]): boolean {
        let current: DeepMapEntry<K, V> = this.root

        for (const keyItem of key) {
            const child = current.children.get(keyItem)
            if (child === undefined) return false
            current = child
        }

        return current.isInitialized
    }

    set(key: K[], value: V): void {
        let current: DeepMapEntry<K, V> = this.root

        for (const keyItem of key) {
            let child = current.children.get(keyItem)
            if (child === undefined) {
                child = new DeepMapEntry()
                current.children.set(keyItem, child)
            }
            current = child
        }

        current.value = value
        current.isInitialized = true
    }

    entries(): [K[], V][] {
        return this.root.entries([])
    }

    keys(): K[][] {
        return this.root.keys([])
    }

    values(): V[] {
        return this.root.values([])
    }
}
