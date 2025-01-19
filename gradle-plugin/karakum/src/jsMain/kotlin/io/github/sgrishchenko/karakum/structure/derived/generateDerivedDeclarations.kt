package io.github.sgrishchenko.karakum.structure.derived

import io.github.sgrishchenko.karakum.extension.ConverterContext
import io.github.sgrishchenko.karakum.extension.DerivedFile
import io.github.sgrishchenko.karakum.extension.plugins.ConfigurationService
import io.github.sgrishchenko.karakum.extension.plugins.configurationServiceKey
import js.array.ReadonlyArray

fun generateDerivedDeclarations(
    declarations: ReadonlyArray<DerivedDeclaration>,
    context: ConverterContext,
): ReadonlyArray<DerivedFile> {
    val configurationService = context.lookupService<ConfigurationService>(configurationServiceKey)
    if (configurationService == null) error("ConfigurationService required")

    val importInfoService = context.lookupService<ImportInfoService>(importInfoServiceKey)
    if (importInfoService == null) error("NamespaceInfoService required")

    val namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)
    if (namespaceInfoService == null) error("NamespaceInfoService required")

    val configuration = configurationService.configuration
    val granularity = configuration.granularity

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
