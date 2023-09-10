import {ConverterPlugin} from "../plugin.js";
import {Node} from "typescript";
import {ConverterContext} from "../context.js";
import {Render} from "../render.js";
import {Configuration} from "../../configuration/configuration.js";
import {GeneratedFile} from "../generated.js";

export const configurationServiceKey = Symbol()

export class ConfigurationService {
    constructor(readonly configuration: Configuration) {
    }
}

export class ConfigurationPlugin implements ConverterPlugin {
    private readonly configurationService: ConfigurationService;

    constructor(configuration: Configuration) {
        this.configurationService = new ConfigurationService(configuration);
    }

    generate(): GeneratedFile[] {
        return [];
    }

    render(node: Node, context: ConverterContext, next: Render): string | null {
        return null;
    }

    traverse(node: Node): void {
    }

    setup(context: ConverterContext): void {
        context.registerService(configurationServiceKey, this.configurationService)
    }
}
