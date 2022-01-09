
plugins {
    kotlin(Plugin.android)
    id(Plugin.kotlinAndroid)
    id(Plugin.androidLibrary)
    id(Plugin.kapt)
    id(Plugin.kotlinSerialization)
    id(Plugin.hiltPlugin)
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
    
    implementation(Ktor.clientJson)
    implementation(Ktor.clientLogging)
    implementation(Ktor.clientSerialization)
    implementation(Ktor.clientAndroid)
    implementation(Ktor.clientOkhttp)

/*    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:26.7.0"))
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")*/
}