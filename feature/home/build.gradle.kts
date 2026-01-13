import extensions.addCoil

plugins {
    id("filmaico.android.feature")
}

android {
    namespace = "com.jycra.filmaico.feature.home"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))
    implementation(project(":domain:user"))
    api(project(":feature:channel"))
    api(project(":feature:movie"))
    api(project(":feature:serie"))
    api(project(":feature:anime"))
}

addCoil()