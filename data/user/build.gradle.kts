import extensions.addFirebase
import extensions.addRoom

plugins {
    id("filmaico.android.library")
    id("filmaico.android.hilt")
}

android {
    namespace = "com.jycra.filmaico.data.user"
}

dependencies {
    api(project(":core:model"))
    api(project(":domain:user"))
}

addFirebase()
addRoom()