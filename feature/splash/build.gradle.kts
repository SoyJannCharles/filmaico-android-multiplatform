plugins {
    id("filmaico.android.feature")
}

android {
    namespace = "com.jycra.filmaico.feature.splash"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:config"))
    implementation(project(":core:network"))
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))
    implementation(project(":domain:user"))
}