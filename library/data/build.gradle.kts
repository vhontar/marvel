plugins {
    id("com.v7v.android.library")
    id("com.v7v.setup.dev.properties")
    kotlin("plugin.serialization") version("2.0.21") // TODO add to custom gradle plugin
}

android {
    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"${devProperties.getByNameOrDefault(project, "base_url")}\"")
        buildConfigField(
            "String",
            "PRIVATE_API_KEY",
            "\"${devProperties.getByNameOrDefault(project, "marvel_private_key")}\"",
        )
        buildConfigField(
            "String",
            "PUBLIC_API_KEY",
            "\"${devProperties.getByNameOrDefault(project, "marvel_api_key")}\"",
        )
    }
}

dependencies {
    implementation(project(":logger"))
    implementation(project(":domain"))

    implementation(libs.androidx.annotation)
    implementation(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.serialization)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.io)
    implementation(libs.ktor.http)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.utils)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.negotiation)

    api(libs.koin.core)

    implementation(libs.room.common)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    api(libs.room.runtime)
    ksp(libs.room.compiler)

    api(libs.paging.common)
    api(libs.paging.common.android)
}
