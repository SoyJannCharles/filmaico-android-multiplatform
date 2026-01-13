import extensions.addNetwork
import extensions.addMedia3

plugins {
    id("filmaico.android.library")
    id("filmaico.android.hilt")
}

android {
    namespace = "com.jycra.filmaico.core.player"
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":domain:channel"))
}

addNetwork()
addMedia3()