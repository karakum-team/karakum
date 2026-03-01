package io.github.sgrishchenko.karakum.base.inheritanceModifiers

import io.github.sgrishchenko.karakum.extension.match
import io.github.sgrishchenko.karakum.extension.resolve
import io.github.sgrishchenko.karakum.extension.withFile
import io.github.sgrishchenko.karakum.extension.withName
import typescript.isClassDeclaration

val modifyClassInheritance = resolve(
    "open" to match {
        setOf(
            "GrandparentWithConstructor",
            "ParentWithoutConstructor",
            "ParentWithConstructor",
        ).forEach {
            match(::isClassDeclaration, withFile("**/class/parentConstructors.d.ts"), withName(it))
        }

        match(
            ::isClassDeclaration,
            withFile("**/interface/inheritance.d.ts"),
            withName("ParentClass")
        )
    }
)
