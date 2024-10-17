package com.v7v.gradle.plugins.tooling

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AndroidApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.application")
        setupKotlin()
        extensions.configure<ApplicationExtension>("android") { ext ->
            with(ext) {
                compileSdk = AndroidVersions.compileSdk
                namespace = "com.v7v.${gradleProjectPathToNamespace(path)}"
                defaultConfig {
                    minSdk = AndroidVersions.minSdk
                    targetSdk = AndroidVersions.targetSdk
                    vectorDrawables.useSupportLibrary = true
                    versionCode = 1
                    versionName = "1.0"

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
                buildTypes.named("release").configure {
                    it.proguardFiles(getDefaultProguardFile("proguard-android.txt"))
                }
                buildFeatures.buildConfig = true
                compileOptions.setupJava()
                setupCompose(buildFeatures, composeOptions)

                packaging.resources.excludes.add("META-INF/DEPENDENCIES")
            }
        }
    }
}