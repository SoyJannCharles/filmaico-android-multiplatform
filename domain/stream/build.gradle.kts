import extensions.addCoroutines
import extensions.addGson
import extensions.addRuntimeCompose

plugins {
    id("filmaico.android.jvm.inject")
}

java {
    sourceCompatibility = JavaVersion.VERSION_24
    targetCompatibility = JavaVersion.VERSION_24
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_24
    }
}

addRuntimeCompose()
addCoroutines()
addGson()