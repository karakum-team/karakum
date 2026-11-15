package io.github.sgrishchenko.karakum.base.inheritanceModifiers

import io.github.sgrishchenko.karakum.extension.match
import io.github.sgrishchenko.karakum.extension.resolve
import io.github.sgrishchenko.karakum.extension.withFile
import io.github.sgrishchenko.karakum.extension.withName
import typescript.ast.`is`.isClassDeclaration
import typescript.ast.`is`.isPropertyDeclaration

val modifyPropertyInheritance = resolve(
    "override" to match {
        match(
            isClassDeclaration,
            withFile("**/interface/inheritance.d.ts"),
            withName("ChildClass"),
        ) {
            match(isPropertyDeclaration, withName("firstField"))
            match(isPropertyDeclaration, withName("secondField"))
            match(isPropertyDeclaration, withName("thirdField"))
            match(isPropertyDeclaration, withName("fourthField"))
            match(isPropertyDeclaration, withName("otherField"))
        }
    },
)
