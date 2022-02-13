import ts from "typescript";
import {convert} from "./converter/converter";
import {Configuration} from "./configuration/configuration";
import {glob} from "glob";
// import {preprocess} from "./preprocessor/preprocessor";
// import {createMergeInterfacesTransformer} from "./preprocessor/transformers/mergeInterfacesTransformer";
import fs from "fs";
import path from "path";
import {commonPrefix} from "./utils/commonPrefix";

function generateOutputFileName(prefix: string, sourceFileName: string) {
    return sourceFileName
        .replace(prefix, "")
        .replace(/\.d\.ts$/, ".kt")
        .replace(/\.ts$/, ".kt")
}

function generateRelativeFileName(prefix: string, sourceFileName: string) {
    return sourceFileName
        .replace(prefix, "")
        .replace(/\.d\.ts$/, "")
        .replace(/\.ts$/, "")
}

export function process(configuration: Configuration) {
    const {input, output, libraryName = "", compilerOptions} = configuration

    const normalizedInput = typeof input === "string" ? [input] : input

    const rootNames = normalizedInput.flatMap(pattern => glob.sync(pattern))

    const preparedCompilerOptions = {
        lib: [],
        types: [],
        ...compilerOptions
    }

    const compilerHost = ts.createCompilerHost(preparedCompilerOptions, /* setParentNodes */ true);

    const program = ts.createProgram(rootNames, preparedCompilerOptions, compilerHost)

    console.log(`Source files count: ${program.getSourceFiles().length}`)

    const sources = ([] as string[][]).concat(
        normalizedInput.map(pattern => pattern.split("/")),
        rootNames.map(fileName => fileName.split("/")),
    )

    const prefix = commonPrefix(...sources).join("/") + "/"

    console.log(`Source files root: ${prefix}`)

    fs.rmdirSync(output, {recursive: true})
    fs.mkdirSync(output, {recursive: true})

    program.getSourceFiles().forEach(sourceFile => {
        console.log(`Source file: ${sourceFile.fileName}`)

        const targetFileName = path.resolve(output, generateOutputFileName(prefix, sourceFile.fileName))

        console.log(`Target file: ${targetFileName}`)

        // const preprocessedFile = preprocess(sourceFile, [
        //     createMergeInterfacesTransformer(program),
        // ])

        const relativeFileName = generateRelativeFileName(prefix, sourceFile.fileName)

        const convertedFile = convert(libraryName, relativeFileName, sourceFile)

        fs.mkdirSync(path.dirname(targetFileName), {recursive: true})
        fs.writeFileSync(targetFileName, convertedFile, {})
    })
}
