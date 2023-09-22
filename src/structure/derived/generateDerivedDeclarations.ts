import {ModuleDeclaration} from "typescript";
import {Configuration, NamespaceStrategy} from "../../configuration/configuration.js";
import {StructureItem} from "../structure.js";
import {DerivedDeclaration, DerivedDeclarationStructureItem} from "./derivedDeclaration.js";
import {createBundleInfoItem} from "../bundle/createBundleInfoItem.js";
import {createNamespaceInfoItem} from "../namespace/createNamespaceInfoItem.js";
import {createSourceFileInfoItem} from "../sourceFile/createSourceFileInfoItem.js";
import {applyPackageNameMapper} from "../package/applyPackageNameMapper.js";
import {DerivedFile} from "../../converter/generated.js";

export function generateDerivedDeclarations(
    declarations: DerivedDeclaration[],
    configuration: Configuration,
    resolveNamespaceStrategy: (node: ModuleDeclaration) => NamespaceStrategy
): DerivedFile[] {
    const {granularity} = configuration

    const structureItems: DerivedDeclarationStructureItem[] = declarations.map(generatedInfo => {
        const {sourceFileName, namespace, fileName, body} = generatedInfo

        if (granularity === "bundle") {
            const item = createBundleInfoItem(configuration)

            return {
                ...item,
                body,
            }
        }

        let item: StructureItem

        if (namespace && resolveNamespaceStrategy(namespace) === "package") {
            item = createNamespaceInfoItem(namespace, sourceFileName, configuration)
        } else {
            item = createSourceFileInfoItem(sourceFileName, configuration)
        }

        if (granularity === "top-level") {
            return {
                ...item,
                fileName,
                body,
            }
        } else {
            return {
                ...item,
                body,
            }
        }
    })

    return structureItems.map(item => {
        const packageMappingResult = applyPackageNameMapper(
            item.package,
            item.fileName,
            configuration,
        )

        return {
            package: packageMappingResult.package,
            fileName: packageMappingResult.fileName,
            body: item.body,
        }
    })
}