declare type BaseAgnosticBaseRouteObject = {
    params: string | undefined
}

declare type AgnosticBaseRouteObject = {
    handle1?: any;
    handle2?: unknown;
    handle3?: string | undefined;
    handle4?: null;
    handle5?: undefined;
    handle6?;
    handle7: BaseAgnosticBaseRouteObject["params"] | undefined;
};

declare class AgnosticBaseRouteClass {
    handle1?: any;
    handle2?: unknown;
    handle3?: string | undefined;
    handle4?: null;
    handle5?: undefined;
    handle6?;
    handle7: BaseAgnosticBaseRouteObject["params"] | undefined;
}
