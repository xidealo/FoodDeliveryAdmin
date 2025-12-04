pluginManagement {
    includeBuild("build-logic")

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":presentation")
include(":common")
include(":domain")
include(":data")
include(":app")
rootProject.name = "FoodDeliveryAdmin"