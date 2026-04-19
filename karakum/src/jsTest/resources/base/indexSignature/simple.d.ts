export interface RouteData {
    other: string;
    [routeId: string]: any;
}

export interface ReadonlyRouteData {
    other: string;
    readonly [routeId: string]: any;
}


export interface SomeData {
    [routeId: symbol]: number;
}

export type IntersectionSomeData = { x: string } & {
    [routeId: symbol]: number;
}

export interface SomeDataWithMerging {
    [routeId: symbol]: number;
}

export interface SomeDataWithMerging {
    other: string;
}

export interface SomeDataWithClassMerging {
    [routeId: symbol]: number;
}

export class SomeDataWithClassMerging {}

export interface SomeDataParent {
    other: string;
}

export interface SomeDataWithParent extends SomeDataParent {
    [routeId: symbol]: number;
}

export interface SomeDataWithGeneric<T> {
    [routeId: symbol]: T;
}

type SomeDataAlias = { [routeId: symbol]: number }

type SomeDataContainer = {
    data: { [routeId: symbol]: number }
}
