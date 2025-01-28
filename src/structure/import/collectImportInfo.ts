import ts, {Declaration, SourceFile, Node} from "typescript";
import {Configuration} from "../../configuration/configuration.js";
import {traverse} from "../../utils/traverse.js";

export type ImportInfo = Map<Declaration, string[]>

function handleImportHierarchy(importInfo: ImportInfo): ImportInfo {
    const result: ImportInfo = new Map()

    for (const [declaration] of importInfo) {
        let parent: Node | null = declaration
        let collectedImports: string[] = []

        while (parent != null) {
            const parentImports = importInfo.get(parent as Declaration) ?? []
            collectedImports = parentImports.concat(collectedImports)
            parent = parent.parent
        }

        result.set(declaration, collectedImports)
    }

    return result
}

export function collectImportInfo(
    sourceFiles: SourceFile[],
    configuration: Configuration,
): ImportInfo {
    const {importMapper} = configuration
    const result: ImportInfo = new Map()
    const declarations = new Set<Declaration>()

    sourceFiles.forEach(sourceFile => {
        traverse(sourceFile, node => {
            if (
                ts.isSourceFile(node)
                || ts.isModuleDeclaration(node)
            ) {
                declarations.add(node)
            }

            if (
                ts.isImportDeclaration(node)
                && ts.isStringLiteral(node.moduleSpecifier)
                && node.importClause
            ) {
                let declaration: Declaration

                if (ts.isSourceFile(node.parent)) {
                    declaration = node.parent
                } else if (ts.isModuleBlock(node.parent)) {
                    declaration = node.parent.parent
                } else {
                    return
                }

                const moduleName = node.moduleSpecifier.text
                const imports = result.get(declaration) ?? []

                const importNames: Record<string, /* alias */ string> = {}

                if (node.importClause.name) {
                    importNames.default = node.importClause.name.text
                }

                if (node.importClause.namedBindings) {
                    if (ts.isNamespaceImport(node.importClause.namedBindings)) {
                        importNames["*"] = node.importClause.namedBindings.name.text
                    } else if (ts.isNamedImports(node.importClause.namedBindings)) {
                        node.importClause.namedBindings.elements.forEach(element => {
                            if (element.propertyName) {
                                importNames[element.propertyName.text] = element.name.text
                            } else {
                                importNames[element.name.text] = element.name.text
                            }
                        })
                    }
                }

                const unhandledImportNames = new Set(Object.keys(importNames))

                for (const [moduleNamePattern, packageInfo] of Object.entries(importMapper)) {
                    const moduleNameRegexp = new RegExp(moduleNamePattern)

                    if (moduleNameRegexp.test(moduleName)) {
                        if (typeof packageInfo === "string") {
                            const packageName = packageInfo

                            for (const [importName, importAlias] of Object.entries(importNames)) {
                                if (importName === importAlias) {
                                    imports.push(`import ${packageName}.${importName}`)
                                } else {
                                    imports.push(`import ${packageName}.${importName} as ${importAlias}`)
                                }

                                unhandledImportNames.delete(importName)
                            }
                        } else {
                            for (const [importNamePattern, packageName] of Object.entries(packageInfo)) {
                                const importNameRegexp = new RegExp(importNamePattern)

                                for (const [importName, importAlias] of Object.entries(importNames)) {
                                    if (importNameRegexp.test(importName) && unhandledImportNames.has(importName)) {
                                        if (packageName.endsWith(".")) {
                                            if (importName === importAlias) {
                                                imports.push(`import ${packageName}${importName}`)
                                            } else {
                                                imports.push(`import ${packageName}${importName} as ${importAlias}`)
                                            }
                                        } else if (packageName !== "") {
                                            if (importName === importAlias || packageName.includes(" as ")) {
                                                imports.push(`import ${packageName}`)
                                            } else {
                                                imports.push(`import ${packageName} as ${importAlias}`)
                                            }
                                        }

                                        unhandledImportNames.delete(importName)
                                    }
                                }
                            }
                        }
                    }
                }

                for (const importName of unhandledImportNames) {
                    const importAlias = importNames[importName]

                    if (importName === importAlias) {
                        imports.push(`// unhandled import: ${importName} from "${moduleName}"`)
                    } else {
                        imports.push(`// unhandled import: ${importName} as ${importAlias} from "${moduleName}"`)
                    }
                }

                result.set(declaration, imports)
            }
        })
    })

    for (const declaration of declarations) {
        if (!result.has(declaration)) {
            result.set(declaration, [])
        }
    }

    return handleImportHierarchy(result)
}
