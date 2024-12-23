export interface InterfaceWithTrickyField {
    "_hasFetcherDoneAnything"?: boolean
    " _hasFetcherDoneAnything "?: boolean
    "123test"?: boolean
    "kebab-case-test"?: boolean
    "123-invalid-kebab-case-test"?: boolean
    42: boolean

    "_hasFetcherDoneAnything2"(): boolean
    " _hasFetcherDoneAnything2 "(): boolean
    "123test2"(): boolean
    "kebab-case-test2"(): boolean
    "123-invalid-kebab-case-test2"(): boolean
    43(): boolean

    get "_hasFetcherDoneAnything3"(): boolean
    get " _hasFetcherDoneAnything3 "(): boolean
    get "123test3"(): boolean
    get "kebab-case-test3"(): boolean
    get "123-invalid-kebab-case-test3"(): boolean
    get 44(): boolean
}

export declare class ClassWithTrickyField {
    "_hasFetcherDoneAnything"?: boolean
    " _hasFetcherDoneAnything "?: boolean
    "123test"?: boolean
    "kebab-case-test"?: boolean
    "123-invalid-kebab-case-test"?: boolean
    42: boolean

    "_hasFetcherDoneAnything2"(): boolean
    " _hasFetcherDoneAnything2 "(): boolean
    "123test2"(): boolean
    "kebab-case-test2"(): boolean
    "123-invalid-kebab-case-test2"(): boolean
    43(): boolean

    get "_hasFetcherDoneAnything3"(): boolean
    set "_hasFetcherDoneAnything3"(value: boolean)
    get " _hasFetcherDoneAnything3 "(): boolean
    set " _hasFetcherDoneAnything3 "(value: boolean)
    get "123test3"(): boolean
    set "123test3"(value: boolean)
    get "kebab-case-test3"(): boolean
    set "kebab-case-test3"(value: boolean)
    get "123-invalid-kebab-case-test3"(): boolean
    set "123-invalid-kebab-case-test3"(value: boolean)
    get 44(): boolean
    set 44(value: boolean)
}
