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
    implementation(project(":data:movie"))
    implementation(project(":data:serie"))
    implementation(project(":data:channel"))
    implementation(project(":data:anime"))
    implementation(project(":data:stream"))
}

addRoom()
addGson()