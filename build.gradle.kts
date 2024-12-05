// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    kotlin("plugin.serialization") version "2.1.0"

    kotlin("kapt") version "2.1.0"
}

buildscript {
    repositories {
        google()
    }
    dependencies {
        val nav_version = "2.8.1"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}