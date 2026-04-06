package extensions

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Extension functions para grupos de dependencias relacionadas
 */

/**
 * Agrega dependencias básicas de AndroidX
 */
fun Project.addAndroidXCore() {
    dependencies {
        implementation(libs.library("androidx-core-ktx"))
        implementation(libs.library("androidx-lifecycle-runtime-ktx"))
    }
}

fun Project.addDatastore() {
    dependencies {
        implementation(libs.library("androidx-datastore"))
    }
}

fun Project.addCoroutines() {
    dependencies {
        implementation(libs.library("kotlinx-coroutines-core"))
    }
}

/**
 * Agrega dependencias completas de Compose
 */
fun Project.addCompose() {
    dependencies {
        val composeBom = libs.library("androidx-compose-bom")
        implementation(platform(composeBom))

        implementation(libs.library("androidx-activity-compose"))
        implementation(libs.library("androidx-compose-ui"))
        implementation(libs.library("androidx-compose-ui-graphics"))
        implementation(libs.library("androidx-compose-ui-tooling-preview"))
        implementation(libs.library("androidx-compose-navigation"))
        implementation(libs.library("androidx-compose-material3"))

        // Testing
        androidTestImplementation(platform(composeBom))
        androidTestImplementation(libs.library("androidx-compose-ui-test-junit4"))

        // Debug
        debugImplementation(libs.library("debug-androidx-compose-ui-tooling"))
        debugImplementation(libs.library("debug-androidx-compose-ui-test-manifest"))
    }
}

fun Project.addRuntimeCompose() {
    dependencies {

        val composeBom = libs.library("androidx-compose-bom")
        implementation(platform(composeBom))

        implementation(libs.library("androidx-compose-runtime"))

    }
}

/**
 * Agrega dependencias de Android TV
 */
fun Project.addTv() {
    dependencies {
        implementation(libs.library("androidx-tv-foundation"))
        implementation(libs.library("androidx-tv-material"))
        implementation(libs.library("androidx-appcompat"))
    }
}

fun Project.addRoom() {
    dependencies {
        implementation(libs.library("androidx-room-ktx"))
        implementation(libs.library("androidx-room-runtime"))
        ksp(libs.library("androidx-room-compiler"))
    }
}

/**
 * Agrega dependencias de Firebase
 */
fun Project.addFirebase() {
    dependencies {
        val firebaseBom = libs.library("firebase-bom")
        implementation(platform(firebaseBom))
        implementation(libs.library("firebase-config"))
        implementation(libs.library("firebase-crashlytics"))
        implementation(libs.library("firebase-analytics"))
        implementation(libs.library("firebase-firestore"))
        implementation(libs.library("firebase-auth"))
    }
}

fun Project.addFirebaseRemoteConfig() {
    dependencies {
        val firebaseBom = libs.library("firebase-bom")
        implementation(platform(firebaseBom))
        implementation(libs.library("firebase-config"))
    }
}

fun Project.addFirebaseCrashlytics() {
    dependencies {
        val firebaseBom = libs.library("firebase-bom")
        implementation(platform(firebaseBom))
        implementation(libs.library("firebase-crashlytics"))
        implementation(libs.library("firebase-analytics"))
    }
}

fun Project.addFirestoreAnnotation() {
    dependencies {
        val firebaseBom = libs.library("firebase-bom")
        implementation(platform(firebaseBom))
        implementation(libs.library("firebase-firestore"))
    }
}

/**
 * Agrega dependencias de red (Retrofit + OkHttp)
 */
fun Project.addNetwork() {
    dependencies {

        implementation(libs.library("retrofit-core"))
        implementation(libs.library("retrofit-converter-gson"))

        val okhttpBom = libs.library("okhttp-bom")
        implementation(platform(okhttpBom))
        implementation(libs.library("okhttp-core"))
        implementation(libs.library("okhttp-logging-interceptor"))

    }
}

/**
 * Agrega dependencias de Hilt
 */
fun Project.addHilt() {
    dependencies {
        implementation(libs.library("hilt-android"))
        implementation(libs.library("hilt-compose"))
        ksp(libs.library("hilt-compiler"))
    }
}

fun Project.addJvmInject() {
    dependencies {
        implementation(libs.library("javax-inject"))
    }
}

fun Project.addGson() {
    dependencies {
        implementation(libs.library("gson"))
    }
}

/**
 * Agrega dependencias de Media3 (ExoPlayer)
 */
fun Project.addMedia3() {
    dependencies {
        implementation(libs.library("media3-exoplayer"))
        implementation(libs.library("media3-exoplayer-dash"))
        implementation(libs.library("media3-exoplayer-hls"))
        implementation(libs.library("media3-exoplayer-ffmpeg"))
        implementation(libs.library("media3-datasource"))
        implementation(libs.library("media3-ui"))
    }
}

fun Project.addCoil() {
    dependencies {
        implementation(libs.library("coil-core"))
        implementation(libs.library("coil-compose"))
        implementation(libs.library("coil-okhttp"))
    }
}

/**
 * Agrega dependencias básicas de testing
 */
fun Project.addTesting() {
    dependencies {
        testImplementation(libs.library("junit"))
        androidTestImplementation(libs.library("androidx-junit"))
        androidTestImplementation(libs.library("androidx-espresso-core"))
    }
}