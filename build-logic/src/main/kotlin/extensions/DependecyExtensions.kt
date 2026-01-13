package extensions

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * Extension functions para manejar dependencias de manera más limpia
 */

fun DependencyHandler.implementation(dependency: Any) {
    add("implementation", dependency)
}

fun DependencyHandler.testImplementation(dependency: Any) {
    add("testImplementation", dependency)
}

fun DependencyHandler.androidTestImplementation(dependency: Any) {
    add("androidTestImplementation", dependency)
}

fun DependencyHandler.debugImplementation(dependency: Any) {
    add("debugImplementation", dependency)
}

fun DependencyHandler.ksp(dependency: Any) {
    add("ksp", dependency)
}

fun DependencyHandler.compileOnly(dependency: Any) {
    add("compileOnly", dependency)
}

fun DependencyHandler.api(dependency: Any) {
    add("api", dependency)
}