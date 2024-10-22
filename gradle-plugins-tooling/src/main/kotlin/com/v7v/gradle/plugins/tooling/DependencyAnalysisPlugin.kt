package com.v7v.gradle.plugins.tooling

import com.autonomousapps.DependencyAnalysisExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class DependencyAnalysisPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.autonomousapps.dependency-analysis")
        if (project == project.rootProject) {
            extensions.configure<DependencyAnalysisExtension>("dependencyAnalysis") { ext ->
                ext.structure { handler ->
                    handler.bundle("kotlin-stdlib") {
                        it.includeGroup("org.jetbrains.kotlin")
                    }
                    handler.bundle("compose-ui") {
                        it.includeGroup("androidx.compose.ui")
                    }
                    handler.bundle("coil") {
                        it.includeGroup("io.coil-kt")
                    }
                }
                ext.issues { handler ->
                    handler.all { projectIssueHandler ->
                        projectIssueHandler.onAny {
                            it.exclude(
                                "androidx.sqlite:sqlite",
                                "org.jetbrains.kotlinx:kotlinx-serialization-json"
                            )
                        }
                        projectIssueHandler.onIncorrectConfiguration { it.severity("warn") }
                        projectIssueHandler.onUnusedDependencies { it.severity("fail") }
                        projectIssueHandler.onUsedTransitiveDependencies { it.severity("fail") }
                        projectIssueHandler.onCompileOnly { it.severity("fail") }
                        projectIssueHandler.onRuntimeOnly { it.severity("fail") }
                        projectIssueHandler.onUnusedAnnotationProcessors { it.severity("fail") }
                        projectIssueHandler.onRedundantPlugins { it.severity("fail") }
                    }
                }
            }
        }
    }
}
