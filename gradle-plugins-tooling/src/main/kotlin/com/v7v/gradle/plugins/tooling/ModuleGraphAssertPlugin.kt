package com.v7v.gradle.plugins.tooling

import com.jraska.module.graph.assertion.GraphRulesExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class ModuleGraphAssertPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.jraska.module.graph.assertion")

        extensions.configure<GraphRulesExtension>("moduleGraphAssert") { ext ->
            ext.maxHeight = 4
            ext.allowed = arrayOf(
                ":core -> :.*",
                ":navigation -> :.*",
                ":feature:.* -> :domain",
                ":.* -> :logger",
                ":data -> :domain"
            )
            ext.restricted = arrayOf(
                ":.* -X> :core",
                ":feature:.* -X> :data",
                ":feature:.* -X> :navigation",
                ":feature:.* -X> :feature:.*",
                ":data -X> :feature:.*",
                ":domain -X> :data"
            )
            ext.assertOnAnyBuild = false
        }
    }
}
