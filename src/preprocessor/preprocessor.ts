import ts, {SourceFile, TransformerFactory} from "typescript";

export function preprocess(sourceFile: SourceFile, transformers: TransformerFactory<SourceFile>[]): SourceFile {
    const result = ts.transform(sourceFile, transformers);
    const [output] = result.transformed

    return output
}
