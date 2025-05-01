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

declare class ParentClass {

}

declare class ChildClass extends ParentClass implements ChildInterface {
    firstField: string;
    secondField: number;
    thirdField: string;
    fourthField: number;
    otherField: boolean;
}
