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

        maven(url = "https://artifactory-external.vkpartner.ru/artifactory/vkid-sdk-android/")
        maven(url = "https://artifactory-external.vkpartner.ru/artifactory/maven/")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
        maven {
            url = uri("https://artifactory-external.vkpartner.ru/artifactory/vkid-sdk-android/")
        }
        maven {
            url = uri("https://artifactory-external.vkpartner.ru/artifactory/maven/")
        }
    }
}

rootProject.name = "Reflect"
include(":app")
 