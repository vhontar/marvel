plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
    `java-gradle-plugin`
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.gradlePlugins.android)
    implementation(libs.gradlePlugins.kotlin)
    implementation(libs.gradlePlugins.ksp)
    implementation(libs.gradlePlugins.kotlinComposeCompiler)
    implementation(libs.kotlinx.serialization)
}

gradlePlugin {
    plugins {
        register("androidLibraryPlugin") {
            id = "com.v7v.android.library"
            implementationClass = "com.v7v.gradle.plugins.tooling.AndroidLibraryPlugin"
        }
        register("androidApplicationPlugin") {
            id = "com.v7v.android.application"
            implementationClass = "com.v7v.gradle.plugins.tooling.AndroidApplicationPlugin"
        }
        register("setupDevPropertiesPlugin") {
            id = "com.v7v.setup.dev.properties"
            implementationClass = "com.v7v.gradle.plugins.tooling.SetupDevPropertiesPlugin"
        }
    }
}