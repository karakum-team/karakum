import {exec} from "node:child_process"

exec("typescript-json-schema --titles --include src/configuration/configuration.ts --out schemas/granularity-schema.json tsconfig.json Granularity")
exec("typescript-json-schema --titles --include src/configuration/configuration.ts --out schemas/namespace-strategy-schema.json tsconfig.json NamespaceStrategy")
exec("typescript-json-schema --titles --include src/configuration/configuration.ts --out schemas/karakum-schema.json tsconfig.json SchemaConfiguration")
