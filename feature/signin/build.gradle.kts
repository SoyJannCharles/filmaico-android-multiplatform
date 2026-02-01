plugins {
    id("filmaico.android.feature")
}

android {
    namespace = "com.jycra.filmaico.feature.signin"
}

dependencies {
    implementation(project(":core:device"))
    implementation(project(":core:navigation"))
    implementation(project(":core:ui"))
    implementation(project(":domain:user"))
}