export interface ExampleInterface {
    firstField: string;
    secondField: number;
}

export interface GenericInterface<T, U extends ExampleInterface> {
    firstField: T;
    secondField: number;

    firstMethod(firstParam: string, secondParam: U): void;
}

export interface InterfaceWithGenericMethod {
    genericMethod<T, U extends ExampleInterface>(firstParam: T, secondParam: number): void;
}

export interface ChildGenericInterface<T> extends GenericInterface<T, ExampleInterface> {
}
