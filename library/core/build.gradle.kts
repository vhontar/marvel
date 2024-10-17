plugins {
    id("com.v7v.android.library")
}

dependencies {
    implementation(project(":data"))
    implementation(project(":logger"))
    implementation(project(":navigation"))
    implementation(project(":feature:home"))
    implementation(project(":feature:character-details"))

    implementation(libs.koin)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)
}