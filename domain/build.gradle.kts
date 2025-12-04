
plugins {
    alias(libs.plugins.admin.android.feature)
}

android {
    namespace = Namespace.domain

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(project(":common"))

    implementation(libs.joda.time)

    // Koin
    implementation(libs.bundles.di)

    // Mocks for testing
    testImplementation(libs.bundles.mockk)

    // Coroutine
    testImplementation(libs.kotlinx.coroutines.test)

    // Test
    testImplementation(libs.kotlin.test)
}
