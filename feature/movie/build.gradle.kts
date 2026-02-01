import extensions.addCoil

plugins {
    id("filmaico.android.feature")
}

android {
    namespace = "com.jycra.filmaico.feature.movie"
}

dependencies {
    implementation(project(":core:device"))
    implementation(project(":core:ui"))
    implementation(project(":domain:media"))
}

addCoil()