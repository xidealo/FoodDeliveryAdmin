plugins {
    kotlin(Plugin.android)
    id(Plugin.androidLibrary)
    id(Plugin.kapt)
    id(Plugin.kotlinSerialization)
}

android {
    compileSdk = AndroidSdk.compile

    defaultConfig {
        minSdk = AndroidSdk.min
        targetSdk = AndroidSdk.target
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

    implementation(Coroutine.coroutineCore)

    //Database
    implementation(RoomDatabase.roomDatabaseRuntime)
    implementation(RoomDatabase.roomDatabaseKtx)
    kapt(RoomDatabase.roomDatabaseKapt)

    //Hilt
    implementation(Dagger.hilt)
    kapt(Dagger.hiltCompiler)

    //DataStore
    implementation(DataStore.dataStorePreferences)

    Ktor.run {
        implementation(clientSerialization)
        implementation(clientLogging)
        implementation(clientWebsockets)
        implementation(negotiation)
        implementation(serializerJson)
        implementation(clientJson)
        implementation(clientAuth)
        implementation(clientOkhttp)
    }
}