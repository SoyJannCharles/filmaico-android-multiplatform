plugins {
    id("filmaico.android.feature")
}

android {
    namespace = "com.jycra.filmaico.feature.subscription"
}

dependencies {
    implementation(project(":core:device"))
    implementation(project(":core:navigation"))
    implementation(project(":core:ui"))
    implementation(project(":domain:user"))
}