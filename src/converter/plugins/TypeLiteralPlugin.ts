import {ConverterPlugin} from "../plugin";
import ts from "typescript";
import {ConverterContext} from "../context";
import {Render} from "../render";
import {capitalize} from "../../utils/strings";
import {generateOutputFileInfo} from "../../structure/generateOutputFileInfo";
import {ConfigurationService, configurationServiceKey} from "./ConfigurationPlugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import path from "path";

export class TypeLiteralPlugin implements ConverterPlugin {
    private counter = 0
    private generated: Record<string, string[]> = {}

    constructor(private sourceFileRoot: string) {
    }

    generate(context: ConverterContext): Record<string, string> {
        const configurationService = context.lookupService<ConfigurationService>(configurationServiceKey)

        const output = configurationService?.configuration?.output
        if (output === undefined) throw new Error("Output should be defined in configuration")

        const libraryName = configurationService?.configuration?.libraryName ?? ""
        const libraryNameOutputPrefix = configurationService?.configuration?.libraryNameOutputPrefix ?? false
        const packageNameMapper = configurationService?.configuration?.packageNameMapper

        return Object.fromEntries(
            Object.entries(this.generated)
                .map(([fileName, declarations]) => {
                    const {outputFileName} = generateOutputFileInfo(
                        this.sourceFileRoot,
                        fileName,
                        libraryName,
                        libraryNameOutputPrefix,
                        packageNameMapper,
                    );
                    return [path.resolve(output, outputFileName), declarations.join("\n\n")];
                })
        );
    }

    render(node: ts.Node, context: ConverterContext, next: Render): string | null {
        if (!ts.isTypeLiteralNode(node)) return null

        const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
        checkCoverageService?.cover(node)

        let generatedName = `Temp${this.counter++}`

        if (ts.isParameter(node.parent)) {
            let parameterName = ""

            if (ts.isIdentifier(node.parent.name)) {
                parameterName = node.parent.name.text ?? ""
            }

            let parentName = ""

            if (ts.isFunctionDeclaration(node.parent.parent)) {
                parentName = node.parent.parent.name?.text ?? ""
            }

            if (ts.isConstructorDeclaration(node.parent.parent)) {
                parentName = node.parent.parent.parent.name?.text ?? ""
            }

            if (ts.isMethodDeclaration(node.parent.parent)) {
                const methodName = node.parent.parent.name.getText()
                let className = ""

                if (ts.isClassDeclaration(node.parent.parent.parent)) {
                    className = node.parent.parent.parent.name?.text ?? ""
                }

                parentName = `${capitalize(className)}${capitalize(methodName)}`
            }

            if (parentName || parameterName) {
                generatedName = `${capitalize(parentName)}${capitalize(parameterName)}`
            }
        }

        const fileName = node.getSourceFile().fileName
        const generatedDeclarations = this.generated[fileName] ?? []

        const members = node.members
            .map(member => next(member))
            .join("\n")

        generatedDeclarations.push(`
external interface ${generatedName} {
${members}
}
        `)

        this.generated[fileName] = generatedDeclarations

        return generatedName;
    }

    setup(context: ConverterContext): void {
    }

    traverse(node: ts.Node, context: ConverterContext): void {
    }

}
