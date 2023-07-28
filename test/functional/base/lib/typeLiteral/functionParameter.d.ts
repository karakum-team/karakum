export declare function createMemoryRouter(routes: RouteObject[], opts?: {
    basename?: string;
    hydrationData?: HydrationState;
    initialEntries?: string[];
    initialIndex?: number;
}): RemixRouter;


export declare class MyClass {
    constructor(options: { first: string, second: number })

    method(options: { third: boolean, fourth: string[] }): string
}

export interface MyInterface {
    method(options: { first: boolean, second: string[] }): string
}

export interface ShouldRevalidateFunction {
    (args: {
        currentUrl: URL;
    }): boolean;
}

export declare type BlockerFunction = (args: {
    currentLocation: Location;
    nextLocation: Location;
    historyAction: HistoryAction;
}) => boolean;
