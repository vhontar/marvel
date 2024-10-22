plugins {
    id("com.v7v.android.library")
}

dependencies {
    implementation(project(":logger"))

    implementation(libs.textref)

    implementation(libs.paging.common)
    implementation(libs.paging.runtime)
}
