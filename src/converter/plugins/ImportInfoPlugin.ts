import {ConverterPlugin} from "../plugin.js";
import ts, {ModuleDeclaration, Node, Program} from "typescript";
import {ConverterContext} from "../context.js";
import {Render} from "../render.js";
import {GeneratedFile} from "../generated.js";
import {ImportInfo} from "../../structure/import/collectImportInfo.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";

export const importInfoServiceKey = Symbol()

export class ImportInfoService {
    constructor(
        private readonly program: Program,
        private readonly importInfo: ImportInfo,
    ) {
    }

    resolveImports(sourceFileName: string, node: ModuleDeclaration | undefined): string[] {
        if (node) {
            return this.importInfo.get(node) ?? []
        } else {
            const sourcefile = this.program.getSourceFile(sourceFileName)
            if (!sourcefile) return []

            return this.importInfo.get(sourcefile) ?? []
        }
    }
}

export class ImportInfoPlugin implements ConverterPlugin {
    private readonly importInfoService: ImportInfoService;

    constructor(
        program: Program,
        importInfo: ImportInfo
    ) {
        this.importInfoService = new ImportInfoService(program, importInfo);
    }

    generate(): GeneratedFile[] {
        return [];
    }

    render(node: Node, context: ConverterContext, next: Render): string | null {
        if (ts.isImportDeclaration(node)) {
            const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
            checkCoverageService?.deepCover(node)

            return ""
        }

        return null;
    }

    traverse(node: Node): void {
    }

    setup(context: ConverterContext): void {
        context.registerService(importInfoServiceKey, this.importInfoService)
    }
}
