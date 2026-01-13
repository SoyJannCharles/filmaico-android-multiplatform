plugins {
    id("filmaico.android.app.mobile")
    id("filmaico.android.compose")
    id("filmaico.android.hilt")
    id("filmaico.android.crashlytics")
}

android {
    namespace = "com.jycra.filmaico.mobile"

    signingConfigs {
        create("release") {
            // Leemos las credenciales desde gradle.properties
            val storeFile = System.getenv("MYAPP_RELEASE_STORE_FILE") ?: project.property("MYAPP_RELEASE_STORE_FILE")
            val storePassword = System.getenv("MYAPP_RELEASE_STORE_PASSWORD") ?: project.property("MYAPP_RELEASE_STORE_PASSWORD")
            val keyAlias = System.getenv("MYAPP_RELEASE_KEY_ALIAS") ?: project.property("MYAPP_RELEASE_KEY_ALIAS")
            val keyPassword = System.getenv("MYAPP_RELEASE_KEY_PASSWORD") ?: project.property("MYAPP_RELEASE_KEY_PASSWORD")

            this.storeFile = storeFile?.let { file(it) }
            this.storePassword = storePassword.toString()
            this.keyAlias = keyAlias.toString()
            this.keyPassword = keyPassword.toString()
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            // 2. Vincula la firma a la build de release
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            // La build de debug se firma automáticamente con la clave de depuración
        }
    }

}

dependencies {

    implementation(project(":feature:splash"))
    implementation(project(":feature:signup"))
    implementation(project(":feature:signin"))
    implementation(project(":feature:pay"))
    implementation(project(":feature:main"))
    implementation(project(":feature:movie"))
    implementation(project(":feature:serie"))
    implementation(project(":feature:anime"))
    implementation(project(":feature:player"))

    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:config"))
    implementation(project(":core:navigation"))

    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(project(":core:device"))

    implementation(project(":data:user"))
    implementation(project(":data:search"))
    implementation(project(":data:movie"))
    implementation(project(":data:serie"))
    implementation(project(":data:channel"))
    implementation(project(":data:anime"))

}