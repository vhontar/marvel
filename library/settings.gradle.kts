dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

include(
    ":data",
    ":domain",
    ":logger",
    ":core",
    ":navigation",
    ":feature:home",
    ":feature:character-details",
    ":feature:comic-details"
)
includeBuild("../gradle-plugins-tooling")