type TestConditional<S> = S extends () => infer P ? Promise<P> : Promise<void>;

type TestNullableConditional<S> = S extends () => infer P ? Promise<P> | null : Promise<void>;
