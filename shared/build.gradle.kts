
plugins {
    alias(libs.plugins.admin.multiplatform.feature)
    alias(libs.plugins.compose)
    alias(libs.plugins.cocoa)
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

    cocoapods {
        summary = "Main shared module with presentation layer"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget =  "15.5"
        podfile = project.file("../FoodDelivery/Podfile")

        pod("FirebaseMessaging")
        pod("MapLibre", "6.17.1")

        framework {
            baseName = "shared"
            isStatic = true
            binaryOption("bundleId", "com.bunbeauty.fooddeliveryadmin.shared")
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":domain"))
                implementation(project(":common"))
                implementation(project(":data"))

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

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:3.3.2")
                implementation("io.ktor:ktor-client-content-negotiation:3.3.2")
                implementation("io.ktor:ktor-serialization-kotlinx-json:3.3.2")
            }
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
    }
}
