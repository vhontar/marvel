plugins {
    id("com.v7v.android.library")
}

dependencies {
    api(project(":domain"))

    api(libs.kotlinx.coroutines.core)

    implementation(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)

    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.koin.core.viewmodel)

    api(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.koin.androidx.compose)

    implementation(libs.coil)

    implementation(libs.paging.common)
    implementation(libs.paging.compose)
}
