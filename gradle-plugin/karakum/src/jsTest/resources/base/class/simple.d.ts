export class SimpleClass {
    static firstStaticField: string;
    static secondStaticField: number;

    static firstStaticMethod(firstParam: string, secondParam: number): void;
    static secondStaticMethod(firstParam: string, secondParam: number): boolean;

    firstField: string;
    secondField: number;

    firstMethod(firstParam: string, secondParam: number): void;
    secondMethod(firstParam: string, secondParam: number): boolean;
}
