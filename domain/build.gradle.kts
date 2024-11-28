plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ktLint)
}

android {
    namespace = Namespace.domain

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
    implementation(project(":common"))

    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)
    implementation(libs.joda.time)

    // Mocks for testing
    implementation(libs.bundles.mockk)

    // Coroutine
    implementation(libs.kotlinx.coroutines.test)

    // Test
    testImplementation(libs.kotlin.test)
}
