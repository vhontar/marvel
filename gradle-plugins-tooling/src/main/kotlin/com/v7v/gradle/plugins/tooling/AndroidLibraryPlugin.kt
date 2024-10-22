package com.v7v.gradle.plugins.tooling

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AndroidLibraryPlugin: Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.library")
        pluginManager.apply("com.autonomousapps.dependency-analysis")
        setupKotlin()
        extensions.configure<LibraryExtension>("android") { ext ->
            with(ext) {
                compileSdk = AndroidVersions.compileSdk
                namespace = "com.v7v.${gradleProjectPathToNamespace(path)}"
                defaultConfig {
                    minSdk = AndroidVersions.minSdk
                    vectorDrawables.useSupportLibrary = true
                }
                buildTypes.configureEach {
                    it.consumerProguardFiles("proguard-rules.pro")
                }
                buildFeatures.buildConfig = true
                compileOptions.setupJava()
                setupCompose(buildFeatures, composeOptions)
            }
        }
        setupLinting()
        setupTesting()
    }
}
