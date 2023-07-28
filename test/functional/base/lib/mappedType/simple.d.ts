type Keys = "one" | "two"

type OptionsFlags = {
    [Property in Keys]: Promise<Property>;
}

type ReadonlyOptionsFlags = {
    readonly [Property in Keys]: Promise<Property>;
}

type OptionsFlagsWithTypeLiteral = { three: string } & {
    [Property in Keys]: Promise<Property>;
}

interface KeyWrapper<T> {
    key: T
}

type NamedOptionsFlags = {
    [Property in Keys as KeyWrapper<Property>]: Promise<Property>;
}

type OptionalOptionsFlags = {
    readonly [Property in Keys as KeyWrapper<Property>]: Promise<Property> | undefined;
}
