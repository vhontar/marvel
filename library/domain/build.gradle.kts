plugins {
    id("com.v7v.android.library")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.paging.common)

    api(libs.textref)
}
