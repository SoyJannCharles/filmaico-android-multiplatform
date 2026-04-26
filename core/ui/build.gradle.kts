import extensions.addTv
import extensions.addCoil

plugins {
    id("filmaico.android.library")
    id("filmaico.android.compose")
}

android {
    namespace = "com.jycra.filmaico.core.ui"
}

dependencies {
    implementation(project(":core:device"))
    implementation(project(":domain:network"))
    implementation(project(":domain:media"))
    implementation(project(":domain:user"))
}

addTv()
addCoil()