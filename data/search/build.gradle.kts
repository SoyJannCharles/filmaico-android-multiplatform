import extensions.addGson

plugins {
    id("filmaico.android.library")
    id("filmaico.android.hilt")
}

android {
    namespace = "com.jycra.filmaico.data.search"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":data:movie"))
    implementation(project(":data:serie"))
    implementation(project(":data:channel"))
    implementation(project(":data:anime"))
    implementation(project(":domain:search"))
    implementation(project(":domain:movie"))
    implementation(project(":domain:serie"))
    implementation(project(":domain:channel"))
    implementation(project(":domain:anime"))
    api(project(":domain:stream"))
}

addGson()