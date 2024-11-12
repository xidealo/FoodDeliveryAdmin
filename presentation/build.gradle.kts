plugins {
    kotlin(Plugin.android)
    id(Plugin.androidLibrary)
    id(Plugin.kapt)
    id(Plugin.hiltPlugin)
    id(Plugin.ktLint) version Versions.ktLint
}

android {
    namespace = Namespace.presentation

    compileSdk = AndroidSdk.compile
    defaultConfig {
        minSdk = AndroidSdk.min
        targetSdk = AndroidSdk.target
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs = listOf("-Xstring-concat=inline")
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

    implementation(Lifecycle.viewModel)

    // TODO remove
    // Navigation
    implementation(Navigation.runtime)

    // Hilt
    implementation(Dagger.hilt)
    kapt(Dagger.hiltCompiler)
    implementation("javax.inject:javax.inject:1")

    // Mocks for testing
    implementation(Mockk.main)
    implementation(Mockk.common)

    // Coroutine
    implementation(Coroutines.test)

    // Test
    testImplementation(kotlin(Test.test))
    testImplementation(Turbine.turbine)
}
