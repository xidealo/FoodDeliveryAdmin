plugins {
    alias(libs.plugins.admin.android.feature)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = Namespace.data
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":common"))

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage)

    // Work manager
    implementation(libs.work.runtime.ktx)

    // Coroutine
    implementation(libs.kotlinx.coroutines.services)

    // Koin
    implementation(libs.bundles.di)

    // DataStore
    implementation(libs.datastore.preferences)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)

    implementation(libs.bundles.ktor)

    // Testing
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.bundles.mockk)
}
