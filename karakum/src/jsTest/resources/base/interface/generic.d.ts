export interface ExampleBoundInterface {
    firstField: string;
    secondField: number;
}

export interface GenericInterface<T, U extends ExampleBoundInterface> {
    firstField: T;
    secondField: number;

    firstMethod(firstParam: string, secondParam: U): void;
}

export interface InterfaceWithGenericMethod {
    genericMethod<T, U extends ExampleBoundInterface>(firstParam: T, secondParam: number): void;
}

export interface ChildGenericInterface<T> extends GenericInterface<T, ExampleBoundInterface> {
}
