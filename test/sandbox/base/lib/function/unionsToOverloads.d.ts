interface A { brand: "one" }

interface B { brand: "two" }

interface C { brand: "three" }

export function exampleFn(firstParam: A | B, secondParam: B | C, thirdParam: A | B | C): boolean;
