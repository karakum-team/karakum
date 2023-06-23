import {escapeIdentifier} from "../../utils/strings";

export function createPackageName(packageChunks: string[]) {
    return packageChunks
        .map(escapeIdentifier)
        .join(".")
}
