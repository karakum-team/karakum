function returnsPromise1(): Promise<string>;

function returnsPromise2(param: string): Promise<boolean>;

function returnsPromise2(param: boolean): Promise<boolean>;

/* should be excluded */
function returnsPromise2(param: string | boolean): Promise<boolean>;

function returnsPromiseIgnored(): Promise<string>;

class CustomPromise extends Promise<string> {}

function returnsCustomPromise(): CustomPromise;
