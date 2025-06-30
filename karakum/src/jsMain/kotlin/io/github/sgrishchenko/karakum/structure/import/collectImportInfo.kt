package io.github.sgrishchenko.karakum.structure.import

import io.github.sgrishchenko.karakum.configuration.Configuration
import io.github.sgrishchenko.karakum.util.recordOrNull
import io.github.sgrishchenko.karakum.util.singleOrNull
import io.github.sgrishchenko.karakum.util.traverse
import js.array.ReadonlyArray
import js.array.component1
import js.array.component2
import js.objects.Object
import js.objects.recordOf
import typescript.*

typealias ImportInfo = Map<Declaration, ReadonlyArray<String>>

private fun handleImportHierarchy(importInfo: ImportInfo): ImportInfo {
    val result = mutableMapOf<Declaration, ReadonlyArray<String>>()

    for ((declaration) in importInfo) {
        var parent: Node? = declaration
        var collectedImports = emptyArray<String>()

        while (parent != null) {
            @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
            val parentImports = importInfo[parent as Declaration] ?: emptyArray()
            collectedImports = parentImports + collectedImports
            parent = parent.parent
        }

        result[declaration] = collectedImports
    }

    return result
}

fun collectImportInfo(
    sourceFiles: ReadonlyArray<SourceFile>,
    configuration: Configuration,
): ImportInfo {
    val importMapper = configuration.importMapper
    val result = mutableMapOf<Declaration, ReadonlyArray<String>>()
    val declarations = mutableSetOf<Declaration>()

    sourceFiles.forEach { sourceFile ->
        traverse(sourceFile) { node ->
            if (isSourceFile(node)) declarations += node
            if (isModuleDeclaration(node)) declarations += node

            if (isImportDeclaration(node)) {
                val moduleSpecifier = node.moduleSpecifier
                val importClause = node.importClause

                if (!isStringLiteral(moduleSpecifier)) return@traverse
                if (importClause == null) return@traverse

                val parent = node.parent

                val declaration = if (isSourceFile(parent)) {
                    parent
                } else if (isModuleBlock(parent)) {
                    parent.parent
                } else {
                    return@traverse
                }

                val moduleName = moduleSpecifier.text
                val imports = result[declaration]?.toMutableList() ?: mutableListOf()

                val importNames = recordOf<String, /* alias */ String>()

                val importClauseName = importClause.name

                if (importClauseName != null) {
                    importNames["default"] = importClauseName.text
                }

                val importClauseNamedBindings = importClause.namedBindings

                if (importClauseNamedBindings != null) {
                    if (isNamespaceImport(importClauseNamedBindings)) {
                        importNames["*"] = importClauseNamedBindings.name.text
                    } else if (isNamedImports(importClauseNamedBindings)) {
                        importClauseNamedBindings.elements.asArray().forEach { element ->
                            val propertyName = element.propertyName

                            if (propertyName != null) {
                                if (isStringLiteral(propertyName)) {
                                    importNames[propertyName.text] = element.name.text
                                } else if (isIdentifier(propertyName)) {
                                    importNames[propertyName.text] = element.name.text
                                }
                            } else {
                                importNames[element.name.text] = element.name.text
                            }
                        }
                    }
                }

                val unhandledImportNames = Object.keys(importNames).toMutableSet()

                for ((moduleNamePattern, packageInfo) in Object.entries(importMapper)) {
                    val moduleNameRegexp = moduleNamePattern.toRegex()

                    if (moduleNameRegexp.containsMatchIn(moduleName)) {
                        val singlePackageName = packageInfo.singleOrNull()
                        val packageRecord = packageInfo.recordOrNull()

                        if (singlePackageName != null) {
                            for ((importName, importAlias) in Object.entries(importNames)) {
                                imports += if (importName == importAlias) {
                                    "import ${singlePackageName}.${importName}"
                                } else {
                                    "import ${singlePackageName}.${importName} as $importAlias"
                                }

                                unhandledImportNames -= importName
                            }
                        } else if (packageRecord != null) {
                            for ((importNamePattern, packageName) in Object.entries(packageRecord)) {
                                val importNameRegexp = importNamePattern.toRegex()

                                for ((importName, importAlias) in Object.entries(importNames)) {
                                    if (importNameRegexp.containsMatchIn(importName) && importName in unhandledImportNames) {
                                        if (packageName.endsWith(".")) {
                                            imports += if (importName === importAlias) {
                                                "import ${packageName}${importName}"
                                            } else {
                                                "import ${packageName}${importName} as $importAlias"
                                            }
                                        } else if (packageName.isNotEmpty()) {
                                            imports += if (importName === importAlias || " as " in packageName) {
                                                "import $packageName"
                                            } else {
                                                "import $packageName as $importAlias"
                                            }
                                        }

                                        unhandledImportNames -= importName
                                    }
                                }
                            }
                        }
                    }
                }

                for (importName in unhandledImportNames) {
                    val importAlias = importNames[importName]

                    imports += if (importName == importAlias) {
                        "// unhandled import: $importName from \"${moduleName}\""
                    } else {
                        "// unhandled import: $importName as $importAlias from \"${moduleName}\""
                    }
                }

                result[declaration] = imports.toTypedArray()
            }
        }
    }

    for (declaration in declarations) {
        if (declaration !in result) {
            result[declaration] = emptyArray()
        }
    }

    return handleImportHierarchy(result)
}
