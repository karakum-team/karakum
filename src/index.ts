import ts, {EmitHint, SyntaxKind, Type} from "typescript";
import {traverse} from "./utils/traverse";
import {createMergeInterfacesTransformer} from "./preprocessor/transformers/mergeInterfacesTransformer";
import {preprocess} from "./preprocessor/preprocessor";
import fs from "fs";
import {convert} from "./converter/converter";

// const file = "node_modules/typescript/lib/typescript.d.ts"
// const file = "node_modules/typescript/lib/lib.webworker.iterable.d.ts"
const file = "src/test.d.ts"
const program = ts.createProgram([file], {lib: []})
const printer = ts.createPrinter({newLine: ts.NewLineKind.LineFeed});
const typeChecker = program.getTypeChecker()
const sourceFile = program.getSourceFile(file)

if (!sourceFile) throw new Error("Source file is not defined")

const preprocessedFile = preprocess(sourceFile, [
    createMergeInterfacesTransformer(typeChecker),
])

fs.writeFileSync("typescript.d.ts", printer.printFile(preprocessedFile))

const result = convert(preprocessedFile)

fs.writeFileSync("typescript.kt", result)

// traverse(sourceFile, node => {
//     if (node.kind === SyntaxKind.ObjectBindingPattern) {
//         console.log(printer.printNode(EmitHint.Unspecified, node, sourceFile))
//     }
// })

/*

let unionsCount = 0
let stringUnionsCount = 0
let optionalUnionsCount = 0
let referenceUnionsCount = 0
let arrayUnionsCount = 0
let stringNodeUnionsCount = 0
let withFinalUnionsCount = 0
let restUnionsCount = 0

traverse(sourceFile, node => {
    // if (ts.isStringLiteral(node) && node.parent.kind === SyntaxKind.LiteralType) {
    //     stringLiteralParents.add(node.parent.parent.kind)
    //     console.log(printer.printNode(EmitHint.Unspecified, node.parent.parent, sourceFile))
    // }

    if (ts.isUnionTypeNode(node)) {
        unionsCount++

        if (
            node.types.length === 2 &&
            node.types[1].kind === SyntaxKind.UndefinedKeyword
        ) {
            optionalUnionsCount++
        } else if (node.types.every(typeNode => {
            return (
                typeNode.kind === SyntaxKind.TypeReference ||
                typeNode.kind === SyntaxKind.UndefinedKeyword
            );
        })) {
            referenceUnionsCount++
        } else if (node.types.every(typeNode => {
            return typeNode.kind === SyntaxKind.ArrayType;
        })) {
            arrayUnionsCount++
        } else if (node.types.every(typeNode => {
            return (
                ts.isLiteralTypeNode(typeNode) &&
                typeNode.kind === SyntaxKind.LiteralType
            );
        })) {
            stringUnionsCount++
        } else if (
            node.getText() === "string | Identifier" ||
            node.getText() === "string | Identifier | undefined" ||
            node.getText() === "string | NodeArray<JSDocComment>" ||
            node.getText() === "string | NodeArray<JSDocComment> | undefined"
        ) {
            stringNodeUnionsCount++
        } else if (
            node.types[0].kind === SyntaxKind.BooleanKeyword ||
            node.types[0].kind === SyntaxKind.StringKeyword ||
            node.types[0].kind === SyntaxKind.NumberKeyword ||
            node.types[node.types.length - 1].kind === SyntaxKind.StringKeyword ||
            node.types[node.types.length - 1].kind === SyntaxKind.NumberKeyword
        ) {
            withFinalUnionsCount++
        } else {
            restUnionsCount++
            console.log(node.getText())
            // node.types.forEach(typeNode => console.log(SyntaxKind[typeNode.kind]))
        }
    }
})

console.log("unionsCount", unionsCount)
console.log("stringUnionsCount", stringUnionsCount)
console.log("optionalUnionsCount", optionalUnionsCount)
console.log("referenceUnionsCount", referenceUnionsCount)
console.log("arrayUnionsCount", arrayUnionsCount)
console.log("stringNodeArrayUnionsCount", stringNodeUnionsCount)
console.log("withFinalUnionsCount", withFinalUnionsCount)
console.log("restUnionsCount", restUnionsCount)

// VariableDeclarationList | readonly VariableDeclaration[] <- with final
// T | T[] | undefined <- ???
// CallHierarchyItem | CallHierarchyItem[] | undefined <- ???
// CodeActionCommand | CodeActionCommand[] <- ???
// ApplyCodeActionCommandResult | ApplyCodeActionCommandResult[] <- ???
// CodeActionCommand | CodeActionCommand[] <- ???
// ApplyCodeActionCommandResult | ApplyCodeActionCommandResult[]
// SignatureHelpTriggerCharacter | ")" <- string
// T | T[] <- ???
// VariableDeclarationList | readonly VariableDeclaration[] <- with final

*/
