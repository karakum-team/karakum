class CustomPromise extends Promise<string> {
}

interface InterfaceWithPromiseMethods {
    returnsPromise1(): Promise<string>;

    returnsPromise2(param: string): Promise<boolean>;

    returnsPromise2(param: boolean): Promise<boolean>;

    /* should be excluded */
    returnsPromise2(param: string | boolean): Promise<boolean>;

    "returns-promise-3"(): Promise<boolean>;

    returnsPromiseIgnored(): Promise<string>;

    returnsCustomPromise(): CustomPromise;
}

class ClassWithPromiseMethods {
    returnsPromise1(): Promise<string>;

    returnsPromise2(param: string): Promise<boolean>;

    returnsPromise2(param: boolean): Promise<boolean>;

    /* should be excluded */
    returnsPromise2(param: string | boolean): Promise<boolean>;

    "returns-promise-3"(): Promise<boolean>;

    returnsPromiseIgnored(): Promise<string>;

    returnsCustomPromise(): CustomPromise;
}
