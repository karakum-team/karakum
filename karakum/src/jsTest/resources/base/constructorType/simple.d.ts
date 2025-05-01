interface ConstructorTypeInstance {}

type SimpleEmptyConstructor = new () => ConstructorTypeInstance

type SimpleConstructor1 = new (firstArg: string) => ConstructorTypeInstance

type SimpleConstructor2 = new (firstArg: string, secondArg: number) => ConstructorTypeInstance

type SimpleConstructor3 = new (firstArg: string, secondArg: number, thirdOptionalArg?: boolean) => ConstructorTypeInstance

type ConstructorWithRest = new (firstArg: string, secondArg: number, ...rest: unknown[]) => ConstructorTypeInstance
