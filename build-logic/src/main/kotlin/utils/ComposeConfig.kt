package utils

import com.android.build.api.dsl.CommonExtension
import config.ProjectConfig

/**
 * Configuración específica de Compose
 */
fun CommonExtension<*, *, *, *, *, *>.configureCompose() {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = ProjectConfig.COMPOSE_COMPILER_EXTENSION_VERSION
    }
}