export function simpleVararg(...args: string[]): void

export function genericVararg<T extends string[]>(...args: T): void

export function callbackVararg(fn: (...args: string[]) => void): void
