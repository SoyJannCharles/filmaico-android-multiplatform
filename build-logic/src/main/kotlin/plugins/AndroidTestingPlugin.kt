package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import extensions.addTesting

/**
 * Plugin para configurar testing avanzado en un módulo
 */
class AndroidTestingPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Agregar dependencias de testing
            addTesting()

            // Configuraciones adicionales de testing si las necesitas
            // Por ejemplo, configuración de JUnit5, Mockk, etc.
        }
    }
}