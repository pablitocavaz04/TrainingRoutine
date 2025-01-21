// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("androidx.navigation.safeargs.kotlin") version "2.8.4" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin) apply false

}
buildscript {
    repositories {
        google()
    }
    dependencies{
        classpath (libs.secrets.gradle.plugin)
    }
}