import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.admin.multiplatform.feature)
}

android {
    namespace = Namespace.data
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":domain"))
                implementation(project(":common"))
                implementation(project(":core"))

                // Koin
                implementation(libs.koin.core)

                // Coroutines
                implementation(libs.kotlinx.coroutines.core)

                // Ktor
                implementation(libs.bundles.ktor)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.okhttp)

                // Firebase
                implementation(project.dependencies.platform(libs.firebase.bom))
                implementation(libs.firebase.storage)
                implementation(libs.firebase.messaging)

                // Work manager
                implementation(libs.work.runtime.ktx)
                implementation(libs.koin.android.workmanager)

                // DataStore
                implementation(libs.datastore.preferences)

                // Koin
                implementation(libs.bundles.di)
            }
        }

        val androidUnitTest by getting {
            dependencies {
                // Testing
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.bundles.mockk)
            }
        }
    }
}
