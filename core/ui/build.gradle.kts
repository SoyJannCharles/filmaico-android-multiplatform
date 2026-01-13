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
    implementation(project(":core:navigation"))
    implementation(project(":domain:user"))
    implementation(project(":domain:movie"))
    implementation(project(":domain:serie"))
    implementation(project(":domain:channel"))
    implementation(project(":domain:anime"))
}

addTv()
addCoil()