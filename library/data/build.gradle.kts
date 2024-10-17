plugins {
    id("com.v7v.android.library")
    kotlin("plugin.serialization") version("2.0.21") // TODO add to custom gradle plugin
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
