import path from "node:path";
import {StructureItem} from "../structure.js";
import {Configuration} from "../../configuration/configuration.js";
import {createSourceFileInfoItem} from "../sourceFile/createSourceFileInfoItem.js";

export function createBundleInfoItem(
    configuration: Configuration,
): StructureItem {
    const {outputFileName} = configuration

    const fileName = outputFileName
        ? path.basename(outputFileName)
        : "bundle.kt"

    const syntheticItem = createSourceFileInfoItem("", configuration)

    return {
        ...syntheticItem,
        fileName,
    }
}
