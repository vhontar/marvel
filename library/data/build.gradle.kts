plugins {
    id("com.v7v.android.library")
    id("com.v7v.setup.dev.properties")
    kotlin("plugin.serialization") version("2.0.21") // TODO add to custom gradle plugin
}

android {
    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"${devProperties.getByNameOrDefault(project, "base_url")}\"")
        buildConfigField("String", "PRIVATE_API_KEY", "\"${devProperties.getByNameOrDefault(project, "marvel_private_key")}\"")
        buildConfigField("String", "PUBLIC_API_KEY", "\"${devProperties.getByNameOrDefault(project, "marvel_api_key")}\"")
    }
}

dependencies {
    implementation(project(":logger"))
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.negotiation)
    implementation(libs.ktor.serialization)
    implementation(libs.koin)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    ksp(libs.room.compiler)

    implementation(libs.paging.common)
    implementation(libs.paging.runtime)
}
