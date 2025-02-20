interface A { brand: "one" }

interface B { brand: "two" }

interface C { brand: "three" }

export function exampleFn(firstParam: A | B, secondParam: B | C, thirdParam: A | B | C): boolean;

export function otherExampleFn(firstParam: A | B, secondParam: B | C, thirdParam?: A | B | C): boolean;

export function oneMoreExampleFn(firstParam: A | B, secondParam?: B | C, thirdParam?: A | B | C): boolean;

export function nullableOptionalExampleFn(firstParam: A | B, secondParam?: B | undefined): boolean;
