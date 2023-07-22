plugins {
    kotlin(Plugin.android)
    id(Plugin.kotlinAndroid)
    id(Plugin.androidLibrary)
    id(Plugin.kapt)
    id(Plugin.kotlinParcelize)
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

    //Navigation
    implementation(Navigation.runtime)

    //Hilt
    implementation(Dagger.hilt)
    kapt(Dagger.hiltCompiler)
    implementation("javax.inject:javax.inject:1")

    //DataStore
    implementation(DataStore.dataStorePreferences)
}