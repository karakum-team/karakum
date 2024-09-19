package io.github.sgrishchenko.karakum.gradle.plugin.worker

import com.pinterest.ktlint.rule.engine.api.Code
import com.pinterest.ktlint.rule.engine.core.api.AutocorrectDecision.ALLOW_AUTOCORRECT
import com.pinterest.ktlint.rule.engine.core.api.AutocorrectDecision.NO_AUTOCORRECT
import io.github.sgrishchenko.karakum.gradle.plugin.service.KtLintService
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.Logging
import org.gradle.api.provider.Property
import org.gradle.api.services.ServiceReference
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters

abstract class KtLintFormatWorker : WorkAction<KtLintFormatWorker.Parameters> {
    private val logger = Logging.getLogger(KtLintFormatWorker::class.java)

    @get:ServiceReference
    abstract val ktlintService: Property<KtLintService>

    override fun execute() {
        val file = parameters.file.get().asFile
        logger.info("Formatting ${file.absolutePath}")
        runCatching {
            ktlintService
                .get()
                .ktlint
                .format(Code.fromFile(file)) { lintError ->
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
