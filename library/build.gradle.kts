buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath(libs.gradlePlugins.android)
        classpath(libs.gradlePlugins.kotlin)
    }
}

allprojects {
    version = "1.0.0"
}