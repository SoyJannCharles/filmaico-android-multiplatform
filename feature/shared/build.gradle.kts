plugins {
    id("filmaico.android.feature")
}

android {
    namespace = "com.jycra.filmaico.feature.shared"
}

dependencies {
    implementation(project(":data:stream"))
    implementation(project(":domain:media"))
    implementation(project(":domain:stream"))
}