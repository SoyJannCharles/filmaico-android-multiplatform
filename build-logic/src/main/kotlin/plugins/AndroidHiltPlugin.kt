package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import extensions.addHilt

/**
 * Plugin para configurar Hilt en un módulo
 */
class AndroidHiltPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.dagger.hilt.android")
                apply("com.google.devtools.ksp")
            }

            // Agregar dependencias de Hilt
            addHilt()
        }
    }
}