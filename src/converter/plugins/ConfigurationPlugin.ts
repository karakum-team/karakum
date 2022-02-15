import {ConverterPlugin} from "../plugin";
import {Node} from "typescript";
import {ConverterContext} from "../context";
import {Render} from "../render";
import {Configuration} from "../../configuration/configuration";

export const configurationServiceKey = Symbol()

export class ConfigurationService {
    public readonly configuration: Configuration;

    constructor(configuration: Configuration) {
        this.configuration = configuration;
    }
}

export class ConfigurationPlugin implements ConverterPlugin {
    private readonly configurationService: ConfigurationService;

    constructor(configuration: Configuration) {
        this.configurationService = new ConfigurationService(configuration);
    }

    generate(): Record<string, string> {
        return {};
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
