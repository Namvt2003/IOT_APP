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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // install paho-snapshots library
        maven {
            url = uri("https://repo.eclipse.org/content/repositories/paho-snapshots/")
            url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "IOT_application"
include(":app")
