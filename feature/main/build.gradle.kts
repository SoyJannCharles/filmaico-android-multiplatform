plugins {
    id("filmaico.android.feature")
}

android {
    namespace = "com.jycra.filmaico.feature.main"
}

dependencies {
    implementation(project(":core:navigation"))
    implementation(project(":core:ui"))
    implementation(project(":domain:user"))
    implementation(project(":feature:home"))
    implementation(project(":feature:movie"))
    implementation(project(":feature:serie"))
    implementation(project(":feature:channel"))
    implementation(project(":feature:anime"))
    implementation(project(":feature:search"))
}