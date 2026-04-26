import extensions.addGson
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
    implementation(project(":data:stream"))
    implementation(project(":domain:media"))
}

addMedia3()
addGson()