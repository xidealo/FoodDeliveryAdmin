plugins {
    kotlin(Plugin.android)
    id(Plugin.kotlinAndroid)
    id(Plugin.androidLibrary)
    id(Plugin.kapt)
    id(Plugin.kotlinSerialization)
    id(Plugin.hiltPlugin)
    id(Plugin.googleService)
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
            buildConfigField("String", "FB_LINK", "\"https://test-fooddelivery.firebaseio.com/\"")
        }

        getByName("release") {
            isMinifyEnabled = false
            buildConfigField(
                "String",
                "FB_LINK",
                "\"https://fooddelivery-ce2ef-default-rtdb.firebaseio.com/\""
            )
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

    //Firebase
    implementation(platform(Firebase.bom))
    implementation(Firebase.messaging)
}