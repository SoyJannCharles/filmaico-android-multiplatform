import extensions.addRoom
import extensions.addGson

plugins {
    id("filmaico.android.library")
    id("filmaico.android.hilt")
}

android {
    namespace = "com.jycra.filmaico.core.database"
}

dependencies {
    implementation(project(":data:user"))
    implementation(project(":data:history"))
    implementation(project(":data:media"))
    implementation(project(":data:stream"))
    implementation(project(":domain:media"))
}

addRoom()
addGson()