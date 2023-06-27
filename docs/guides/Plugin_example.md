# Plugin example (WIP)

Now we can write a simple plugin for Karakum to customize this behavior. Let's
create `karakum/plugins/convertNamespace.js` file in our project and write the following code there:

```javascript
const ts = require("typescript");

module.exports = (node, context, render) => {
    return null
}
```

Here is the declaration of a simplest Karakum plugin. In Karakum, plugins are applied to each AST node one by one,
so if a particular plugin doesn't know what to do with the passed AST node, it can just return `null` to say:
"It is not my area of responsibility to handle this type of AST nodes, just try to use some other plugin to convert
it". Right now our plugin knows nothing about any AST node. To handle some type of nodes, let's use
TypeScript to select some node by its kind:

```diff
const ts = require("typescript");

module.exports = (node, context, render) => {
+    if (ts.isModuleDeclaration(node)) {
+
+    }

  return null
}
```

In TypeScript, a body of a namespace can be another namespace or a list of members. Let's forget about embedded
namespaces for now and handle only flat namespaces:

```diff
const ts = require("typescript");

module.exports = (node, context, render) => {
-   if (ts.isModuleDeclaration(node)) {
+   if (ts.isModuleDeclaration(node) && ts.isModuleBlock(node.body)) {

  }

  return null
}
```

And now we need to make the last step: let's go through namespace members and convert each of them. `render` callback,
that we have as the last parameter in our plugin function is some kind of continuation, it receives an AST node, passes
it through the plugin chain and returns a string as the conversion result. Knowing that, we can write this code:

```diff
const ts = require("typescript");

module.exports = (node, context, render) => {
  if (ts.isModuleDeclaration(node) && ts.isModuleBlock(node.body)) {
+     return node.body.statements
+         .map(statement => render(statement))
+         .join("\n")
  }

  return null
}
```
