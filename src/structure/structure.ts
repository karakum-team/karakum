import ts, {EmitHint, ScriptTarget, SourceFile, Visitor} from "typescript";
import {createTransformer} from "../preprocessor/createTransformer";
import path from "path";
import fs from "fs";

const file = "src/test.d.ts"

const program = ts.createProgram([file], {lib: [], types: []})
const printer = ts.createPrinter({newLine: ts.NewLineKind.LineFeed});

const sourceFiles = [...program.getSourceFiles()]
const outputFiles: SourceFile[] = []

const result = ts.transform(sourceFiles, [
    createTransformer((node, context) => {
        if (ts.isSourceFile(node)) {

            const visit: Visitor = child => {
                if (ts.isClassDeclaration(child) && child.name) {
                    const dir = path.dirname(node.fileName)
                    const fileName = `${child.name.getText(node)}.d.ts`

                    outputFiles.push(
                        ts.createSourceFile(
                            path.join(dir, fileName),
                            printer.printNode(EmitHint.Unspecified, child, node),
                            ScriptTarget.Latest
                        )
                    )

                    return []
                } else {
                    return child
                }
            };

            return ts.visitEachChild(node, visit, context)
        }
    })
]);

const outDir = './dist';

if (fs.existsSync(outDir)) {
    fs.rmdirSync(outDir, {recursive: true});
}

fs.mkdirSync(outDir, {recursive: true});

for (const output of [...result.transformed, ...outputFiles]) {
    const dir = path.dirname(output.fileName)
    const targetDir = path.join(outDir, dir)

    if (!fs.existsSync(targetDir)) {
        fs.mkdirSync(targetDir, {recursive: true});
    }

    fs.writeFileSync(path.join(outDir, output.fileName), printer.printFile(output))
}
