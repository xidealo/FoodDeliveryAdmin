import com.github.triplet.gradle.androidpublisher.ReleaseStatus
import com.github.triplet.gradle.play.PlayPublisherExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.navigation)
    alias(libs.plugins.google.service)
    alias(libs.plugins.crashlytics.firebase)
    alias(libs.plugins.triplet.play)
    alias(libs.plugins.ktLint)
}

android {
    namespace = Namespace.app

    compileSdk = AndroidSdk.compile
    defaultConfig {
        applicationId = Application.applicationId
        minSdk = AndroidSdk.min
        targetSdk = AndroidSdk.target
        versionCode = Application.versionCode
        versionName = Application.versionName
        signingConfig = signingConfigs.getByName("debug")
    }

    signingConfigs {
        create("release") {
            storeFile = file(getProperty("RELEASE_STORE_FILE"))
            storePassword = getProperty("RELEASE_STORE_PASSWORD")
            keyAlias = getProperty("RELEASE_KEY_ALIAS")
            keyPassword = getProperty("RELEASE_KEY_PASSWORD")

            enableV1Signing = true
            enableV2Signing = true
        }
    }

    buildTypes {
        applicationVariants.all {
            val variant = this
            variant.outputs
                .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
                .forEach { output ->
                    val outputFileName =
                        "Admin_${variant.baseName}_${Application.versionName}.apk"
                    println("OutputFileName: $outputFileName")
                    output.outputFileName = outputFileName
                }
        }
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }

        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }

        buildFeatures {
            viewBinding = true
            compose = true
            buildConfig = true
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
        kotlin {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_11)
                freeCompilerArgs.add("-Xstring-concat=inline")
            }
        }
        composeOptions {
            kotlinCompilerExtensionVersion = "1.5.0"
        }
        playConfigs {
            register("release") {
                commonPlayConfig(this)
            }
        }
    }
}

fun getProperty(key: String): String {
    val propertiesFile = rootProject.file("./local.properties")
    val properties = Properties()
    properties.load(FileInputStream(propertiesFile))
    val property = properties.getProperty(key)
    if (property == null) {
        println("Property with key $key not found")
    }
    return property
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":common"))
    implementation(project(":presentation"))

    // Navigation
    implementation(libs.bundles.navigation)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.firebase.crashlytics.ktx)

    // Work manager
    implementation(libs.work.runtime.ktx)

    // Lifecycle
    implementation(libs.lifecycle.service)

    // Material
    implementation(libs.material)

    // ViewBinding
    implementation(libs.viewbindingpropertydelegate)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)

    // Date time picker
    implementation(libs.material.dialogs.datetime)

    // Koin
    implementation(libs.bundles.di)
    implementation(libs.koin.android.workmanager)
    implementation(libs.koin.test)
    implementation(libs.koin.test.junit)

    // Coil
    implementation(libs.bundles.coil)

    implementation(libs.kotlinx.collections.immutable)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)

    // Image cropper
    implementation(libs.android.image.cropper)

    // Time
    implementation(libs.kotlinx.datetime)
}

fun commonPlayConfig(playPublisherExtension: PlayPublisherExtension) {
    with(playPublisherExtension) {
        track.set("production")
        defaultToAppBundles.set(true)
        userFraction.set(0.99)
        serviceAccountCredentials.set(file("google-play-api-key.json"))
        releaseStatus.set(ReleaseStatus.IN_PROGRESS)
    }
}
