import {StructureItem} from "../structure.js";
import {DerivedDeclaration, DerivedDeclarationStructureItem} from "./derivedDeclaration.js";
import {createBundleInfoItem} from "../bundle/createBundleInfoItem.js";
import {createNamespaceInfoItem} from "../namespace/createNamespaceInfoItem.js";
import {createSourceFileInfoItem} from "../sourceFile/createSourceFileInfoItem.js";
import {applyPackageNameMapper} from "../package/applyPackageNameMapper.js";
import {DerivedFile} from "../../converter/generated.js";
import {ConverterContext} from "../../converter/context.js";
import {ConfigurationService, configurationServiceKey} from "../../converter/plugins/ConfigurationPlugin.js";
import {NamespaceInfoService, namespaceInfoServiceKey} from "../../converter/plugins/NamespaceInfoPlugin.js";
import {ImportInfoService, importInfoServiceKey} from "../../converter/plugins/ImportInfoPlugin.js";

export function generateDerivedDeclarations(
    declarations: DerivedDeclaration[],
    context: ConverterContext,
): DerivedFile[] {
    const configurationService = context.lookupService<ConfigurationService>(configurationServiceKey)
    if (configurationService === undefined) throw new Error("ConfigurationService required")

    const importInfoService = context.lookupService<ImportInfoService>(importInfoServiceKey)
    if (importInfoService === undefined) throw new Error("NamespaceInfoService required")

    const namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)
    if (namespaceInfoService === undefined) throw new Error("NamespaceInfoService required")

    const configuration = configurationService.configuration
    const {granularity} = configuration

    const structureItems: DerivedDeclarationStructureItem[] = declarations.map(generatedInfo => {
        const {sourceFileName, namespace, fileName, body} = generatedInfo
        const imports = importInfoService.resolveImports(sourceFileName, namespace)

        if (granularity === "bundle") {
            const item = createBundleInfoItem(configuration)

            return {
                ...item,
                body,
            }
        }

        let item: StructureItem

        if (namespace && namespaceInfoService.resolveNamespaceStrategy(namespace) === "package") {
            item = createNamespaceInfoItem(namespace, sourceFileName, imports, configuration)
        } else {
            item = createSourceFileInfoItem(sourceFileName, imports, configuration)
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
            imports: item.imports,
            body: item.body,
        }
    })
}
