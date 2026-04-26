import extensions.addMedia3

plugins {
    id("filmaico.android.feature")
}

android {
    namespace = "com.jycra.filmaico.feature.player"
}

dependencies {
    implementation(project(":core:device"))
    implementation(project(":core:navigation"))
    implementation(project(":core:ui"))
    implementation(project(":core:reporter"))
    implementation(project(":core:player"))
    implementation(project(":domain:history"))
    implementation(project(":domain:media"))
    implementation(project(":domain:stream"))
    implementation(project(":feature:shared"))
}

addMedia3()