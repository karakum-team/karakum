export interface GeneratedFile {
    fileName: string,
    body: string,
}

export interface DerivedFile extends GeneratedFile {
    package: string[],
    imports: string[],
}

export function isDerivedFile(generatedFile: GeneratedFile): generatedFile is DerivedFile {
    return (
        "package" in generatedFile
        && Array.isArray(generatedFile.package)
        && "imports" in generatedFile
        && Array.isArray(generatedFile.imports)
    )
}
