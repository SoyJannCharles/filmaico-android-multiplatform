import extensions.addFirebase
import extensions.addGson
import extensions.addNetwork
import extensions.addRoom

plugins {
    id("filmaico.android.library")
    id("filmaico.android.hilt")
}

android {
    namespace = "com.jycra.filmaico.data.stream"
}

dependencies {
    implementation(project(":core:config"))
    implementation(project(":core:security"))
    implementation(project(":core:model"))
    implementation(project(":domain:common"))
    implementation(project(":domain:stream"))
    implementation(project(":domain:media"))
}

addFirebase()
addRoom()
addNetwork()
addGson()