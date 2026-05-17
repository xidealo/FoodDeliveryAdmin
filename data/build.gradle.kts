import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.admin.multiplatform.feature)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.cocoa)
}

val localProperties =
    Properties().apply {
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            FileInputStream(localPropertiesFile).use(::load)
        }
    }

fun localProperty(key: String): String =
    localProperties
        .getProperty(key)
        .orEmpty()
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")

android {
    namespace = Namespace.data

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "YC_ACCESS_KEY", "\"${localProperty("yc.accessKey")}\"")
        buildConfigField("String", "YC_SECRET_KEY", "\"${localProperty("yc.secretKey")}\"")
        buildConfigField("String", "YC_BUCKET", "\"${localProperty("yc.bucket")}\"")
    }
}

kotlin {

    cocoapods {
        summary = "Main shared module with presentation layer"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "15.5"
        podfile = project.file("../FoodDelivery/Podfile")

        pod("FirebaseMessaging")

        framework {
            baseName = "data"
            isStatic = true
        }
    }

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
                implementation(libs.firebase.messaging)
                implementation(libs.work.runtime.ktx)
                implementation(libs.kotlinx.coroutines.services)
                implementation(libs.datastore.preferences)
                implementation(libs.ktor.client.okhttp)
                // implementation(libs.ktor.client.cio)
                implementation(libs.bundles.di)
                implementation(libs.aws.s3)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
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
