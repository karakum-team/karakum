import {KOTLIN_KEYWORDS} from "../../converter/constants";

export function createPackageName(packageChunks: string[]) {
    return packageChunks
        .map(it => {
            if (KOTLIN_KEYWORDS.has(it)) {
                return `\`${it}\``
            } else {
                return it
            }
        })
        .join(".")
}
