export declare class MyClass {
    conflictHandler?: (conflictType: { third: boolean, fourth: string[] }) => boolean;

    method(cb: (options: { third: boolean, fourth: string[] }) => void): string
}

export interface MyInterface {
    conflictHandler?: (conflictType: { first: boolean, second: string[] }) => boolean;

    method(cb: (options: { first: boolean, second: string[] }) => void): string
}
