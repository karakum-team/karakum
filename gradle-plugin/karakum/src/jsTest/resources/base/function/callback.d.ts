type Callback = (error: string) => void

export function higherOrderFn(cb: Callback): boolean;
