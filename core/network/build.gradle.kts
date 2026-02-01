import extensions.addNetwork
import extensions.addFirebase

plugins {
    id("filmaico.android.library")
    id("filmaico.android.hilt")
}

android {
    namespace = "com.jycra.filmaico.core.network"
}

dependencies {
    implementation(project(":core:config"))
    implementation(project(":core:model"))
    implementation(project(":data:stream"))
}

addNetwork()
addFirebase()