import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ktLint)
}

android {
    namespace = Namespace.domain

    compileSdk = AndroidSdk.compile
    defaultConfig {
        minSdk = AndroidSdk.min
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
            freeCompilerArgs.add("-Xstring-concat=inline")
        }
    }
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
