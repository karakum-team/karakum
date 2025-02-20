package io.github.sgrishchenko.karakum.structure.derived

import io.github.sgrishchenko.karakum.configuration.Granularity
import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.extension.ConverterContext
import io.github.sgrishchenko.karakum.extension.DerivedFile
import io.github.sgrishchenko.karakum.extension.plugins.*
import io.github.sgrishchenko.karakum.structure.StructureItem
import io.github.sgrishchenko.karakum.structure.bundle.createBundleInfoItem
import io.github.sgrishchenko.karakum.structure.namespace.createNamespaceInfoItem
import io.github.sgrishchenko.karakum.structure.`package`.applyPackageNameMapper
import io.github.sgrishchenko.karakum.structure.sourceFile.createSourceFileInfoItem
import js.array.ReadonlyArray

@OptIn(ExperimentalJsExport::class)
@JsExport
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

    val structureItems: List<DerivedDeclarationStructureItem> = declarations.map { generatedInfo ->
        val sourceFileName = generatedInfo.sourceFileName
        val namespace = generatedInfo.namespace
        val fileName = generatedInfo.fileName
        val body = generatedInfo.body

        val imports = importInfoService.resolveImports(sourceFileName, namespace)

        if (granularity == Granularity.bundle) {
            val item = createBundleInfoItem(configuration)

            return@map DerivedDeclarationStructureItem(
                fileName = item.fileName,
                `package` = item.`package`,
                moduleName = item.moduleName,
                qualifier = item.qualifier,
                hasRuntime = item.hasRuntime,
                imports = item.imports,
                body = body,
            )
        }

        val item: StructureItem =
            if (
                namespace != null
                && namespaceInfoService.resolveNamespaceStrategy(namespace) == NamespaceStrategy.`package`
            ) {
                createNamespaceInfoItem(namespace, sourceFileName, imports, configuration)
            } else {
                createSourceFileInfoItem(sourceFileName, imports, configuration)
            }

        if (granularity == Granularity.topLevel) {
            DerivedDeclarationStructureItem(
                fileName = fileName,
                `package` = item.`package`,
                moduleName = item.moduleName,
                qualifier = item.qualifier,
                hasRuntime = item.hasRuntime,
                imports = item.imports,
                body = body,
            )
        } else {
            DerivedDeclarationStructureItem(
                fileName = item.fileName,
                `package` = item.`package`,
                moduleName = item.moduleName,
                qualifier = item.qualifier,
                hasRuntime = item.hasRuntime,
                imports = item.imports,
                body = body,
            )
        }
    }

    return structureItems
        .map { item ->
            val packageMappingResult = applyPackageNameMapper(
                item.`package`,
                item.fileName,
                configuration,
            )

            DerivedFile(
                `package` = packageMappingResult.`package`,
                fileName = packageMappingResult.fileName,
                imports = item.imports,
                body = item.body,
            )
        }
        .toTypedArray()
}
