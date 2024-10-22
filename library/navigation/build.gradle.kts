plugins {
    id("com.v7v.android.library")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21" // TODO add to custom plugin
}

dependencies {
    implementation(project(":feature:home"))
    implementation(project(":feature:character-details"))
    implementation(project(":feature:comic-details"))

    api(libs.kotlinx.serialization)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.navigation.common)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime)
}
