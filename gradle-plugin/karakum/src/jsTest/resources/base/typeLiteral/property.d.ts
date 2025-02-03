declare type FetcherStates<TData = any> = {
    Idle: {
        state: "idle";
        formMethod: undefined;
        formAction: undefined;
        formEncType: undefined;
        formData: undefined;
        data: TData | undefined;
    };
}

declare class FetcherClass<TData = any> {
    state: {
        data: TData | undefined;
    };
}

declare interface FetcherInterface<TData = any> {
    state: {
        data: TData | undefined;
    };
}

declare interface BaseLiteralType {
    one: string
}

declare interface LiteralTypeContainer<TData = any> {
    state: BaseLiteralType & {
        data: TData | undefined;
    };
}
