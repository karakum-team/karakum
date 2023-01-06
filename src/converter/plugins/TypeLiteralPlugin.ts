import {ConverterPlugin} from "../plugin";
import ts from "typescript";
import {ConverterContext} from "../context";
import {Render} from "../render";
import {capitalize} from "../../utils/strings";
import {generateOutputFileInfo} from "../../structure/generateOutputFileInfo";
import {createGeneratedFile} from "../../structure/createGeneratedFile";
import {ConfigurationService, configurationServiceKey} from "./ConfigurationPlugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import path from "path";

export class TypeLiteralPlugin implements ConverterPlugin {
    private counter = 0
    private generated: Record<string, { name: string, declaration: string }[]> = {}

    constructor(private sourceFileRoot: string) {
    }

    generate(context: ConverterContext): Record<string, string> {
        const configurationService = context.lookupService<ConfigurationService>(configurationServiceKey)
        const configuration = configurationService?.configuration
        if (configuration === undefined) throw new Error("TypeLiteralPlugin can't work without ConfigurationService")

        const output = configuration.output
        const granularity = configuration.granularity ?? "file"

        return Object.fromEntries(
            Object.entries(this.generated)
                .flatMap(([sourceFileName, declarations]) => {
                    const {outputFileName, packageName} = generateOutputFileInfo(
                        this.sourceFileRoot,
                        sourceFileName,
                        configuration,
                    );

                    if (granularity === "top-level") {
                        return declarations
                            .map(({name, declaration}) => {
                                const outputDirName = path.dirname(outputFileName)
                                const currentOutputFileName = `${outputDirName}/${name}.kt`
                                const generatedFile = createGeneratedFile(
                                    this.sourceFileRoot,
                                    currentOutputFileName,
                                    packageName,
                                    declaration,
                                    configuration,
                                )

                                return [path.resolve(output, currentOutputFileName), generatedFile];
                            })
                    } else {
                        const generatedFile = declarations
                            .map(({declaration}) => declaration)
                            .join("\n\n")

                        return [[path.resolve(output, outputFileName), generatedFile]];
                    }
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

            if (ts.isCallSignatureDeclaration(node.parent.parent)) {
                if (ts.isInterfaceDeclaration(node.parent.parent.parent)) {
                    parentName = node.parent.parent.parent.name?.text ?? ""
                }
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

        if (ts.isPropertySignature(node.parent)) {
            let propertyName = ""

            if (ts.isIdentifier(node.parent.name)) {
                propertyName = node.parent.name.text ?? ""
            }

            let parentName = ""

            if (
                ts.isTypeLiteralNode(node.parent.parent)
                && ts.isTypeAliasDeclaration(node.parent.parent.parent)
            ) {
                parentName = node.parent.parent.parent.name.text ?? ""
            }

            if (parentName || propertyName) {
                generatedName = `${capitalize(parentName)}${capitalize(propertyName)}`
            }
        }

        const fileName = node.getSourceFile().fileName
        const generatedDeclarations = this.generated[fileName] ?? []

        const members = node.members
            .map(member => next(member))
            .join("\n")

        const declaration = `
external interface ${generatedName} {
${members}
}
        `

        generatedDeclarations.push({
            name: generatedName,
            declaration,
        })

        this.generated[fileName] = generatedDeclarations

        return generatedName;
    }

    setup(context: ConverterContext): void {
    }

    traverse(node: ts.Node, context: ConverterContext): void {
    }

}
