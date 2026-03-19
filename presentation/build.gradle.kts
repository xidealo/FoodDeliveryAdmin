
plugins {
    alias(libs.plugins.admin.multiplatform.feature)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.plugin)
}

android {
    namespace = Namespace.presentation
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":domain"))
                implementation(project(":common"))

                // Koin
                implementation(libs.bundles.di)

                // Coroutine
                implementation(libs.kotlinx.coroutines.test)

                // Map
                implementation(libs.maplibre.compose)

                implementation(libs.bundles.navigation.new)

                implementation(compose.components.resources)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.animation)
                implementation(compose.animationGraphics)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.bundles.coil)
                implementation(libs.kotlinx.collections.immutable)
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
}
