package com.v7v.gradle.plugins.tooling

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.Properties

internal class SetupDevPropertiesPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        extensions.create("devProperties", DevPropertiesExtension::class.java)

        val devPropertiesFile = file("$rootDir/../dev.properties")
        if (!devPropertiesFile.exists()) return@with

        devPropertiesFile.run {
            val properties = Properties()
            inputStream().use { properties.load(it) }
            properties.forEach { key, value ->
                project.rootProject.extensions.extraProperties.set(
                    key as String,
                    value,
                )
            }
        }
    }
}

abstract class DevPropertiesExtension {
    fun getByNameOrDefault(project: Project, name: String, defaultValue: String = ""): String =
        if (project.rootProject.extensions.extraProperties.has(name)) {
            project.rootProject.extensions.extraProperties.get(name) as String
        } else {
            defaultValue
        }
}
