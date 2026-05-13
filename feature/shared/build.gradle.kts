import extensions.addMedia3

plugins {
    id("filmaico.android.feature")
}

android {
    namespace = "com.jycra.filmaico.feature.shared"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:player"))
    implementation(project(":data:stream"))
    implementation(project(":domain:media"))
    implementation(project(":domain:stream"))
}

addMedia3()