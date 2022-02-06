export interface ExampleInterface {
    firstField: string;
    secondField: number;
}

export interface HelloWorldInterface {
    firstField: string;
    secondField: number;
    thirdField: ExampleInterface;

    firstMethod(firstParam: string, secondParam: number): void;
    secondMethod(firstParam: string, secondParam: number): boolean;
}

export function helloWorld(firstParam: string, secondParam: number): HelloWorldInterface;
