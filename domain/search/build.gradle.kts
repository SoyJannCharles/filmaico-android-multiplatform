import extensions.addCoroutines
import extensions.addGson
import extensions.implementation

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

dependencies {
    implementation(project(":domain:movie"))
    implementation(project(":domain:serie"))
    implementation(project(":domain:channel"))
    implementation(project(":domain:anime"))
}

addCoroutines()
addGson()