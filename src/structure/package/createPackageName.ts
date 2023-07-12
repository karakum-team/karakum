import {escapeIdentifier} from "../../utils/strings.js";

export function createPackageName(packageChunks: string[]) {
    return packageChunks
        .map(escapeIdentifier)
        .join(".")
}
