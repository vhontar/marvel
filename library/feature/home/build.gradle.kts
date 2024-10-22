plugins {
    id("com.v7v.android.library")
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":logger"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)

    implementation(libs.paging.common)
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    implementation(libs.koin)
    implementation(libs.koin.viewmodel)
    implementation(libs.koin.compose)

    implementation(libs.coil)
}
