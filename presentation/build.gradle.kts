
plugins {
    alias(libs.plugins.admin.multiplatform.feature)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.plugin)
}

ktlint {
    filter {
        exclude {
            val path = it.file.absolutePath.replace('\\', '/')
            path.contains("/build/generated/")
        }
    }
}

tasks.named("runKtlintCheckOverAndroidMainSourceSet") {
    enabled = false
}
tasks.named("ktlintAndroidMainSourceSetCheck") {
    enabled = false
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
        val androidMain by getting {
            dependencies {
                implementation(libs.android.image.cropper)
                implementation(compose.uiTooling)
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
