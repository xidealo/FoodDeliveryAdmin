import org.gradle.api.Plugin

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(ClassPath.kotlinPlugin)
        classpath(ClassPath.gradle)
        classpath(ClassPath.navigationServices)
        classpath(ClassPath.kotlinSerialization)
        classpath(ClassPath.googleServices)
        classpath(ClassPath.hiltServices)
        classpath(ClassPath.crashlytics)
    }
}
plugins {
    id("org.jlleitschuh.gradle.ktlint") version Versions.ktLint
}
allprojects {
    repositories {
        google()
        mavenCentral()
    }

}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}