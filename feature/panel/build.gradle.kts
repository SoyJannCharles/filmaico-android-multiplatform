plugins {
    id("filmaico.android.feature")
}

android {
    namespace = "com.jycra.filmaico.feature.panel"
}

dependencies {

    implementation(project(":core:app"))
    implementation(project(":core:device"))
    implementation(project(":core:ui"))

    implementation(project(":domain:user"))

}