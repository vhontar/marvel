package com.v7v.gradle.plugins.tooling

import com.android.build.api.dsl.BuildFeatures
import com.android.build.api.dsl.CompileOptions
import com.android.build.api.dsl.ComposeOptions
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainSpec
import org.gradle.jvm.toolchain.JvmVendorSpec
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension

internal fun gradleProjectPathToNamespace(gradleProjectPath: String) =
    gradleProjectPath.drop(1).replace(":", ".").replace("-", ".")

internal fun Project.setupKotlin() {
    pluginManager.apply("org.jetbrains.kotlin.android")
    pluginManager.apply("com.google.devtools.ksp")
    extensions.findByType(KotlinTopLevelExtension::class.java)!!.apply {
        jvmToolchain {
            it as JavaToolchainSpec
            it.languageVersion.set(JavaLanguageVersion.of(17))
            it.vendor.set(JvmVendorSpec.ADOPTIUM)
        }
    }
    dependencies.add("implementation", "org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}

internal fun CompileOptions.setupJava() {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

internal fun Project.setupCompose(buildFeatures: BuildFeatures, composeOptions: ComposeOptions) {
    pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
    composeOptions.kotlinCompilerExtensionVersion = "1.5.14"
    buildFeatures.compose = true
    dependencies.add("implementation", "androidx.compose.runtime:runtime:1.7.3")
}

internal fun ComposeOptions.setup(project: Project) {
    val versionCatalogsExtension = project.extensions.getByType(VersionCatalogsExtension::class.java)
    kotlinCompilerExtensionVersion = versionCatalogsExtension
        .named("libs")
        .findLibrary("tooling-composeCompiler")
        .get()
        .get()
        .versionConstraint
        .requiredVersion
}