package plugins

import org.gradle.api.Project
import org.gradle.api.Plugin
import extensions.addJvmInject

class AndroidJvmInjectPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
            }
            addJvmInject()
        }
    }
}