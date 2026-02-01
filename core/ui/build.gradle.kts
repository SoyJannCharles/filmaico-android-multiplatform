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
    implementation(project(":core:network"))
    implementation(project(":domain:common"))
    implementation(project(":domain:media"))
    implementation(project(":domain:user"))
}

addTv()
addCoil()