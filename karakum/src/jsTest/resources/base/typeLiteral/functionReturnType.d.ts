export declare function useRevalidator(): {
    revalidate: () => void;
};

export declare class MyClassWithLiteralInReturn {
    static getDerivedStateFromProps(): {
        error: any;
        location: string;
    };
}

export interface MyInterfaceWithLiteralInReturn {
    getDerivedStateFromProps(): {
        error: any;
        location: string;
    };
}

export interface MapRoutePropertiesFunction {
    (route: { value: string }): {
        hasErrorBoundary: boolean;
    }
}
