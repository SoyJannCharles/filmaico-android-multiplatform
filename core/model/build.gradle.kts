import extensions.addFirestoreAnnotation
import extensions.addGson

plugins {
    id("filmaico.android.library")
}

android {
    namespace = "com.jycra.filmaico.core.model"
}

dependencies {

}

addFirestoreAnnotation()
addGson()