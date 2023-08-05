import childProcess from "node:child_process"
import util from "node:util"
import fs from "node:fs/promises"

interface Schema {
    $schema: string
}

const exec = util.promisify(childProcess.exec)

const granularitySchemaFileName = "schemas/granularity-schema.json"
const namespaceStrategySchemaFileName = "schemas/namespace-strategy-schema.json"
const configurationSchemaFileName = "schemas/karakum-schema.json"

await Promise.all([
    exec(`typescript-json-schema --titles --include src/configuration/configuration.ts --out ${granularitySchemaFileName} tsconfig.json Granularity`),
    exec(`typescript-json-schema --titles --include src/configuration/configuration.ts --out ${namespaceStrategySchemaFileName} tsconfig.json NamespaceStrategy`),
    exec(`typescript-json-schema --titles --include src/configuration/configuration.ts --out ${configurationSchemaFileName} tsconfig.json SchemaConfiguration`),
])

const granularitySchema: Schema = JSON.parse(await fs.readFile(granularitySchemaFileName, "utf8"))
const namespaceStrategySchema: Schema = JSON.parse(await fs.readFile(namespaceStrategySchemaFileName, "utf8"))
const configurationSchema: Schema = JSON.parse(await fs.readFile(configurationSchemaFileName, "utf8"))

const {$schema, ...restConfigurationSchema} = configurationSchema

const resultConfigurationSchema = {
    $schema,
    definitions: {
        Granularity: {
            ...granularitySchema,
            $schema: undefined,
        },
        NamespaceStrategy: {
            ...namespaceStrategySchema,
            $schema: undefined,
        },
    },
    ...restConfigurationSchema,
}

await Promise.all([
    fs.rm(granularitySchemaFileName),
    fs.rm(namespaceStrategySchemaFileName),
    fs.writeFile(configurationSchemaFileName, JSON.stringify(resultConfigurationSchema, null, 4)),
])
