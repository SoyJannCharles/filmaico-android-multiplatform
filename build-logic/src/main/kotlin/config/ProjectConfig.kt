package config

/**
 * Configuración centralizada del proyecto
 */
object ProjectConfig {
    const val APPLICATION_ID_MOBILE = "com.jycra.filmaico.mobile"
    const val APPLICATION_ID_TV = "com.jycra.filmaico.tv"
    const val VERSION_CODE = 2
    const val VERSION_NAME = "1.1.0"

    // SDK Versions
    const val COMPILE_SDK = 36
    const val MIN_SDK = 23
    const val TARGET_SDK = 36

    // Build Config
    const val TEST_INSTRUMENTATION_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
    const val COMPOSE_COMPILER_EXTENSION_VERSION = "2.2.10"
    const val JVM_TARGET = "24"

    // Proguard
    const val PROGUARD_ANDROID_OPTIMIZE = "proguard-android-optimize.txt"
    const val PROGUARD_RULES = "proguard-rules.pro"
    const val CONSUMER_RULES = "consumer-rules.pro"
}