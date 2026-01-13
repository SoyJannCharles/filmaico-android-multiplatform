import extensions.addGson

plugins {
    id("filmaico.android.library")
    id("filmaico.android.compose")
    id("filmaico.android.hilt")
}

android {
    namespace = "com.jycra.filmaico.core.navigation"
}

dependencies {

}

addGson()