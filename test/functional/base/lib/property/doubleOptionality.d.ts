declare type SomeNullableType = string | null

declare type SomeUnionNullableType = number | string | null

declare type BaseAgnosticBaseRouteObject = {
    params: string | undefined
    params2?: string
    params3: SomeNullableType
    params4: SomeUnionNullableType
}

declare type AgnosticBaseRouteObject = {
    handle1?: any;
    handle2?: unknown;
    handle3?: string | undefined;
    handle4?: null;
    handle5?: undefined;
    handle6?;
    handle7: BaseAgnosticBaseRouteObject["params"] | undefined;
    handle8?: BaseAgnosticBaseRouteObject["params2"];
    handle9?: SomeNullableType;
    handle10?: BaseAgnosticBaseRouteObject["params3"];
    handle11?: SomeUnionNullableType;
    handle12?: BaseAgnosticBaseRouteObject["params4"];
};

declare class AgnosticBaseRouteClass {
    handle1?: any;
    handle2?: unknown;
    handle3?: string | undefined;
    handle4?: null;
    handle5?: undefined;
    handle6?;
    handle7: BaseAgnosticBaseRouteObject["params"] | undefined;
    handle8?: BaseAgnosticBaseRouteObject["params2"];
    handle9?: SomeNullableType;
    handle10?: BaseAgnosticBaseRouteObject["params3"];
    handle11?: SomeUnionNullableType;
    handle12?: BaseAgnosticBaseRouteObject["params4"];
}
