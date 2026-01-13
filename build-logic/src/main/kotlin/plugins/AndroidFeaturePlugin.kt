package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Plugin compuesto para módulos de feature
 * Combina: library + compose + hilt + testing
 */
class AndroidFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                // Aplicar plugins base
                apply("filmaico.android.library")
                apply("filmaico.android.compose")
                apply("filmaico.android.hilt")
                apply("filmaico.android.testing")
            }

            // Aquí puedes agregar dependencias específicas de features
            // Por ejemplo, navegación, ViewModels, etc.
        }
    }
}