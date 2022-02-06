export interface FirstParentInterface {
    firstField: string;
    secondField: number;
}

export interface SecondParentInterface {
    thirdField: string;
    fourthField: number;
}

export interface ChildInterface extends FirstParentInterface, SecondParentInterface {
    otherField: boolean;
}
