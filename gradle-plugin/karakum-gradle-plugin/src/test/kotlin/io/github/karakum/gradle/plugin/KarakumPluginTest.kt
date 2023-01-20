package io.github.karakum.gradle.plugin

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

class KarakumPluginTest {
    @Test fun `plugin registers task`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("org.jetbrains.kotlin.js")
        project.plugins.apply("io.github.karakum")

        // Verify tasks
        assertNotNull(project.tasks.findByName("copyKarakumPlugins"))
        assertNotNull(project.tasks.findByName("configureKarakum"))
        assertNotNull(project.tasks.findByName("generateKarakumExternals"))
    }
}