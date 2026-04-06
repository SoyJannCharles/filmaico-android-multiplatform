import extensions.addFirebase
import extensions.addNetwork
import extensions.addMedia3

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

addFirebase()
addNetwork()
addMedia3()