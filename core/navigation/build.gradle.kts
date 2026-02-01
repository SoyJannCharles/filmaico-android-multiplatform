import extensions.addGson

plugins {
    id("filmaico.android.library")
    id("filmaico.android.compose")
    id("filmaico.android.hilt")
}

android {
    namespace = "com.jycra.filmaico.core.navigation"
}

dependencies {
    implementation(project(":core:device"))
    implementation(project(":domain:media"))
    implementation(project(":feature:movie"))
    implementation(project(":feature:serie"))
    implementation(project(":feature:anime"))
}

addGson()