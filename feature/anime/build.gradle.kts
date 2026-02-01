import extensions.addCoil

plugins {
    id("filmaico.android.feature")
}

android {
    namespace = "com.jycra.filmaico.feature.anime"
}

dependencies {
    implementation(project(":core:device"))
    implementation(project(":core:ui"))
    implementation(project(":domain:common"))
    implementation(project(":domain:media"))
}

addCoil()