
plugins {
    alias(libs.plugins.admin.multiplatform.feature)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common"))

                // Time
                implementation(libs.kotlinx.datetime)

                // Koin
                implementation(libs.koin.core)

                // Coroutines
                implementation(libs.kotlinx.coroutines.core)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(kotlin("test"))
                // Mocks for testing
                implementation(libs.bundles.mockk)
                // Coroutine
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}
android {
    namespace = Namespace.domain
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}
