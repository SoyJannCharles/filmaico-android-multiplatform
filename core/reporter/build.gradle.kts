import extensions.addFirebaseCrashlytics

plugins {
    id("filmaico.android.library")
    id("filmaico.android.hilt")
}

android {
    namespace = "com.jycra.filmaico.core.reporter"
}

dependencies {

}

addFirebaseCrashlytics()