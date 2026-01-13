package extensions

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

/**
 * Extension functions para acceder al version catalog de manera type-safe
 */

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun VersionCatalog.library(alias: String): Provider<MinimalExternalModuleDependency> =
    findLibrary(alias).orElseThrow {
        NoSuchElementException("Library '$alias' not found in version catalog")
    }

fun VersionCatalog.version(alias: String): String =
    findVersion(alias).orElseThrow {
        NoSuchElementException("Version '$alias' not found in version catalog")
    }.toString()

fun VersionCatalog.plugin(alias: String) =
    findPlugin(alias).orElseThrow {
        NoSuchElementException("Plugin '$alias' not found in version catalog")
    }