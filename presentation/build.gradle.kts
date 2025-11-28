import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ktLint)
}

android {
    namespace = Namespace.presentation

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
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":common"))

    implementation(libs.lifecycle.viewmodel.ktx)

    // TODO remove
    // Navigation
    implementation(libs.navigation.runtime.ktx)

    // Koin
    implementation(libs.bundles.di)

    // Mocks for testing
    testImplementation(libs.bundles.mockk)

    // Coroutine
    implementation(libs.kotlinx.coroutines.test)

    // Test
    testImplementation(libs.kotlin.test)
    testImplementation(libs.turbine)
}
