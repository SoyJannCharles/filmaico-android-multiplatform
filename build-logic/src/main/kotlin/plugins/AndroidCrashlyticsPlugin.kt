package plugins

import extensions.addFirebaseCrashlytics
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidCrashlyticsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.firebase.crashlytics")
            }
            addFirebaseCrashlytics()
        }
    }
}