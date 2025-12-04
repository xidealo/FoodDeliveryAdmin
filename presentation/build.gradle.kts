plugins {
    alias(libs.plugins.admin.android.feature)
}

android {
    namespace = Namespace.presentation
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":common"))

    implementation(libs.lifecycle.viewmodel.ktx)

    // Navigation
    implementation(libs.navigation.runtime.ktx)

    // Koin
    implementation(libs.bundles.di)

    // Mocks for testing
    testImplementation(libs.bundles.mockk)

    // Coroutine
    implementation(libs.kotlinx.coroutines.test)

    // Test
    testImplementation(libs.kotlin.test)
    testImplementation(libs.turbine)
}
