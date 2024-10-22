plugins {
    id("com.v7v.android.library")
}

dependencies {
    implementation(project(":data"))
    implementation(project(":logger"))
    implementation(project(":navigation"))
    implementation(project(":feature:home"))
    implementation(project(":feature:character-details"))
    implementation(project(":feature:comic-details"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.activity)

    implementation(libs.koin.core)
    implementation(libs.koin)
}
