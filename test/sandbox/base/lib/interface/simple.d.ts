export interface ExampleInterface {
    firstField: string;
    secondField: number;
}

export interface SimpleInterface {
    firstField: string;
    secondField: number;
    thirdField: ExampleInterface;

    firstMethod(firstParam: string, secondParam: number): void;
    secondMethod(firstParam: string, secondParam: number): boolean;
}
