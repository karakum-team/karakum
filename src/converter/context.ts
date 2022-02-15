export interface ConverterContext {
    registerService<T>(key: symbol, service: T): void
    lookupService<T>(key: symbol): T | undefined
}

export function createContext(): ConverterContext {
    const services: Record<symbol, unknown> = {}

    return {
        registerService<T>(key: symbol, service: T) {
            services[key] = service
        },

        lookupService<T>(key: symbol): T | undefined {
            return services[key] as T
        },
    }
}
