type AgnosticDataRouteMatch = {
    params: string | number
}

type MyTypeWithAccessType = {
    currentUrl: URL;
    currentParams: AgnosticDataRouteMatch["params"];
}
