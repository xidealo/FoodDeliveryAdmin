plugins {
    kotlin(Plugin.android)
    id(Plugin.androidLibrary)
    id(Plugin.kapt)
    id(Plugin.kotlinParcelize)
    id(Plugin.hiltPlugin)
    id(Plugin.ktLint) version Versions.ktLint
}

android {
    namespace = Namespace.domain

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
    implementation(project(":common"))

    implementation(Dagger.hilt)
    kapt(Dagger.hiltCompiler)
    implementation(Time.jodaTime)

    // Mocks for testing
    implementation(Mockk.main)
    implementation(Mockk.common)

    // Coroutine
    implementation(Coroutines.test)

    // Test
    testImplementation(kotlin(Test.test))
}
