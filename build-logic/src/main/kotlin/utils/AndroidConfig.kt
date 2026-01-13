package utils

import com.android.build.api.dsl.CommonExtension
import config.ProjectConfig
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/**
 * Configuración común de Android para todos los módulos
 */
fun Project.configureAndroidCommon(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.apply {
        compileSdk = ProjectConfig.COMPILE_SDK

        defaultConfig {
            minSdk = ProjectConfig.MIN_SDK
            testInstrumentationRunner = ProjectConfig.TEST_INSTRUMENTATION_RUNNER
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_24
            targetCompatibility = JavaVersion.VERSION_24
            isCoreLibraryDesugaringEnabled = false
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

        packaging {
            resources {
                excludes += listOf(
                    "/META-INF/{AL2.0,LGPL2.1}",
                    "/META-INF/LICENSE*",
                    "/META-INF/NOTICE*"
                )
            }
        }
    }

    // Configurar Kotlin
    configureKotlin()
}

/**
 * Configuración específica de Kotlin
 */
fun Project.configureKotlin() {
    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_24) // ProjectConfig.JVM_TARGET = "17" por ejemplo
            freeCompilerArgs.addAll(
                listOf(
                    "-opt-in=kotlin.RequiresOptIn",
                    "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
                )
            )
        }
    }
}