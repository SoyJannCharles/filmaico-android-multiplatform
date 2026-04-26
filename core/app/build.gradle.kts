import extensions.addGson

plugins {
    id("filmaico.android.library")
    id("filmaico.android.hilt")
}

android {
    namespace = "com.jycra.filmaico.core.app"
}

dependencies {
    implementation(project(":core:config"))
    implementation(project(":data:user"))
    implementation(project(":domain:network"))
}

addGson()