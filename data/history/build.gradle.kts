import extensions.addRoom
import extensions.addGson

plugins {
    id("filmaico.android.library")
    id("filmaico.android.hilt")
}

android {
    namespace = "com.jycra.filmaico.data.history"
}

dependencies {
    implementation(project(":domain:common"))
    implementation(project(":domain:history"))
}

addRoom()
addGson()