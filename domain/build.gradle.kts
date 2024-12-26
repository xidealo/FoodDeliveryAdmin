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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs = listOf("-Xstring-concat=inline")
    }
}

dependencies {
    implementation(project(":common"))

//    implementation(libs.dagger.hilt)
//    kapt(libs.dagger.hilt.compiler)
    implementation(libs.joda.time)

    // koin
    implementation(libs.bundles.di)

    // Mocks for testing
    testImplementation(libs.bundles.mockk)

    // Coroutine
    testImplementation(libs.kotlinx.coroutines.test)

    // Test
    testImplementation(libs.kotlin.test)
}
