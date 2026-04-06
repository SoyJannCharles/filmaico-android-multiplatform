import extensions.addCoil

plugins {
    id("filmaico.android.feature")
}

android {
    namespace = "com.jycra.filmaico.feature.channel"
}

dependencies {
    implementation(project(":core:device"))
    implementation(project(":core:ui"))
    implementation(project(":domain:media"))
    implementation(project(":feature:shared"))
}

addCoil()