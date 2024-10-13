import {PartialConfiguration} from "./configuration/configuration.js";
import {reifyConfiguration} from "./configuration/reifyConfiguration.js";
import {generate} from "./generate.js";

export async function run(partialConfiguration: PartialConfiguration) {
    const configuration = await reifyConfiguration(partialConfiguration)

    await generate(configuration)
}
