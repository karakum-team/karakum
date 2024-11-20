package io.github.sgrishchenko.karakum.gradle.plugin.service

import com.pinterest.ktlint.rule.engine.api.KtLintRuleEngine
import com.pinterest.ktlint.ruleset.standard.StandardRuleSetProvider
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters.None

abstract class KtLintService : BuildService<None> {
    val ktlint = KtLintRuleEngine(
        ruleProviders = StandardRuleSetProvider().getRuleProviders()
    ).apply {
        // ktlint stores .editorconfig files in a companion object and thus as static state
        // this is not good when running multiple builds within the same Gradle daemon
        // and the .editorconfig file might have changed, so clear that cache
        // so that .editorconfig files are read freshly
        trimMemory()
    }
}
