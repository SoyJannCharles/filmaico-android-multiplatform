import extensions.addFirebase
import extensions.addRoom
import extensions.addNetwork
import extensions.addGson

plugins {
    id("filmaico.android.library")
    id("filmaico.android.hilt")
}

android {
    namespace = "com.jycra.filmaico.data.serie"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":domain:serie"))
    api(project(":domain:stream"))
}

addFirebase()
addRoom()
addNetwork()
addGson()