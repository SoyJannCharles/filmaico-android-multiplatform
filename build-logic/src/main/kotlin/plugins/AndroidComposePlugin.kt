package plugins

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import utils.configureCompose
import extensions.addCompose

/**
 * Plugin para habilitar Compose en un módulo
 * Se puede aplicar tanto a aplicaciones como a bibliotecas
 */
class AndroidComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.plugin.compose")
            }
            // Configurar Compose en la extensión de Android
            val androidAppExt = target.extensions.findByType(ApplicationExtension::class.java)
            val androidLibExt = target.extensions.findByType(LibraryExtension::class.java)

            val androidExtension = androidAppExt ?: androidLibExt
            ?: throw IllegalStateException("Android plugin must be applied before Compose plugin")

            androidExtension.configureCompose()

            // Agregar dependencias de Compose
            addCompose()
        }
    }
}