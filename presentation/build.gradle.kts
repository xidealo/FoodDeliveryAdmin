plugins {
    kotlin(Plugin.android)
    id(Plugin.androidLibrary)
    id(Plugin.kapt)
    id(Plugin.hiltPlugin)
}

android {
    namespace = Namespace.presentation

    compileSdk = AndroidSdk.compile
    defaultConfig {
        minSdk = AndroidSdk.min
        targetSdk = AndroidSdk.target
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

    implementation(Lifecycle.viewModel)

    // TODO remove
    // Navigation
    implementation(Navigation.runtime)

    // Hilt
    implementation(Dagger.hilt)
    kapt(Dagger.hiltCompiler)
    implementation("javax.inject:javax.inject:1")

    //DataStore
    implementation(DataStore.dataStorePreferences)

    // Mocks for testing
    implementation(Mockk.main)
    implementation(Mockk.common)

    // Coroutine
    implementation(Coroutine.test)

    // Test
    testImplementation(kotlin(Test.test))
}