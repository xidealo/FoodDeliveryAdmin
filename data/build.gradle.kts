plugins {
    kotlin(Plugin.android)
    id(Plugin.androidLibrary)
    id(Plugin.kapt)
    id(Plugin.kotlinSerialization)
}

android {
    namespace = Namespace.data

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
            isMinifyEnabled = true
        }
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":common"))

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