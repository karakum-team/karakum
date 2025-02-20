type HydrationState = string

export declare function createMemoryRouter(routes: RouteObject[], opts?: {
    basename?: string;
    hydrationData?: HydrationState;
    initialEntries?: string[];
    initialIndex?: number;
}): RemixRouter;


export declare class MyClassWithTypeLiteral {
    constructor(options: { first: string, second: number })

    method(options: { third: boolean, fourth: string[] }): string
}
