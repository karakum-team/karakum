function returnsPromiseResult1(): Promise<string> | string;

function returnsPromiseResult2(): string | Promise<string>;

function returnsPromiseResultIgnored(): string | Promise<string>;

class CustomPromise extends Promise<unknown> {}

function returnsCustomPromiseResult(): CustomPromise | unknown;
