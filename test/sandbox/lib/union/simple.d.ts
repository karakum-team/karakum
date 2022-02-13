interface A { brand: "one" }

interface B { brand: "two" }

interface C { brand: "three" }

type AorBorC = A | B | C

type AorB = A | B
