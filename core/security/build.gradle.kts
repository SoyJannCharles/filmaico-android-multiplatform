plugins {
    id("filmaico.android.library")
    id("filmaico.android.hilt")
}

android {
    namespace = "com.jycra.filmaico.core.security"
}

dependencies {
    implementation(project(":core:config"))
}