import extensions.addFirebaseRemoteConfig

plugins {
    id("filmaico.android.library")
    id("filmaico.android.hilt")
}

android {
    namespace = "com.jycra.filmaico.core.config"
}

dependencies {
    implementation(project(":core:common"))
}

addFirebaseRemoteConfig()