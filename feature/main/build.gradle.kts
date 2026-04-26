plugins {
    id("filmaico.android.feature")
}

android {
    namespace = "com.jycra.filmaico.feature.main"
}

dependencies {

    implementation(project(":core:app"))
    implementation(project(":core:device"))
    implementation(project(":core:navigation"))
    implementation(project(":core:ui"))

    implementation(project(":domain:user"))
    implementation(project(":domain:media"))

    implementation(project(":feature:search"))
    implementation(project(":feature:home"))
    implementation(project(":feature:saves"))
    implementation(project(":feature:channel"))
    implementation(project(":feature:movie"))
    implementation(project(":feature:serie"))
    implementation(project(":feature:anime"))
    implementation(project(":feature:panel"))

}