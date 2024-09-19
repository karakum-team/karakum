package io.github.sgrishchenko.karakum.gradle.plugin.worker

import com.pinterest.ktlint.rule.engine.api.Code
import com.pinterest.ktlint.rule.engine.api.KtLintRuleEngine
import com.pinterest.ktlint.rule.engine.core.api.AutocorrectDecision.ALLOW_AUTOCORRECT
import com.pinterest.ktlint.rule.engine.core.api.AutocorrectDecision.NO_AUTOCORRECT
import com.pinterest.ktlint.ruleset.standard.StandardRuleSetProvider
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.Logging
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters

abstract class KtLintFormatWorker : WorkAction<KtLintFormatWorker.Parameters> {
    private val logger = Logging.getLogger(KtLintFormatWorker::class.java)

    override fun execute() {
        val file = parameters.file.get().asFile
        logger.info("Formatting ${file.absolutePath}")
        runCatching {
            KtLintRuleEngine(
                ruleProviders = StandardRuleSetProvider().getRuleProviders()
            ).apply {
                // ktlint stores .editorconfig files in a companion object and thus as static state
                // this is not good when running multiple builds within the same Gradle daemon or worker
                // and the .editorconfig file might have changed, so clear that cache
                // for .editorconfig files to be read freshly
                trimMemory()
            }.format(Code.fromFile(file)) { lintError ->
                if (lintError.canBeAutoCorrected) ALLOW_AUTOCORRECT else NO_AUTOCORRECT
            }.also(file::writeText)
        }.onFailure {
            logger.info("Formatting failed for ${file.absolutePath}")
            logger.info(it.stackTraceToString())
        }.onSuccess {
            logger.info("Formatting successful for ${file.absolutePath}")
        }
    }

    interface Parameters : WorkParameters {
        val file: RegularFileProperty
    }
}
