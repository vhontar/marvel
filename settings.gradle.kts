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
    }
}

rootProject.name = "Marvel"
include(":app")
includeBuild("library") {
    dependencySubstitution {
        all {
            if (requested.displayName.startsWith("library:")) {
                val module = requested.displayName.replace("library", "").replace(".", ":")
                useTarget(project(module))
            }
        }
    }
}
includeBuild("gradle-plugins-tooling")
