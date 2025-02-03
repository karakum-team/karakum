export interface RouteData {
    [routeId: string]: any;
}

export interface ReadonlyRouteData {
    readonly [routeId: string]: any;
}


export interface SomeData {
    [routeId: symbol]: number;
}

export type IntersectionSomeData = { x: string } & {
    [routeId: symbol]: number;
}
