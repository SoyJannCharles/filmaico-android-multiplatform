package plugins

import com.android.build.api.dsl.ApplicationExtension
import config.ProjectConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import utils.configureAndroidCommon
import extensions.addAndroidXCore
import extensions.addTesting
import extensions.addTv

/**
 * Plugin específico para la aplicación TV
 */
class AndroidAppTvPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("com.google.gms.google-services")
            }

            extensions.configure<ApplicationExtension> {
                configureAndroidCommon(this)

                defaultConfig {
                    applicationId = ProjectConfig.APPLICATION_ID_TV
                    targetSdk = ProjectConfig.TARGET_SDK
                    versionCode = ProjectConfig.VERSION_CODE
                    versionName = ProjectConfig.VERSION_NAME

                    vectorDrawables {
                        useSupportLibrary = true
                    }
                }

                buildTypes {
                    debug {
                        applicationIdSuffix = ".debug"
                        versionNameSuffix = "-debug"
                        isDebuggable = true
                    }

                    release {
                        isMinifyEnabled = true
                        isShrinkResources = true
                        proguardFiles(
                            getDefaultProguardFile(ProjectConfig.PROGUARD_ANDROID_OPTIMIZE),
                            ProjectConfig.PROGUARD_RULES
                        )
                    }
                }
            }

            // Dependencias básicas + TV específicas
            addAndroidXCore()
            addTesting()
            addTv() // Dependencias específicas de Android TV
        }
    }
}