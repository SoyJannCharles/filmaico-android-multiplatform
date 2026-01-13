package plugins

import com.android.build.gradle.LibraryExtension
import config.ProjectConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import utils.configureAndroidCommon
import extensions.addAndroidXCore
import extensions.addTesting

/**
 * Plugin para módulos de biblioteca Android
 */
class AndroidLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureAndroidCommon(this)

                defaultConfig {
                    consumerProguardFiles(ProjectConfig.CONSUMER_RULES)
                }

                buildTypes {
                    release {
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile(ProjectConfig.PROGUARD_ANDROID_OPTIMIZE),
                            ProjectConfig.PROGUARD_RULES
                        )
                    }
                }
            }

            // Agregar dependencias básicas
            addAndroidXCore()
            addTesting()
        }
    }
}