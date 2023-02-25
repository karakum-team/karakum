package io.github.sgrishchenko.karakum.gradle.plugin

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertNotNull

class KarakumPluginTest {
    @Test @Ignore fun `plugin registers task`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("org.jetbrains.kotlin.js")
        project.plugins.apply("io.github.sgrishchenko.karakum")

        // Verify tasks
        assertNotNull(project.tasks.findByName("copyKarakumPlugins"))
        assertNotNull(project.tasks.findByName("configureKarakum"))
        assertNotNull(project.tasks.findByName("generateKarakumExternals"))
    }
}
