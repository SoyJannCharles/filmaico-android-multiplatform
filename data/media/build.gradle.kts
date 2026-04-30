import extensions.addFirebase
import extensions.addRoom
import extensions.addNetwork
import extensions.addGson

plugins {
    id("filmaico.android.library")
    id("filmaico.android.hilt")
}

android {
    namespace = "com.jycra.filmaico.data.movie"
}

dependencies {
    implementation(project(":core:firebase"))
    implementation(project(":domain:stream"))
    implementation(project(":domain:media"))
}

addFirebase()
addRoom()
addNetwork()
addGson()