export declare function useRevalidator(): {
    revalidate: () => void;
};

export declare class MyClass {
    static getDerivedStateFromProps(): {
        error: any;
        location: Location;
    };
}

export interface MyInterface {
    getDerivedStateFromProps(): {
        error: any;
        location: Location;
    };
}

export interface MapRoutePropertiesFunction {
    (route: { value: string }): {
        hasErrorBoundary: boolean;
    }
}
