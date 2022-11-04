package karakum.gradle.plugin

import org.gradle.api.Project
import org.gradle.api.Plugin

class KarakumPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("karakum") { task ->
            task.doLast {
                println("Hello from Karakum plugin")
            }
        }
    }
}
