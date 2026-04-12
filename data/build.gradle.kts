plugins {
    alias(libs.plugins.admin.multiplatform.feature)
    alias(libs.plugins.kotlin.serialization)
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

                implementation(libs.koin.core)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktor.client.websockets)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.json)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.serialization)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.auth)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(project.dependencies.platform(libs.firebase.bom))
                implementation(libs.firebase.storage)
                implementation(libs.firebase.messaging)
                implementation(libs.work.runtime.ktx)
                implementation(libs.kotlinx.coroutines.services)
                implementation(libs.datastore.preferences)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.bundles.di)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.bundles.mockk)
            }
        }
    }
}
