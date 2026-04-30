plugins {
    id("filmaico.android.app.tv")
    id("filmaico.android.compose")
    id("filmaico.android.hilt")
    id("filmaico.android.crashlytics")
}

android {
    namespace = "com.jycra.filmaico.tv"
}

dependencies {

    implementation(project(":feature:splash"))
    implementation(project(":feature:update"))
    implementation(project(":feature:signup"))
    implementation(project(":feature:signin"))
    implementation(project(":feature:subscription"))
    implementation(project(":feature:main"))
    implementation(project(":feature:movie"))
    implementation(project(":feature:serie"))
    implementation(project(":feature:anime"))
    implementation(project(":feature:player"))

    implementation(project(":core:app"))
    implementation(project(":core:common"))
    implementation(project(":core:config"))
    implementation(project(":core:network"))
    implementation(project(":core:navigation"))
    implementation(project(":core:ui"))

    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:device"))

    implementation(project(":data:user"))
    implementation(project(":data:history"))
    implementation(project(":data:media"))

    implementation(project(":domain:network"))
    implementation(project(":domain:user"))
    implementation(project(":domain:media"))

}