type HydrationState = string

export declare function createMemoryRouter(routes: string[], opts?: {
    basename?: string;
    hydrationData?: HydrationState;
    initialEntries?: string[];
    initialIndex?: number;
}): void;


export declare class MyClassWithLiteralInParameter {
    constructor(options: { first: string, second: number })

    method(options: { third: boolean, fourth: string[] }): string
}

export interface MyInterfaceWithLiteralInParameter {
    method(options: { first: boolean, second: string[] }): string
}

export interface ShouldRevalidateFunction {
    (args: {
        currentUrl: string;
    }): boolean;
}

export declare type BlockerFunction = (args: {
    currentLocation: string;
    nextLocation: string;
    historyAction: () => void;
}) => boolean;
