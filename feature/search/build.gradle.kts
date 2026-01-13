plugins {
    id("filmaico.android.feature")
}

android {
    namespace = "com.jycra.filmaico.feature.search"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))
    implementation(project(":domain:search"))
    implementation(project(":domain:movie"))
    implementation(project(":domain:serie"))
    implementation(project(":domain:channel"))
    implementation(project(":domain:anime"))
}