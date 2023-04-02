export function moduleNameToPackage(moduleName: string): string[] {
    // delimiters
    // "-" - react-router
    // ":" - node:url
    // "." - socket.io
    // "/" - @remix-run/router

    return moduleName
        .split(/[-:.\/]/)
        .map(it => it.replace(/\W/g, ""))
        .filter(it => it !== "")
        .map(it => it.toLowerCase())
}
