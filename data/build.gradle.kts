plugins {
    kotlin(Plugin.android)
    id(Plugin.androidLibrary)
    id(Plugin.kapt)
    id(Plugin.kotlinSerialization)
    id(Plugin.ktLint) version Versions.ktLint
}

android {
    namespace = Namespace.data

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
            isMinifyEnabled = true
        }
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":common"))

    // Firebase
    implementation(platform(Firebase.bom))
    implementation(Firebase.storage)

    // Work manager
    implementation(WorkManager.runtime)

    // Coroutine
    implementation(Coroutines.playServices)

    // Database
    implementation(RoomDatabase.roomDatabaseRuntime)
    implementation(RoomDatabase.roomDatabaseKtx)
    kapt(RoomDatabase.roomDatabaseKapt)

    // Hilt
    implementation(Dagger.hilt)
    kapt(Dagger.hiltCompiler)
    implementation(Dagger.androidxHiltWork)
    kapt(Dagger.androidxHiltCompiler)

    // DataStore
    implementation(DataStore.dataStorePreferences)

    implementation(platform(Firebase.bom))
    implementation(Firebase.messaging)

    Ktor.run {
        implementation(clientLogging)
        implementation(clientWebsockets)
        implementation(clientContentNegotiation)
        implementation(clientSerialization)
        implementation(serializationJson)
        implementation(clientJson)
        implementation(clientAuth)
        implementation(clientOkhttp)
    }
}
