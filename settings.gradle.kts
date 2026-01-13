pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

includeBuild("build-logic")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Filmaico"
include(":app-mobile")
include(":app-tv")
include(":feature:home")
include(":core:ui")
include(":core:navigation")
include(":core:database")
include(":core:common")
include(":core:network")
include(":core:player")
include(":feature:player")
include(":core:security")
include(":feature:splash")
include(":core:design")
include(":core:config")
include(":data:channel")
include(":data:user")
include(":domain:channel")
include(":domain:user")
include(":core:datastore")
include(":feature:signin")
include(":feature:signup")
include(":feature:pay")
include(":core:device")
include(":domain:stream")
include(":data:stream")
include(":domain:anime")
include(":domain:movie")
include(":domain:serie")
include(":data:anime")
include(":data:serie")
include(":data:movie")
include(":feature:anime")
include(":feature:movie")
include(":feature:serie")
include(":feature:channel")
include(":feature:main")
include(":core:model")
include(":core:reporter")
include(":feature:search")
include(":domain:search")
include(":data:search")
