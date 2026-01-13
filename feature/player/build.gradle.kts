import extensions.addMedia3

plugins {
    id("filmaico.android.feature")
}

android {
    namespace = "com.jycra.filmaico.feature.player"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))
    implementation(project(":core:reporter"))
    implementation(project(":core:player"))
    implementation(project(":domain:movie"))
    implementation(project(":domain:serie"))
    implementation(project(":domain:channel"))
    implementation(project(":domain:anime"))
}

addMedia3()