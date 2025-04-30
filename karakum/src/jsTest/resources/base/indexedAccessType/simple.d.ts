interface URL {}

type AgnosticDataRouteMatch = {
    params: string | number
    matches: string[]
}

type MyTypeWithAccessType = {
    currentUrl: URL;
    currentParams: AgnosticDataRouteMatch["params"];
    matches: AgnosticDataRouteMatch["matches"];
}
