plugins {
    id("filmaico.android.feature")
}

android {
    namespace = "com.jycra.filmaico.feature.saves"
}

dependencies {
    implementation(project(":core:device"))
    implementation(project(":core:ui"))
    implementation(project(":domain:media"))
}