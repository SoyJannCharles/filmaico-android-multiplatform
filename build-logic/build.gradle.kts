import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    // Android Gradle Plugin
    implementation("com.android.tools.build:gradle:8.13.0")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.20")

    // Hilt
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.57.1")

    // KSP
    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.2.0-2.0.2")
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_24)
        freeCompilerArgs.addAll(listOf("-opt-in=kotlin.RequiresOptIn"))
    }
}

gradlePlugin {
    plugins {
        // Core plugins
        register("androidAppMobile") {
            id = "filmaico.android.app.mobile"
            implementationClass = "plugins.AndroidAppMobilePlugin"
        }

        register("androidAppTv") {
            id = "filmaico.android.app.tv"
            implementationClass = "plugins.AndroidAppTvPlugin"
        }

        register("androidLibrary") {
            id = "filmaico.android.library"
            implementationClass = "plugins.AndroidLibraryPlugin"
        }

        // Feature plugins
        register("androidCompose") {
            id = "filmaico.android.compose"
            implementationClass = "plugins.AndroidComposePlugin"
        }

        register("androidHilt") {
            id = "filmaico.android.hilt"
            implementationClass = "plugins.AndroidHiltPlugin"
        }

        register("androidCrashlytics") {
            id = "filmaico.android.crashlytics"
            implementationClass = "plugins.AndroidCrashlyticsPlugin"
        }

        register("androidJvmInject") {
            id = "filmaico.android.jvm.inject"
            implementationClass = "plugins.AndroidJvmInjectPlugin"
        }

        register("androidTv") {
            id = "filmaico.android.tv"
            implementationClass = "plugins.AndroidTvPlugin"
        }

        register("androidTesting") {
            id = "filmaico.android.testing"
            implementationClass = "plugins.AndroidTestingPlugin"
        }

        // Composite plugins
        register("androidFeature") {
            id = "filmaico.android.feature"
            implementationClass = "plugins.AndroidFeaturePlugin"
        }
    }
}