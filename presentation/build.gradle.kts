plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ktLint)
}

android {
    namespace = Namespace.presentation

    compileSdk = AndroidSdk.compile
    defaultConfig {
        minSdk = AndroidSdk.min
        lint.targetSdk = AndroidSdk.target
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }

        getByName("release") {
            isMinifyEnabled = false
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

    // Hilt
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)
    implementation(libs.javax.inject)

    // Mocks for testing
    implementation(libs.bundles.mockk)

    // Coroutine
    implementation(libs.kotlinx.coroutines.test)

    // Test
    testImplementation(libs.kotlin.test)
    testImplementation(libs.turbine)
}
