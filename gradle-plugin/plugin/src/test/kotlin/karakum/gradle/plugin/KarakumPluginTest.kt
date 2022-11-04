package karakum.gradle.plugin

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

class KarakumPluginTest {
    @Test fun `plugin registers task`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("karakum.gradle.plugin")

        // Verify the result
        assertNotNull(project.tasks.findByName("karakum"))
    }
}
