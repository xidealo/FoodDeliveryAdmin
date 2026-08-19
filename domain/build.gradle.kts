
plugins {
    alias(libs.plugins.admin.multiplatform.feature)
}

kotlin {
    android {
        namespace = Namespace.domain
    }

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
        val androidHostTest by getting {
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
