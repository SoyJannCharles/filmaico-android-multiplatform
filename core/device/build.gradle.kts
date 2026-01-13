plugins {
    id("filmaico.android.library")
    id("filmaico.android.hilt")
}

android {
    namespace = "com.jycra.filmaico.core.device"
}

dependencies {
    implementation(project(":data:user"))
}