interface AgnosticBaseRouteObject {}

export declare type AgnosticNonIndexRouteObject = AgnosticBaseRouteObject & {
    children?: string[];
    index?: false;
};
