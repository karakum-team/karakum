export interface Path {
    pathname: string;
    search: string;
    hash: string;
}

export declare function createPath({ pathname, search, hash, }: Path): string;
