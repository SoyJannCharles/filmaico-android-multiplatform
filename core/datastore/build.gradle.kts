import extensions.addDatastore

plugins {
    id("filmaico.android.library")
    id("filmaico.android.hilt")
}

android {
    namespace = "com.jycra.filmaico.core.datastore"
}

dependencies {
    implementation(project(":data:user"))
    implementation(project(":data:stream"))
}

addDatastore()